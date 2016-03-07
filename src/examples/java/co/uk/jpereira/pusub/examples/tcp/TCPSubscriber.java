package co.uk.jpereira.pusub.examples.tcp;

import uk.co.jpereira.pubsub.Subscriber;
import uk.co.jpereira.pubsub.connection.TCP.TCPClient;
import uk.co.jpereira.pubsub.connection.TCP.TCPConfig;

public class TCPSubscriber extends Subscriber{

	TCPConfig config = null;
	public TCPSubscriber() {
		config = new TCPConfig();
		config.setPort(8888);
		config.setUrl("localhost");
		TCPClient client = new TCPClient(config);
		initClient(client);
	}
}
