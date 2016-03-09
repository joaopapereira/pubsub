package uk.co.jpereira.pubsub.connection.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.pubsub.TransferData;
import uk.co.jpereira.pubsub.connection.Connection;
import uk.co.jpereira.pubsub.connection.Server;

/**
 * TCP implementation of the server
 * Uses ServerSocket to ensure the connections are correctly created
 * @author joaopapereira@gmail.com
 *
 */
public class TCPServer extends Server {
	private static final Logger logger = LoggerFactory.getLogger(TCPServer.class);
	
	private ServerSocket serverSocket = null;
	/**
	 * Class constructor
	 * Creates the server in the IP and port provided by the configuration
	 * 
	 * @param configuration Server configuration
	 * @throws IOException Throws if an error occur while creating the socket
	 */
	public TCPServer(TCPConfig configuration) throws IOException{
		InetAddress addr = InetAddress.getByName(configuration.getUrl());
		serverSocket = new ServerSocket(configuration.getPort(), 50, addr);
		logger.info("Socketserver: " + serverSocket);
	}
			
	@Override
	public void update(TransferData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection waitForConnection() {
		try {

			logger.info("Waiting for connections");
			if(serverSocket.isClosed())
				return null;
			Socket socket = serverSocket.accept();
			logger.info("New connection accepted: " + socket);
			return new TCPConnection(socket);
		} catch(SocketException exp) {
			logger.error("Error accepting new connections: " + exp);
			if(exp.getMessage().indexOf("closed") > 0)
				super.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void stop() {
		super.stop();
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
