package co.uk.jpereira.pusub.examples.tcp;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.pubsub.DisconnectedPublisherException;
import uk.co.jpereira.pubsub.TransferData;

/**
 * Example of utilization of the TCP version of the pubsub
 * The publisher starts
 * 2 subscribers are created
 * The publisher publishes on object
 * Both the subscribers read it and print the information
 * Both subscribers try to read next information
 * Publisher is stopped
 * Both subscribers stop because no publisher is available 
 * @author joaopapereira@gmail.com
 *
 */
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) throws InterruptedException, DisconnectedPublisherException, IOException {
		// TODO Auto-generated method stub
		logger.info("Bamm");
		TCPPublisher publisher = new TCPPublisher();
		Thread.sleep(10);

		logger.info("Before subscriber");
		Application app = new Application();
		app.test();
		app.test();
		Thread.sleep(5000);
		logger.info("after subscriber");
		Information info = new Information();
		info.test = "jjjjj";
		publisher.publish("bamm", info);
		logger.info("after publish");
		Thread.sleep(2000);
		publisher.stop();
		//publisher = new TCPPublisher();
		//publisher.publish("bamm", info);
		
	}
	
	public static class Information implements TransferData{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6820227253669673236L;
		public String test;
	}
	
	public void test() {
		logger.info("111111111111111111111");
		new Thread() {
			@Override
			public void run() {

				logger.info("Start subscriber");
				TCPSubscriber sub1 = new TCPSubscriber();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.info("going to subscribe");
				try {
					sub1.subscribe("bamm");
				} catch (DisconnectedPublisherException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logger.info("Subscribed waiting");
				Information info;
				try {
					info = (Information)sub1.read();
					logger.info(info.test);
				} catch (DisconnectedPublisherException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					info = (Information)sub1.read();

					logger.info("Retrieved second");
				} catch (DisconnectedPublisherException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

}
