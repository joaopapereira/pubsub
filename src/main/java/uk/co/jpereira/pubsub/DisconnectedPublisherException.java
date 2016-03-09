package uk.co.jpereira.pubsub;

/**
 * Exception used when the subscriber lose the connection to the publisher
 * @author joaopapereira@gmail.com
 *
 */
public class DisconnectedPublisherException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8032985219720669697L;

	String url;
	int port;
	
	public DisconnectedPublisherException(String url, int port) {
		super();
		this.url = url;
		this.port = port;
	}
	
	@Override
	public String getMessage() {
		return "Publisher in " + url + ":" + port + " is not longer connected";
	}
}
