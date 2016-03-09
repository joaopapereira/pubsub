package uk.co.jpereira.pubsub.connection.tcp;

/**
 * Class that contains the needed configuration to server and client
 * to connect with each other
 * @author jpereira
 *
 */
public class TCPConfig {
	private String url;
	private int port;
	
	/**
	 * Retrieve the URL
	 * @return URL of the publisher
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Set the URL of the server
	 * @param url URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Retrieve the port of the server
	 * @return Port of the server
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Set the port of the server
	 * @param port Port of the server
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
