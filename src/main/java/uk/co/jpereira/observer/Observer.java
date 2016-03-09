package uk.co.jpereira.observer;

/**
 * Interface used by the classes that will act as an Observer
 * for the Observable class.
 * The classes that implement this Interface will need to
 * Override the function called by the Observable class to do any action
 * @author jpereira
 *
 */
public interface Observer {
	/**
	 * Function called by the Observable Class
	 * Inside this class the Observer should execute any action needed
	 * This function implementation should be as small and quick as possible
	 * @param observable Observable class that trigger the  Observer
	 * @param obj Any object that be used to pass information between Observer and Observable
	 */
	public void update(Observable observable, Object obj);
}
