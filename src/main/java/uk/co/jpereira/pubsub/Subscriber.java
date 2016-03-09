package uk.co.jpereira.pubsub;

import java.util.ArrayList;
import java.util.List;

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
	/**
	 * Client used to send and retrieve information
	 */
	private Client client;
	
	/**
	 * Class constructor
	 */
	public Subscriber() {
	}
	
	/**
	 * Subscribe to a specific feed
	 * @param feed Feed to subscribe to
	 * @throws java.lang.NullPointerException If the initClient function is not called
	 */
	public void subscribe(String feed) {
		List<String> allFeeds = new ArrayList<>();
		allFeeds.add(feed);
		RegistrationPacket packet = new RegistrationPacket(allFeeds);
		client.registerFeed(packet);
	}
	
	/**
	 * Read the data from the Publisher
	 * This method blocks until the Publisher publishes something
	 * @return Data sent from the subscriber
	 */
	public TransferData read() {
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
}
