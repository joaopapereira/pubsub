package uk.co.jpereira.pubsub.connection;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.TransferData;

/**
 * Interface used to represent the connection between Client and Server\
 * This object should live in both sides of the connection
 * @author joaopapereira@gmail.com
 *
 */
public interface Connection extends Observable {
	
	/**
	 * Function to send information through this connection
	 * @param data Information to be sent
	 */
	public void send(TransferData data);
	
	/**
	 * Function to receive information through this connection
	 * This is a blocking method that will wait for information in the channel
	 * @return Data transfered through this connection
	 */
	public TransferData receive();
	
	/**
	 * Register the observer that will be called when a specific Data is transfered
	 * between the channel
	 * @param typeOfObjects Type of object that will trigger the observer
	 * @param observer Observer to be triggered
	 */
	public void registerReadObserver(Class<?> typeOfObjects, Observer observer);
	
	/**
	 * Check if this connection is still connected
	 * @return True if still active false otherwise
	 */
	public boolean isConnected();
}
