package uk.co.jpereira.pubsub.connection;

import uk.co.jpereira.pubsub.TransferData;

public interface Connection {
	public void send(TransferData data);
	public TransferData receive();
	public void registerObserverNewObject(Class typeOfObjects, Observer observer);
	public boolean isConnected();
}
