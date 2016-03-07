package uk.co.jpereira.pubsub.connection;

import uk.co.jpereira.pubsub.TransferData;

public interface Server {
	public void update(TransferData data);
	public Connection waitForConnection();
	public void registerWaitForConnection(Observer object);
	public void stop();
}
