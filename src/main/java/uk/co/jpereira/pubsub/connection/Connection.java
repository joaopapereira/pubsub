package uk.co.jpereira.pubsub.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.DisconnectedPublisherException;
import uk.co.jpereira.pubsub.TransferData;

/**
 * Interface used to represent the connection between Client and Server\
 * This object should live in both sides of the connection
 * @author joaopapereira@gmail.com
 *
 */
public abstract class Connection implements Observable {

	public Map<Class<?>, List<Observer>> observers;
	boolean keepConnection = true;
	public Thread newObject = null;
	
	/**
	 * Function to send information through this connection
	 * @param data Information to be sent
	 * @throws DisconnectedPublisherException 
	 */
	public abstract void send(TransferData data) throws DisconnectedPublisherException;
	
	/**
	 * Function to receive information through this connection
	 * This is a blocking method that will wait for information in the channel
	 * @return Data transfered through this connection
	 */
	public abstract TransferData receive() throws DisconnectedPublisherException;
	
	/**
	 * Register the observer that will be called when a specific Data is transfered
	 * between the channel
	 * @param typeOfObjects Type of object that will trigger the observer
	 * @param observer Observer to be triggered
	 */
	public void registerReadObserver(Class<?> typeOfObjects, Observer observer) {
		if(newObject == null) {
			newObject = new Thread(){
				@Override
				public void run() {
					while(keepConnection) {
						TransferData newObject = null;
						try {
							newObject = receive();
						} catch (DisconnectedPublisherException e) {
							keepConnection = false;
							continue;
						}
						if(observers.containsKey(newObject.getClass())) {
							for(Observer observer: observers.get(newObject.getClass())) {
								observer.update(Connection.this, newObject);
							}
						}
					}
				}
			};
			newObject.start();
		}
		List<Observer> listObservers = observers.get(typeOfObjects);
		if(listObservers == null)
			listObservers = new ArrayList<>();
		listObservers.add(observer);
		observers.put(typeOfObjects, listObservers);
	}
	
	public abstract void disconnect();
	
	/**
	 * Check if this connection is still connected
	 * @return True if still active false otherwise
	 */
	public abstract boolean isConnected();
}
