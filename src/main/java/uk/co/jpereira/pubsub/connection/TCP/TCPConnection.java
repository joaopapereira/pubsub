package uk.co.jpereira.pubsub.connection.TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.observer.Observer;
import uk.co.jpereira.pubsub.TransferData;
import uk.co.jpereira.pubsub.connection.Connection;

public class TCPConnection implements Connection {
	private static final Logger logger = LoggerFactory.getLogger(TCPConnection.class);
	Socket connection = null;
	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;
	public Map<Class<?>, List<Observer>> observers;
	boolean keepConnection = true;
	public Thread newObject = null;
	
	public TCPConnection(Socket connection) {
		logger.info("TCPConnection(" + connection +")");
		this.connection = connection;
		try {
			logger.info("1");
			outputStream = new ObjectOutputStream(connection.getOutputStream());
			logger.info("2");
			inputStream = new ObjectInputStream(connection.getInputStream());
			logger.info("3");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		observers = new HashMap<>();
		logger.info("Done TCPConnection");
	}
	public TCPConnection(TCPConfig config) {
		logger.info("TCPConnection(" + connection +")");
		try {
			this.connection = new Socket(config.getUrl(), config.getPort());
			logger.info("1");
			inputStream = new ObjectInputStream(connection.getInputStream());
			logger.info("2");
			outputStream = new ObjectOutputStream(connection.getOutputStream());
			logger.info("3");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		observers = new HashMap<>();
		logger.info("Done TCPConnection");
	}

	@Override
	public void send(TransferData data) {
		try {
			outputStream.writeObject(data);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public TransferData receive() {
		TransferData data = null;
		try {
			data = (TransferData)inputStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public void registerObserverNewObject(Class<?> typeOfObjects, Observer observer) {
		// TODO Auto-generated method stub
		if(newObject == null) {
			newObject = new Thread(){
				@Override
				public void run() {
					while(keepConnection) {
						TransferData newObject = receive();
						if(observers.containsKey(newObject.getClass())) {
							for(Observer observer: observers.get(newObject.getClass())) {
								observer.update(TCPConnection.this, newObject);
							}
						}
					}
				}
			};
			newObject.start();
		}
		List<Observer> listObservers = observers.get(typeOfObjects);
		if(listObservers == null)
			listObservers = new ArrayList<>();
		listObservers.add(observer);
		observers.put(typeOfObjects, listObservers);
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return connection.isConnected();
	}
}
