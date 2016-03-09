package uk.co.jpereira.pubsub.connection.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.DisconnectedPublisherException;
import uk.co.jpereira.pubsub.KeepAlivePacket;
import uk.co.jpereira.pubsub.RegistrationPacket;
import uk.co.jpereira.pubsub.TransferData;
import uk.co.jpereira.pubsub.connection.Client;

/**
 * TCP implementation of the client
 * This implementation creates a socket connection
 * to the publisher
 * @author jpereira
 *
 */
public class TCPClient implements Client {

	private static final Logger logger = LoggerFactory.getLogger(TCPClient.class);
	private TCPConnection connection = null;
	private TCPConfig config = null;
	/**
	 * Class constructor
	 * @param config TCP Configuration to connect to the publisher 
	 */
	public TCPClient(TCPConfig config) {
		this.config = config;
		connect();
		
	}
	
	/**
	 * Connect to the server
	 */
	public void connect() {
		logger.info("Going to connect to: " + config.getUrl() + "," + config.getPort());
		connection = new TCPConnection(config);

		logger.info("Connected");
	}
	
	/**
	 * Check if the connection is still alive
	 * @return True if is alive, false otherwise
	 */
	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}
	
	@Override
	public void registerWait(Observer object) {
		// TODO Auto-generated method stub
		connection.registerReadObserver(TransferData.class, object);
	}

	@Override
	public void registerFeed(RegistrationPacket packet) throws DisconnectedPublisherException {
		connection.send(packet);
	}
	
	@Override
	public TransferData read() throws DisconnectedPublisherException{
		return connection.receive();

	}
	
	@Override
	public void sendKeepAlive() throws DisconnectedPublisherException {
		// TODO Auto-generated method stub
		KeepAlivePacket keepAlive = new KeepAlivePacket();
		connection.send(keepAlive);
		
	}
	@Override
	public boolean isAlive() {
		return connection.isConnected();
	}
}
