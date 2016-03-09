package uk.co.jpereira.pubsub.connection;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.TransferData;

/**
 * Class implemented by Servers
 * This is the minimum interface for the server used by the  
 * Publisher to send the information from the Subscriber
 * 
 * @author joaopapereira@gmail.com
 *
 */
public abstract class Server implements Observable {

	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	private boolean keepConnection = false;
	private List<Observer> newConnectionAsync = null;
	private Thread connectionWaiter = null;
	
	/**
	 * Update
	 * @param data
	 */
	public abstract void update(TransferData data);
	
	/**
	 * Blocking function used to wait for a new subscriber to join
	 * @return A new connection to the subscriber
	 */
	public abstract Connection waitForConnection();
	
	/**
	 * Non blocking function that will wait for a connection
	 * When a new connection arrives all the observer will be notified
	 * @param observer Observer to be notified
	 */
	public void registerWaitForConnection(Observer observer) {
		if(newConnectionAsync == null)
			newConnectionAsync = new ArrayList<>();
		newConnectionAsync.add(observer);
		if(connectionWaiter == null) {
			connectionWaiter = new Thread(){
	
				@Override
				public void run() {
					keepConnection = true;
					while(keepConnection) {
						Connection con = waitForConnection();
						if(con == null) {
							logger.error("Unable to retrieve a valid connection");
							continue;
						}
						for(Observer observer: newConnectionAsync)
							observer.update(Server.this, con);
						logger.info("Connection found and signaled " + newConnectionAsync);
					}
				}
			};
			connectionWaiter.start();
		}
	}

	/**
	 * Stop the async mode thread
	 */
	public void stop() {
		keepConnection = false;
	}
}
