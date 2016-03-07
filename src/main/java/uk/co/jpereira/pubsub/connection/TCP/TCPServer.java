package uk.co.jpereira.pubsub.connection.TCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.pubsub.TransferData;
import uk.co.jpereira.pubsub.connection.Connection;
import uk.co.jpereira.pubsub.connection.Observer;
import uk.co.jpereira.pubsub.connection.Server;

public class TCPServer implements Server {
	private static final Logger logger = LoggerFactory.getLogger(TCPServer.class);
	
	private ServerSocket serverSocket = null;
	private boolean keepConnection = false;
	private Observer newConnectionAsync = null;
	private Thread connectionWaiter = null;
	public TCPServer(TCPConfig configuration){
		try {
			logger.info("Creating socket at ");
			InetAddress addr = InetAddress.getByName(configuration.getUrl());
			serverSocket = new ServerSocket(configuration.getPort(), 50, addr);
			keepConnection = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
			
	@Override
	public void update(TransferData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection waitForConnection() {
		try {

			logger.info("Waiting for connections");
			Socket socket = serverSocket.accept();
			logger.info("New connection accepted: " + socket);
			return new TCPConnection(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void registerWaitForConnection(Observer object) {
		// TODO Auto-generated method stub
		newConnectionAsync = object;
		if(connectionWaiter == null) {
			connectionWaiter = new Thread(){
	
				@Override
				public void run() {
					while(keepConnection) {
						Connection con = waitForConnection();
						newConnectionAsync.update(this, con);

						logger.info("Connection found and signaled " + newConnectionAsync);
					}
				}
			};
			connectionWaiter.start();
		}
	}

	@Override
	public void stop() {
		try {
			keepConnection = false;
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
