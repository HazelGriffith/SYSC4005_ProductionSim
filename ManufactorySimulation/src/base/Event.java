/**
 * 
 */
package base;

/**
 * The Event class represents Finish inspection and end assembly events in the simulation
 *
 */
public class Event implements Comparable<Event>{

	//Events that are either Finish Inspection, or End Assembly
	public enum eventType{FI, EA}
	private eventType eType;
	
	//The time the event takes place
	private double time;
	
	//The associated component
	private Component c;
	
	//The location of where the event is associated with
	public enum eventLocation{I1, I2, W1, W2, W3}
	private eventLocation location;
	
	/**
	 * The Event constructor creates Event objects with the given event type, time occurring, associated component, and location
	 * @param eType
	 * @param time
	 * @param c
	 * @param location
	 */
	public Event(eventType eType, double time, Component c, eventLocation location) {
		this.eType = eType;
		this.time = time;
		this.c = c;
		this.location = location;
	}
	
	/**
	 * The cmpareTo function compares the times in seconds of two Events for sorting in the FEL priorityQueue
	 */
	public int compareTo(Event e) {
		int current = (int)(this.getTime()*60);
		int event = (int)(e.getTime()*60);
		return current-event;
	}
	
	/**
	 * The toString function returns the defining traits of the Event object
	 */
	public String toString() {
		return ("event type is: " + eType + " Time of Event is: " + time + " Component is: " + c + " Event location is: " + location);
	}

	/**
	 * @return the eType
	 */
	public eventType geteType() {
		return eType;
	}

	/**
	 * @param eType the eType to set
	 */
	public void seteType(eventType eType) {
		this.eType = eType;
	}

	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(double time) {
		this.time = time;
	}

	/**
	 * @return the c
	 */
	public Component getC() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(Component c) {
		this.c = c;
	}

	public eventLocation getLocation() {
		return location;
	}

	public void setLocation(eventLocation location) {
		this.location = location;
	}
}
