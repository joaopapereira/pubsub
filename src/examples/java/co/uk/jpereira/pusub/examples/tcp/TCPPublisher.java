package co.uk.jpereira.pusub.examples.tcp;

import java.io.IOException;

import uk.co.jpereira.pubsub.Publisher;
import uk.co.jpereira.pubsub.connection.tcp.TCPConfig;
import uk.co.jpereira.pubsub.connection.tcp.TCPServer;

public class TCPPublisher extends Publisher {
	TCPConfig config = null;
	public TCPPublisher() throws IOException {
		config = new TCPConfig();
		config.setPort(8888);
		config.setUrl("localhost");
		TCPServer server = new TCPServer(config);
		initServer(server);
	}
}
