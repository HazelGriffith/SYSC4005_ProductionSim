/**
 * @author Sonia Hassan-Legault, 101054542
 */
package base;

import java.util.*;

public class Model {
	public static int clock, productCount, chosenTime, totalBlockedTimeI1, totalBlockedTimeI2, startBlockedTimeI1, startBlockedTimeI2;
	private static Queue<Event> FEL;
	private static ArrayList<Component> bufferC1W1, bufferC1W2, bufferC1W3, bufferC2W2, bufferC3W3;
	private static Component blockedI1Component, blockedI2Component;

	private static boolean isI1Busy, isI2Busy, isW1Busy, isW2Busy, isW3Busy, isI1Blocked, isI2Blocked;
	private static double blockedProportionI1, blockedProportionI2;
	private static Random randomNum;
	private static RandomNumberGenerator RNGI1, RNGI2, RNGW1, RNGW2, RNGW3;

	private static enum bufferType{BC1W1, BC1W2, BC1W3, BC2W2, BC3W3};
	/**
	 * Initialize all the variables to their initial states and prime the simulation (both inspectors start inspecting
	 * components).
	 */
	private static void initialize() {
		//Initialize the arrays and get the times from the files
		clock=0;
		productCount=0;
		chosenTime=60*12;
		bufferC1W1 = new ArrayList<>();
		bufferC1W2 = new ArrayList<>();
		bufferC1W3 = new ArrayList<>();
		bufferC2W2 = new ArrayList<>();
		bufferC3W3 = new ArrayList<>();
		isW1Busy=false;
		isW2Busy=false;
		isW3Busy=false;
		totalBlockedTimeI1=0;
		totalBlockedTimeI2=0;
		startBlockedTimeI1=0;
		startBlockedTimeI2=0;
		blockedProportionI1=0.0;
		blockedProportionI2=0.0;
		//TODO: Initialize RNGs

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
		switch (location){
			case I1:
				RNGI1.generateRandomVariate();
				break;
			case I2:
				RNGI2.generateRandomVariate();
				break;
			case W1:
				RNGW1.generateRandomVariate();
				break;
			case W2:
				RNGW2.generateRandomVariate();
				break;
			case W3:
				RNGW3.generateRandomVariate();
				break;
		}
		return 0;
	}

	public static void main(String[] args) {
		Event nextEvent = null;
		FEL = new PriorityQueue<Event>();
		initialize();

		while(!FEL.isEmpty() && (clock<chosenTime)){
			nextEvent = FEL.poll();
			System.out.println(nextEvent);
			if(nextEvent != null){
				clock=nextEvent.getTime();
				processEvent(nextEvent);
			}
		}
		getBlockedProportions();
	}

	/**
	 * Calculate the proportion of time that the inspectors were blocked throughout the simulation.
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
		int time = 0;
		
		time = getRandomTime(location);

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
		if(event.getLocation() == Event.eventLocation.W1){
			isW1Busy=false;
		} else if(event.getLocation() == Event.eventLocation.W2){
			isW2Busy=false;
		} else{
			isW3Busy=false;
		}
		checkToScheduleEAEvent(event);
	}

	/*private static void processSA(Event event) {
		
		
		
		//TODO: Check if inspector is blocked and unblock it if necessary.
		//TODO: Set component service type to Workstation
		//TODO: Set workstation busy boolean to true
	}*/

	/**
	 * Process a Finish Inspection event. Finds which buffer the component should be added to and then calls a
	 * helper function to actually assign the component to the desired buffer. Schedules a new finish inspection event.
	 * Blocks the inspector if the buffer is full.
	 * @param event - The event that needs to be processed
	 */
	private static void processFIEvent(Event event) {
		bufferType buffer;
		Component c = event.getC();
		boolean componentAdded = false;
		int[] bufferSizes = new int[3];
		//Component 1
		if(c.getId() == 1){
			/*bufferSizes[0] = bufferC1W1.size();
			bufferSizes[1] = bufferC1W2.size();
			bufferSizes[2] = bufferC1W3.size();
			//Default to Work station 1
			buffer = bufferType.BC1W1;
			//If W2 buffer is less than W1 buffer
			if(bufferSizes[1] < bufferSizes[0]){
				buffer = bufferType.BC1W2;
			}
			//If W3 buffer is less than W2 buffer
			if(bufferSizes[2] < bufferSizes[1]){
				buffer = bufferType.BC1W3;
			}*/
			componentAdded = selectC1Buffer(c);
		}
		//Component 2
		else if(c.getId() == 2){
			buffer = bufferType.BC2W2;
			componentAdded = addToBuffer(buffer,c);
		}
		//Component 3
		else{
			buffer = bufferType.BC3W3;
			componentAdded = addToBuffer(buffer,c);
		}
		if(componentAdded){
			c.setWhichService(Component.serviceType.BUFFER);
			if(c.getId() == 1){
				scheduleEvent(Event.eventType.FI, new Component(1, Component.serviceType.INSPECTOR), Event.eventLocation.I1);
			} else{
				scheduleEvent(Event.eventType.FI, new Component(randomNum.nextInt(2)+2,
						Component.serviceType.INSPECTOR), Event.eventLocation.I2);
			}
		} else{
			c.setWhichService(Component.serviceType.BLOCKING);
			if(c.getId() == 1){
				isI1Busy = false;
				isI1Blocked = true;
				blockedI1Component = c;
				startBlockedTimeI1 = clock;
			} else{
				isI2Busy = false;
				isI2Blocked = true;
				blockedI2Component = c;
				startBlockedTimeI2 = clock;
			}
		}
		checkToScheduleEAEvent(event);
	}
	
