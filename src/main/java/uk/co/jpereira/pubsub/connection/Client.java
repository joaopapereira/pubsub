package uk.co.jpereira.pubsub.connection;

import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.RegistrationPacket;
import uk.co.jpereira.pubsub.TransferData;

/**
 * Interface implemented by Clients
 * This is the minimum interface for the client used by the  
 * Subscriber to retrieve the information from the Publisher
 * 
 * @author joaopapereira@gmail.com
 *
 */
public interface Client {
	/**
	 * Blocking function to wait and read from the Publisher
	 * @return Data sent by the Publisher
	 */
	public TransferData read();
	
	/**
	 * Non blocking function to retrieve the Publisher data
	 * This Observer will be called when new information is received from the Publisher
	 * WIll be called with (Connection, TransferData)
	 * @param observer Observer
	 */
	public void registerWait(Observer observer);
	
	/**
	 * Register the subscriber to a feed or list of feeds 
	 * @param packet Packet of registration
	 */
	public void registerFeed(RegistrationPacket packet);
}
