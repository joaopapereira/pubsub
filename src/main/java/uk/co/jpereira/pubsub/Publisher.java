package uk.co.jpereira.pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.pubsub.connection.Connection;
import uk.co.jpereira.pubsub.connection.Observer;
import uk.co.jpereira.pubsub.connection.Server;

public abstract class Publisher {
	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	private Server server = null;
	private Map<String, List<Connection>> allConnections;
	public Publisher() {
		allConnections = new HashMap<>();
	}
	
	public void publish(String location, TransferData data){
		if(allConnections.containsKey(location)) {
			for(Connection con: allConnections.get(location)) {
				con.send(data);
			}
		}
	}
	protected void initServer(Server server){

		logger.info("Init server" );
		this.server = server;
		this.server.registerWaitForConnection(new NewConnectionObserver());
	}
	
	protected class NewConnectionObserver implements Observer {
		Connection con = null;
		@Override
		public void setChange(Object obj) {
			// TODO Auto-generated method stub
			con = (Connection) obj;
		}

		@Override
		public synchronized void update(Object observable, Object obj) {
			con = (Connection) obj;
			logger.debug("New connection stablished: " + con);
			con.registerObserverNewObject(RegistrationPacket.class, new ConnectionObserver());
		}
	}
	protected class ConnectionObserver implements Observer {
		RegistrationPacket packet = null;
		@Override
		public void setChange(Object obj) {
			packet = (RegistrationPacket)obj;
		}

		@Override
		public synchronized void update(Object observable, Object obj) {
			packet = (RegistrationPacket)obj;
			Connection con = (Connection)observable;
			for(String feed: packet.getAllFeeds()) {
				List<Connection> connection = allConnections.get(feed);
				if(connection == null)
					connection = new ArrayList<>();
				connection.add(con);
			}
		}
	}
}
