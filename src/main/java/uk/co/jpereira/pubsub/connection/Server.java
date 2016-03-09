package uk.co.jpereira.pubsub.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.TransferData;

public abstract class Server implements Observable {

	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	private boolean keepConnection = false;
	private Observer newConnectionAsync = null;
	private Thread connectionWaiter = null;
	
	public abstract void update(TransferData data);
	public abstract Connection waitForConnection();
	
	public void registerWaitForConnection(Observer object) {
		// TODO Auto-generated method stub
		newConnectionAsync = object;
		if(connectionWaiter == null) {
			connectionWaiter = new Thread(){
	
				@Override
				public void run() {
					keepConnection = true;
					while(keepConnection) {
						Connection con = waitForConnection();
						newConnectionAsync.update(Server.this, con);

						logger.info("Connection found and signaled " + newConnectionAsync);
					}
				}
			};
			connectionWaiter.start();
		}
	}

	public void stop() {
		keepConnection = false;
	}
}
