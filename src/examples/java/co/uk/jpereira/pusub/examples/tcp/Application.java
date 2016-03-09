package co.uk.jpereira.pusub.examples.tcp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jpereira.pubsub.TransferData;

public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) throws InterruptedException {
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
				sub1.subscribe("bamm");
				logger.info("Subscribed waiting");
				Information info = (Information)sub1.read();
				logger.info(info.test);
			}
		}.start();
	}

}
