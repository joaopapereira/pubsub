package uk.co.jpereira.pubsub.connection.TCP;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.pubsub.RegistrationPacket;
import uk.co.jpereira.pubsub.TransferData;
import uk.co.jpereira.pubsub.connection.Client;
import uk.co.jpereira.pubsub.connection.Observer;

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
		try {
			Socket socket = new Socket(config.getUrl(), config.getPort());
			connection = new TCPConnection(socket);

			logger.info("Connected");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}
	@Override
	public void registerWait(Observer object) {
		// TODO Auto-generated method stub
		connection.registerObserverNewObject(TransferData.class, object);
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
