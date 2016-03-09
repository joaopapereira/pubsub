package uk.co.jpereira.pubsub.connection;

import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.RegistrationPacket;
import uk.co.jpereira.pubsub.TransferData;

public interface Client {
	public TransferData read();
	public void registerWait(Observer object);
	public void registerFeed(RegistrationPacket packet);
}
