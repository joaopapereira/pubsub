package uk.co.jpereira.pubsub.connection.TCP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.RegistrationPacket;
import uk.co.jpereira.pubsub.TransferData;
import uk.co.jpereira.pubsub.connection.Client;

public class TCPClient implements Client {

	private static final Logger logger = LoggerFactory.getLogger(TCPClient.class);
	private TCPConnection connection = null;
	private TCPConfig config = null;
	public TCPClient(TCPConfig config) {
		this.config = config;
		connect();
		
	}
	public void connect() {
		logger.info("Going to connect to: " + config.getUrl() + "," + config.getPort());
		connection = new TCPConnection(config);

		logger.info("Connected");
	}
	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}
	@Override
	public void registerWait(Observer object) {
		// TODO Auto-generated method stub
		connection.registerReadObserver(TransferData.class, object);
	}

	@Override
	public void registerFeed(RegistrationPacket packet) {
		// TODO Auto-generated method stub
		connection.send(packet);
	}
	
	@Override
	public TransferData read() {
		// TODO Auto-generated method stub
		return connection.receive();
	}

}
