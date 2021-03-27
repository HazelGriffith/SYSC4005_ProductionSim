/**
 * @author Sonia Hassan-Legault, 101054542
 */
package base;

import java.util.*;

public class Model {
	public static int clock, productCount, chosenTime, totalBlockedTimeI1, totalBlockedTimeI2, startBlockedTimeI1, startBlockedTimeI2;
	private static Queue<Event> FEL;
	private static Component[][] buffers;

	private static boolean isI1Busy, isI2Busy;
	private static double blockedProportionI1, blockedProportionI2;
	private static Random randomNum;

	/**
	 * Initialize all the variables to their initial states and prime the simulation (both inspectors start inspecting
	 * components).
	 */
	private static void initialize() {
		//Initialize the arrays and get the times from the files
		clock=0;
		productCount=0;
		chosenTime=60*12;
		buffers = new Component[5][2]; //Order: [0]C1 for W1, [1]C1 for W2, [2]C1 for W3, [3]C2 for W2, [4]C3 for W3
		totalBlockedTimeI1=0;
		totalBlockedTimeI2=0;
		startBlockedTimeI1=0;
		startBlockedTimeI2=0;
		blockedProportionI1=0.0;
		blockedProportionI2=0.0;

		//Create first Finish Inspection events for both inspectors (initial state of simulation)
		scheduleEvent(Event.eventType.FI, new Component(1, Component.serviceType.INSPECTOR), Event.eventLocation.I1);    //Inspector 1
		randomNum = new Random();
		scheduleEvent(Event.eventType.FI, new Component(randomNum.nextInt(2)+2,
				Component.serviceType.INSPECTOR), Event.eventLocation.I2);   //Inspector 2
		isI1Busy=true;
		isI2Busy=true;
	}

	/**
	 * Generate a random number variate based on the desired location (inspector or workstation
	 * @param location - use the location to determine which distribution to use
	 * @return - return a random number variate
	 */
	private static int getRandomTime(Event.eventLocation location){
		return 0;
	}

	public static void main(String[] args) {
		Event nextEvent = null;
		FEL = new PriorityQueue<Event>();
		initialize();

		while(!FEL.isEmpty() && (clock<chosenTime)){
			nextEvent = FEL.poll();
			if(nextEvent != null){
				clock=nextEvent.getTime();
				processEvent(nextEvent);
			}
		}
		getBlockedProportions();
	}

	/**
	 * Calculate the proportion of time that the inspectors were idle throughout the simulation.
	 */
	private static void getBlockedProportions() {
		blockedProportionI1 = totalBlockedTimeI1/chosenTime;
		blockedProportionI2 = totalBlockedTimeI2/chosenTime;
	}

	/**
	 * Figure out which type of event needs processing and call the appropriate function.
	 * @param nextEvent - the event that needs to be processed
	 */
	private static void processEvent(Event nextEvent) {
		switch(nextEvent.geteType()){
			case FI:
				processFIEvent(nextEvent);
				break;
			case SA:
				processSAEvent(nextEvent);
				break;
			case EA:
				processEAEvent(nextEvent);
				break;
		}
	}

	/**
	 * Create a new event and add the event to the event queue.
	 * @param type - event type to be scheduled
	 * @param component - the component to be scheduled
	 * @param location - where in the flow is this event occurring (inspector or workstation)
	 */
	private static void scheduleEvent(Event.eventType type, Component component, Event.eventLocation location){
		int time = getRandomTime(location);
		if(location == Event.eventLocation.I1 || location == Event.eventLocation.I2){
			component.setWhichService(Component.serviceType.INSPECTOR);
		} else{
			component.setWhichService(Component.serviceType.WORKSTATION);
		}
		Event newEvent = new Event(type, time, component, location);
		FEL.offer(newEvent);
	}

	private static void processEAEvent(Event event) {
		productCount++;
	}

	private static void processSAEvent(Event event) {
		//TODO: Check if inspector is blocked and unblock it if necessary.
		//TODO: Make sure when taking an element out of buffer that it is taken from position 0. If the buffer was full
		// 		shift element in position1 to position 0 (that way the element is moved to the front of the buffer)
	}

	/**
	 * Process a Finish Inspection event. Finds which buffer the component should be added to and then calls a
	 * helper function to actually assign the component to the desired buffer.
	 * @param event - The event that needs to be processed
	 */
	private static void processFIEvent(Event event) {
		Component c = event.getC();
		int[] stations = new int[3];
		//Component 1
		if(c.getId() == 1){
			for(int i=0;i<3;i++){
				int count = -1;
				for(int j=0;j<2;j++){
					if(buffers[i][j] != null){
						count++;
					}
				}
				stations[i] = count;
			}
			//Default to Work station 1
			int temp = stations[0];
			//If W2 buffer is less than W1 buffer
			if(stations[1] < stations[0]){
				temp = stations[1];
			}
			//If W3 buffer is less than W2 buffer
			if(stations[2] < stations[1]){
				temp = stations[2];
			}
			addToBuffer(temp,c);
		}
		//Component 2
		else if(c.getId() == 2){
			addToBuffer(3,c);
		}
		//Component 3
		else{
			addToBuffer(4,c);
		}
	}

	/**
	 * Adds a component to the desired workstation buffer and schedules a new finish inspection event.
	 * Blocks the inspector if the buffer is full.
	 * @param workstation - the work station we want to add the component to
	 * @param c - the component to be added to the buffer
	 */
	private static void addToBuffer(int workstation, Component c){
		if(buffers[workstation][0] == null){
			buffers[workstation][0] = c;
			c.setWhichService(Component.serviceType.BUFFER);
			scheduleEvent(Event.eventType.FI, new Component(1, Component.serviceType.INSPECTOR), Event.eventLocation.I1);
		} else if(buffers[workstation][1] == null){
			buffers[workstation][1] = c;
			c.setWhichService(Component.serviceType.BUFFER);
			scheduleEvent(Event.eventType.FI, new Component(randomNum.nextInt(2)+2,
					Component.serviceType.INSPECTOR), Event.eventLocation.I2);
		} else{
			if(c.getId() == 1){
				isI1Busy = false;
				startBlockedTimeI1 = clock;
			} else{
				isI2Busy = false;
				startBlockedTimeI2 = clock;
			}
			c.setWhichService(Component.serviceType.BLOCKING);
		}
	}
}

