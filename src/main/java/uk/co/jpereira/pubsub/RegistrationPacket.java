package uk.co.jpereira.pubsub;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic class that implements the data transfer
 * This Packet will register the subscriber to a
 * specific feed
 * @author joaopapereira@gmail.com
 *
 */
public class RegistrationPacket implements TransferData {
	/**
	 * Class serial
	 */
	private static final long serialVersionUID = 6384537403895467342L;
	
	/**
	 * All feeds  that the subcriber want to register too
	 */
	private List<String> allFeeds;
	
	/**
	 * Class constructor
	 * @param feed Feed to register to
	 */
	public RegistrationPacket(String feed) {
		List<String> listOfFeeds = new ArrayList<>();
		listOfFeeds.add(feed);
		setAllFeeds(listOfFeeds);
	}
	
	/**
	 * Class constructor
	 * @param listFeeds List of feeds to register to
	 */
	public RegistrationPacket(List<String> listFeeds) {
		setAllFeeds(listFeeds);
	}
	
	/**
	 * Retrieve all the feeds
	 * @return List of feeds to register too
	 */
	public List<String> getAllFeeds() {
		return allFeeds;
	}
	
	/**
	 * Set the feeds  
	 * @param allFeeds List of feeds to register to
	 */
	public void setAllFeeds(List<String> allFeeds) {
		this.allFeeds = allFeeds;
	}
}
