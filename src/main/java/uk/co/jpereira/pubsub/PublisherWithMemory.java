package uk.co.jpereira.pubsub;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.connection.Connection;
import uk.co.jpereira.pubsub.connection.Server;

public abstract class PublisherWithMemory extends Publisher {
	private static final Logger logger = LoggerFactory.getLogger(PublisherWithMemory.class);

	Map<String, TransferData> memory;
	/**
	 * Publish Information to a specific feed
	 * @param feed Feed to publish the data to
	 * @param data Information to be published
	 * @throws DisconnectedPublisherException 
	 */
	@Override
	public void publish(String feed, TransferData data) throws DisconnectedPublisherException {
		if(memory == null)
			memory = new HashMap<>();
		super.publish(feed, data);
		memory.put(feed, data);
	}
	/**
	 * Function used to initialize the server
	 * The server need to be created in the parent class and the
	 * this class will initialize all the needed functionality to make the server responsive
	 * @param server Server
	 * @throws java.lang.NullPointerException when the servers was not initialized
	 */
	protected void initServer(Server server) {
		server.registerWaitForConnection(new NewConnectionObserver());
		super.initServer(server);
	}
	/**
	 * Internal class
	 * This class should only be used inside this class
	 * 
	 * This class is used as an Observer to register the 
	 * Registration Packet on a specific connection
	 * 
	 * @author joaopapereira@gmail.com
	 */
	protected class NewConnectionObserver implements Observer {
		Connection con = null;

		@Override
		public synchronized void update(Observable observable, Object obj) {
			con = (Connection) obj;
			con.registerReadObserver(RegistrationPacket.class, new ConnectionObserver());
		}
	}
	
	/**
	 * Internal class
	 * This class should only be used inside this class
	 * 
	 * This class is used as an Observer that will be triggered every time
	 * that a RegistrationPacket is received by the connection and will
	 * save the connection to a specific client 
	 * 
	 * @author joaopapereira@gmail.com
	 *
	 */
	protected class ConnectionObserver implements Observer {
		RegistrationPacket packet = null;

		@Override
		public synchronized void update(Observable observable, Object obj) {
			packet = (RegistrationPacket)obj;
			Connection con = (Connection)observable;
			for(String feed: packet.getAllFeeds()) {
				try {
					con.send(memory.get(feed));
				} catch (DisconnectedPublisherException e) {
					logger.error("Unable to send the feed '" + feed + "' to the connection '" + con + "'");
				}
			}
		}
	}
	
}
