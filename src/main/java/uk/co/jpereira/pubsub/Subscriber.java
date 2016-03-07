package uk.co.jpereira.pubsub;

import java.util.ArrayList;
import java.util.List;

import uk.co.jpereira.pubsub.connection.Client;

public class Subscriber {
	private Client client;
	public Subscriber() {
	}
	
	public void subscribe(String feed) {
		List<String> allFeeds = new ArrayList<>();
		allFeeds.add(feed);
		RegistrationPacket packet = new RegistrationPacket(allFeeds);
		client.registerFeed(packet);
	}
	public TransferData read() {
		return client.read();
	}
	protected void initClient(Client client) {
		this.client = client;
	}
}
