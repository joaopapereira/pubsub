package uk.co.jpereira.pubsub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import uk.co.jpereira.observer.Observable;
import uk.co.jpereira.pubsub.connection.Connection;
import uk.co.jpereira.pubsub.connection.Server;

@SuiteClasses({PublisherTest.PublishFunction.class,
				PublisherTest.NewConnection.class,
				PublisherTest.NewRegistration.class})
@RunWith(Suite.class)
public class PublisherTest {
	public static class PublisherImpl extends Publisher {
		private Server myServer;
		public PublisherImpl(Server server) {
			super();
			myServer = server;
			initServer(server);
		}
		public PublisherImpl() {
			super();
			myServer = Mockito.mock(Server.class);
			initServer(myServer);
		}
		public Server getServer() {
			return myServer;
		}
	}
	
	@RunWith(MockitoJUnitRunner.class)
	public static class PublishFunction {
		@Mock
		private Connection con1;
		@Mock
		private Connection con2;
		@Mock
		private Connection con3;
		
		@Test
		public void noSubscribers() {
			PublisherImpl publisher = new PublisherImpl();
			publisher.publish("test", Mockito.mock(TransferData.class));
		}
		@Test
		public void noSubscribersForPublished() {
			PublisherImpl publisher = new PublisherImpl();
			
			publisher.addConnection("test1", con1);
			publisher.addConnection("test2", con2);
			publisher.addConnection("test2", con3);
			TransferData data = Mockito.mock(TransferData.class);
			publisher.publish("test", data);
			Mockito.verify(con1, Mockito.times(0)).send(data);
			Mockito.verify(con2, Mockito.times(0)).send(data);
			Mockito.verify(con3, Mockito.times(0)).send(data);
		}
		@Test
		public void oneSubscriber() {
			PublisherImpl publisher = new PublisherImpl();
			
			publisher.addConnection("test", con1);
			publisher.addConnection("test2", con2);
			publisher.addConnection("test2", con3);
			TransferData data = Mockito.mock(TransferData.class);
			publisher.publish("test", data);
			Mockito.verify(con1, Mockito.times(1)).send(data);
			Mockito.verify(con2, Mockito.times(0)).send(data);
			Mockito.verify(con3, Mockito.times(0)).send(data);
		}
		@Test
		public void twoSubscriber() {
			PublisherImpl publisher = new PublisherImpl();
			
			publisher.addConnection("test", con1);
			publisher.addConnection("test2", con2);
			publisher.addConnection("test", con3);
			TransferData data = Mockito.mock(TransferData.class);
			publisher.publish("test", data);
			Mockito.verify(con1, Mockito.times(1)).send(data);
			Mockito.verify(con2, Mockito.times(0)).send(data);
			Mockito.verify(con3, Mockito.times(1)).send(data);
		}
	}
	@RunWith(MockitoJUnitRunner.class)
	public static class NewConnection {
		private Publisher.NewConnectionObserver observer;

		@Mock
		private Connection con1;
		@Mock
		private Connection con2;
		@Mock
		private Connection con3;
		
		@Test
		public void test() {
			Server server = Mockito.mock(Server.class);
			Mockito.doAnswer(new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					observer = invocation.getArgumentAt(0, Publisher.NewConnectionObserver.class);
					return null;
				}
				
			}).when(server).registerWaitForConnection(Mockito.any());
			new PublisherImpl(server);
			observer.update(Mockito.mock(Observable.class), con1);
			Mockito.verify(con1, Mockito.times(1)).registerReadObserver(Mockito.eq(RegistrationPacket.class), Mockito.any());
		}
	}
	
	@RunWith(MockitoJUnitRunner.class)
	public static class NewRegistration {
		private Publisher.NewConnectionObserver observerNewCon;
		private Publisher.ConnectionObserver observerConnection;

		@Mock
		private Connection con1;
		@Mock
		private Connection con2;
		@Mock
		private Connection con3;
		
		@Test
		public void test() {
			Server server = Mockito.mock(Server.class);
			Mockito.doAnswer(new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					observerNewCon = invocation.getArgumentAt(0, Publisher.NewConnectionObserver.class);
					return null;
				}
				
			}).when(server).registerWaitForConnection(Mockito.any());
			Mockito.doAnswer(new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					observerConnection = invocation.getArgumentAt(1, Publisher.ConnectionObserver.class);
					return null;
				}
				
			}).when(con1).registerReadObserver(Mockito.eq(RegistrationPacket.class), Mockito.any());
			PublisherImpl publisher = new PublisherImpl(server);
			observerNewCon.update(Mockito.mock(Observable.class), con1);
			Mockito.verify(con1, Mockito.times(1)).registerReadObserver(Mockito.eq(RegistrationPacket.class), Mockito.any());
			
			RegistrationPacket packet = new RegistrationPacket("test");
			observerConnection.update(con1, packet);
			TransferData data = Mockito.mock(TransferData.class);
			Mockito.verify(con1, Mockito.times(0)).send(Mockito.any());
			publisher.publish("test", data);
			Mockito.verify(con1, Mockito.times(1)).send(Mockito.eq(data));
			
		}
	}
}
