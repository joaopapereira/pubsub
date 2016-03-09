package uk.co.jpereira.pubsub.connection;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.TransferData;

public interface Connection extends Observable {
	public void send(TransferData data);
	public TransferData receive();
	public void registerObserverNewObject(Class<?> typeOfObjects, Observer observer);
	public boolean isConnected();
}
