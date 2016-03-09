package uk.co.jpereira.pubsub.connection.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.pubsub.DisconnectedPublisherException;
import uk.co.jpereira.pubsub.TransferData;
import uk.co.jpereira.pubsub.connection.Connection;

/**
 * TCP Connection between the Server and the client.
 * In this class is stored the socket  that contains the connection
 * @author joaopapereira@gmail.com
 *
 */
public class TCPConnection extends Connection {
	private static final Logger logger = LoggerFactory.getLogger(TCPConnection.class);
	Socket connection = null;
	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;
	
	/**
	 * Class constructor(used by the server)
	 * This class constructor just retrieve the input and output stream
	 * @param connection Already established connection socket
	 */
	public TCPConnection(Socket connection) {
		logger.info("TCPConnection(" + connection +")");
		this.connection = connection;
		try {
			logger.info("Socket created retrieving input/output stream: " + connection);
			outputStream = new ObjectOutputStream(connection.getOutputStream());
			inputStream = new ObjectInputStream(connection.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		observers = new HashMap<>();
		logger.info("Done TCPConnection");
	}
	
	/**
	 * Class constructor(Used by the client)
	 * This constructor will create the socket connection using the 
	 * information provided in the configuration object
	 * @param config Configuration object
	 */
	public TCPConnection(TCPConfig config) {
		logger.info("TCPConnection(" + config +")");
		try {
			this.connection = new Socket(config.getUrl(), config.getPort());
			logger.info("Socket created retrieving input/output stream: " + connection);
			inputStream = new ObjectInputStream(connection.getInputStream());
			outputStream = new ObjectOutputStream(connection.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		observers = new HashMap<>();
		logger.info("Done TCPConnection");
	}

	@Override
	public void send(TransferData data) throws DisconnectedPublisherException{
		try {
			outputStream.writeObject(data);
			outputStream.flush();
		} catch (IOException e) {
			throw new DisconnectedPublisherException(connection.getRemoteSocketAddress().toString(), connection.getPort());
		}
	}

	@Override
	public TransferData receive() throws DisconnectedPublisherException{
		TransferData data = null;
		try {
			data = (TransferData)inputStream.readObject();
		} catch (ClassNotFoundException exp) {
			// TODO Auto-generated catch block
			exp.printStackTrace();
		} catch (IOException e) {
			throw new DisconnectedPublisherException(connection.getRemoteSocketAddress().toString(), connection.getPort());
		}
		return data;
	}
	
	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return connection.isConnected() && !connection.isClosed() && connection.isBound();
	}
	@Override
	public void disconnect() {
		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