	/**
	 * selectC1Buffer selects the C1 buffer for the given component following the chosen policy.
	 * The addToBuffer function is then called to try and place the component in the buffer.
	 * If successful it returns true, false otherwise.
	 * 
	 * @return boolean
	 */
	public static boolean selectC1Buffer(Component c) {
		bufferType buffer;
		int[] bufferSizes = new int[3];
		bufferSizes[0] = bufferC1W1.size();
		bufferSizes[1] = bufferC1W2.size();
		bufferSizes[2] = bufferC1W3.size();
		//Default to Work station 1
		buffer = bufferType.BC1W1;
		//If W2 buffer is less than W1 buffer
		if(bufferSizes[1] < bufferSizes[0]){
			buffer = bufferType.BC1W2;
		}
		//If W3 buffer is less than W2 buffer
		if(bufferSizes[2] < bufferSizes[1]){
			buffer = bufferType.BC1W3;
		}
		return addToBuffer(buffer,c);
	}
	
	/**
	 * checkToUnblockInspectors function checks if each Inspector is blocked, and if so, checks to see if it can now unblock.
	 * If it can unblock, the component is added to the appropriate buffer, and statistics are taken of the time spent blocked.
	 */
	public static void checkToUnblockInspectors() {
		boolean result = false;
		if (isI1Blocked) {
			result = selectC1Buffer(blockedI1Component);
			if (result) {
				isI1Blocked = false;
				blockedI1Component = null;
				totalBlockedTimeI1 += clock - startBlockedTimeI1;
				startBlockedTimeI1 = 0;
				result = false;
				scheduleEvent(Event.eventType.FI, new Component(1, Component.serviceType.INSPECTOR), Event.eventLocation.I1);
			}
		}
		
		if (isI2Blocked) {
			Component cI2 = blockedI2Component;
			if (cI2.getId() == 2) {
				if (bufferC2W2.size() < 2) {
					result = addToBuffer(bufferType.BC2W2, cI2);
				}
			} else {
				if (bufferC3W3.size() < 2) {
					result = addToBuffer(bufferType.BC2W2, cI2);
				}
			}
			if (result) {
				isI2Blocked = false;
				blockedI2Component = null;
				totalBlockedTimeI2 += clock - startBlockedTimeI2;
				startBlockedTimeI2 = 0;
				result = false;
				scheduleEvent(Event.eventType.FI, new Component(randomNum.nextInt(2)+2, Component.serviceType.INSPECTOR), Event.eventLocation.I1);
			}
		}
	}
	
	/**
	 * checkToScheduleEAEvent function accepts the current FI or EA event, and checks if another EA event should be scheduled.
	 * It starts by calling the checkToUnblockInspectors() function to make sure all usable components are in buffers.
	 * Then if the associated workstation is not busy, and there are components available, the EA event is scheduled.
	 * It then ends by checking if any Inspectors should unblock after buffer space might have been made available.
	 * 
	 * @param Event event
	 */
	public static void checkToScheduleEAEvent(Event event) {
		Component c = event.getC();
		checkToUnblockInspectors();
		if (!isW1Busy) {
			if (!bufferC1W1.isEmpty()) {
				bufferC1W1.remove(0);
				scheduleEvent(Event.eventType.EA, event.getC(), Event.eventLocation.W1);
				isW1Busy = true;
			}
		} else if (!isW2Busy) {
			if ((!bufferC1W2.isEmpty())&&(!bufferC2W2.isEmpty())){
				bufferC1W2.remove(0);
				bufferC2W2.remove(0);
				scheduleEvent(Event.eventType.EA, event.getC(), Event.eventLocation.W2);
				isW2Busy = true;
			}
		} else if (!isW3Busy) {
			if ((!bufferC1W3.isEmpty())&&(!bufferC3W3.isEmpty())){
				bufferC1W3.remove(0);
				bufferC3W3.remove(0);
				scheduleEvent(Event.eventType.EA, event.getC(), Event.eventLocation.W3);
				isW3Busy = true;
			}
		}
		checkToUnblockInspectors();
	}

	/**
	 * Adds a component to the desired workstation buffer.
	 * @param workstation - the work station we want to add the component to
	 * @param c - the component to be added to the buffer
	 * @return true if the component was added to the buffer, false otherwise
	 */
	private static boolean addToBuffer(bufferType buffer, Component c){
		boolean componentAdded = false;
		switch(buffer){
			case BC1W1:
				if(bufferC1W1.size() < 2){
					componentAdded = bufferC1W1.add(c);
				}
			case BC1W2:
				if(bufferC1W2.size() < 2){
					componentAdded = bufferC1W2.add(c);
				}
			case BC1W3:
				if(bufferC1W3.size() < 2){
					componentAdded = bufferC1W3.add(c);
				}
			case BC2W2:
				if(bufferC2W2.size() < 2){
					componentAdded = bufferC2W2.add(c);
				}
			case BC3W3:
				if(bufferC3W3.size() < 2){
					componentAdded = bufferC3W3.add(c);
				}
		}
		return componentAdded;
	}

	//TODO: Implement and add other print statements
	public void generateReport(){

	}
}

