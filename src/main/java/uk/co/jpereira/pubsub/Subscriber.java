package uk.co.jpereira.pubsub;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.pubsub.connection.Client;

/**
 * Subscriber class of the Publisher subscriber Design Pattern
 * This class need to be implemented in order to use it, for sample implementations 
 * the class L{uk.co.jpereira.pubsub.examples.tcp.TCPPublisher} can be used
 * 
 * @author joaopapereira@gmail.com
 *
 */
public abstract class Subscriber {
	private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
	private long keepAliveTimeout = 2000;
	private boolean keepWorking = true;
	/**
	 * Client used to send and retrieve information
	 */
	private Client client;
	
	public Subscriber(long keepAliveTimeout) {
		this.keepAliveTimeout = keepAliveTimeout;
		if(this.keepAliveTimeout > 500)
			startKeepAlive();
	}
	
	/**
	 * Class constructor
	 */
	public Subscriber() {
		startKeepAlive();
	}
	
	/**
	 * Subscribe to a specific feed
	 * @param feed Feed to subscribe to
	 * @throws java.lang.NullPointerException If the initClient function is not called
	 */
	public void subscribe(String feed) throws DisconnectedPublisherException {
		List<String> allFeeds = new ArrayList<>();
		allFeeds.add(feed);
		RegistrationPacket packet = new RegistrationPacket(allFeeds);
		client.registerFeed(packet);
	}
	
	/**
	 * Read the data from the Publisher
	 * This method blocks until the Publisher publishes something
	 * @return Data sent from the subscriber
	 * @throws DisconnectedPublisherException 
	 */
	public TransferData read() throws DisconnectedPublisherException{
		return client.read();
	}
	
	/**
	 * Initialize the client
	 * This function need to be called by the classes that implement the 
	 * subscriber
	 * 
	 * @param client Client that will be used to get information from the Publisher
	 */
	protected void initClient(Client client) {
		this.client = client;
	}
	
	protected void startKeepAlive() {
		new Thread() {
			@Override
			public void run() {
				while(keepWorking) {
					try {
						sleep(keepAliveTimeout);
						logger.info("Sending keep alive");
						if(client.isAlive())
							client.sendKeepAlive();
						else {
							logger.error("Client no longer alive, stopping keep alive and");
							keepWorking = false;
						}
					} catch (InterruptedException e) {
					} catch (DisconnectedPublisherException e) {
						logger.error("Client no longer alive, stopping keep alive and");
						keepWorking = false;
					}
					
				}
			}
		}.start();
	}
}
