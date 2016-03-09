package uk.co.jpereira.pubsub;

import java.util.ArrayList;
import java.util.List;


public class RegistrationPacket implements TransferData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6384537403895467342L;
	
	
	private List<String> allFeeds;
	public RegistrationPacket(String feed) {
		List<String> listOfFeeds = new ArrayList<>();
		listOfFeeds.add(feed);
		setAllFeeds(listOfFeeds);
	}
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
