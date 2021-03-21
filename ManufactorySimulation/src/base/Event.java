/**
 * 
 */
package base;

/**
 * @author hazel
 *
 */
public class Event implements Comparable<Event>{
	public enum eventType{SI, FI, SA, EA};
	private eventType eType;
	private int time;
	private Component c;
	
	public Event(eventType eType, int time, Component c) {
		this.eType = eType;
		this.time = time;
		this.c = c;
	}
	
	public int compareTo(Event e) {
		return this.getTime() - e.getTime();
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
	public int getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
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
	
	
}
