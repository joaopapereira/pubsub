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

@SuiteClasses({PublisherWithMemoryTest.NewRegistration.class})
@RunWith(Suite.class)
public class PublisherWithMemoryTest {

	public static class PublisherWithMemoryImpl extends PublisherWithMemory {
		private Server myServer;
		public PublisherWithMemoryImpl(Server server) {
			super();
			myServer = server;
			initServer(server);
		}
		public PublisherWithMemoryImpl() {
			super();
			myServer = Mockito.mock(Server.class);
			initServer(myServer);
		}
		public Server getServer() {
			return myServer;
		}
	}
	@RunWith(MockitoJUnitRunner.class)
	public static class NewRegistration {
		private PublisherWithMemory.NewConnectionObserver observerNewCon;
		private PublisherWithMemory.ConnectionObserver observerConnection;

		@Mock
		private Connection con1;
		@Mock
		private Connection con2;
		@Mock
		private Connection con3;
		
		@Test
		public void test() throws DisconnectedPublisherException {
			Server server = Mockito.mock(Server.class);
			Mockito.doAnswer(new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					observerNewCon = invocation.getArgumentAt(0, PublisherWithMemory.NewConnectionObserver.class);
					return null;
				}
				
			}).doNothing().when(server).registerWaitForConnection(Mockito.any());
			Mockito.doAnswer(new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					observerConnection = invocation.getArgumentAt(1, PublisherWithMemory.ConnectionObserver.class);
					return null;
				}
				
			}).when(con1).registerReadObserver(Mockito.eq(RegistrationPacket.class), Mockito.any());
			PublisherWithMemoryImpl publisher = new PublisherWithMemoryImpl(server);

			TransferData data = Mockito.mock(TransferData.class);
			publisher.publish("test", data);
			observerNewCon.update(Mockito.mock(Observable.class), con1);
			Mockito.verify(con1, Mockito.times(1)).registerReadObserver(Mockito.eq(RegistrationPacket.class), Mockito.any());
			
			RegistrationPacket packet = new RegistrationPacket("test");
			observerConnection.update(con1, packet);
			Mockito.verify(con1, Mockito.times(1)).send(Mockito.eq(data));
			
		}
	}
}
