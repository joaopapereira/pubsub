package uk.co.jpereira.pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.connection.Connection;
import uk.co.jpereira.pubsub.connection.Server;
/**
 * Publisher class of the Publisher subscriber Design Pattern
 * This class need to be implemented in order to use it, for sample implementations 
 * the class L{uk.co.jpereira.pubsub.examples.tcp.TCPPublisher} can be used
 * 
 * @author joaopapereira@gmail.com
 *
 */
public abstract class Publisher {
	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	private Server server = null;
	private Map<String, List<Connection>> allConnections;
	private NewConnectionObserver newConnection;
	
	/**
	 * Class constructor
	 * This need to be called from the parent or else will cause exceptions
	 */
	public Publisher() {
		allConnections = new HashMap<>();
	}
	
	/**
	 * Publish Information to a specific feed
	 * @param feed Feed to publish the data to
	 * @param data Information to be published
	 */
	public void publish(String feed, TransferData data){
		logger.trace("publish({}, {})", feed, data);
		if(allConnections.containsKey(feed)) {
			for(Connection con: allConnections.get(feed)) {
				logger.debug("Sending data to the connection: " + con);
				con.send(data);
			}
		} else {
			logger.debug("No connections configured to receive the data");
		}
	}
	
	/**
	 * Add a new connection into a specific feed
	 * Protected function
	 * 
	 * @param feed Feed to add the connection to
	 * @param connection Connection to add to the feed
	 */
	protected void addConnection(String feed, Connection connection) {
		List<Connection> connections = allConnections.get(feed);
		if(connections == null)
			connections = new ArrayList<>();
		connections.add(connection);
		allConnections.put(feed, connections);
	}
	
	/**
	 * Function used to initialize the server
	 * The server need to be created in the parent class and the
	 * this class will initialize all the needed functionality to make the server responsive
	 * @param server Server
	 * @throws java.lang.NullPointerException when the servers was not initialized
	 */
	protected void initServer(Server server) {
		logger.info("Init server");
		this.server = server;
		newConnection = new NewConnectionObserver();
		this.server.registerWaitForConnection(newConnection);
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
			logger.info("New connection stablished: " + con);
			con.registerObserverNewObject(RegistrationPacket.class, new ConnectionObserver());
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
			logger.info("Received: " + con);
			for(String feed: packet.getAllFeeds()) {
				addConnection(feed, con);
			}
		}
	}
}
