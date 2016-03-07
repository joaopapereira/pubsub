package co.uk.jpereira.pusub.examples.tcp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		logger.info("Bamm");
		TCPPublisher publisher = new TCPPublisher();
		Thread.sleep(10);

		logger.info("Before subscriber");
		TCPSubscriber sub1 = new TCPSubscriber();
		sub1.subscribe("bamm");
		logger.info("before read");
		sub1.read();
		logger.info("after read");
		TCPSubscriber sub2 = new TCPSubscriber();
		
	}

}
