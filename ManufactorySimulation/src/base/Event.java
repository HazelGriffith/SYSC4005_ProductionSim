/**
 * 
 */
package base;

/**
 * @author hazel
 *
 */
public class Event implements Comparable<Event>{

	public enum eventType{FI, EA}
	private eventType eType;
	private double time;
	private Component c;
	public enum eventLocation{I1, I2, W1, W2, W3}
	private eventLocation location;
	
	public Event(eventType eType, double time, Component c, eventLocation location) {
		this.eType = eType;
		this.time = time;
		this.c = c;
		this.location = location;
	}
	
	public int compareTo(Event e) {
		int current = (int)(this.getTime()*60);
		int event = (int)(e.getTime()*60);
		return current-event;
	}
	
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
