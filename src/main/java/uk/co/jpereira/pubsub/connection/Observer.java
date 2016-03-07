package uk.co.jpereira.pubsub.connection;

public interface Observer{
	public void setChange(Object obj);
	public void update(Object observable, Object obj);
}
