package co.uk.jpereira.pusub.examples.tcp;

import uk.co.jpereira.pubsub.Publisher;
import uk.co.jpereira.pubsub.connection.TCP.TCPConfig;
import uk.co.jpereira.pubsub.connection.TCP.TCPServer;

public class TCPPublisher extends Publisher {
	TCPConfig config = null;
	public TCPPublisher() {
		config = new TCPConfig();
		config.setPort(8888);
		config.setUrl("localhost");
		TCPServer server = new TCPServer(config);
		initServer(server);
	}
}
