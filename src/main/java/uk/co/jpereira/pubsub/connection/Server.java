package uk.co.jpereira.pubsub.connection;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.TransferData;

public interface Server extends Observable {
	public void update(TransferData data);
	public Connection waitForConnection();
	public void registerWaitForConnection(Observer object);
	public void stop();
}
