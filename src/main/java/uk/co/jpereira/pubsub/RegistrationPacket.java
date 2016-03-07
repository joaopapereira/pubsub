package uk.co.jpereira.pubsub;

import java.util.List;

import uk.co.jpereira.pubsub.connection.Connection;

public class RegistrationPacket implements TransferData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6384537403895467342L;
	
	
	private List<String> allFeeds;
	public RegistrationPacket(List<String> listFeeds) {
		setAllFeeds(listFeeds);
	}
	public List<String> getAllFeeds() {
		return allFeeds;
	}
	public void setAllFeeds(List<String> allFeeds) {
		this.allFeeds = allFeeds;
	}

}
