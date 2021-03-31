/**
 * This class is used to run the simulation and implement the logic behind it. It access different classes that represent
 * entities in the system.
 * @author Sonia Hassan-Legault, 101054542
 */
package base;

import java.util.*;

import static java.lang.Math.sqrt;

public class Model {
	private static int productCount;
	private static double clock, chosenTime, totalBlockedTimeI1, totalBlockedTimeI2, startBlockedTimeI1, startBlockedTimeI2;
	private static Queue<Event> FEL;
	private static ArrayList<Component> bufferC1W1, bufferC1W2, bufferC1W3, bufferC2W2, bufferC3W3;
	private static Component blockedI1Component, blockedI2Component;

	private static boolean isI1Busy, isI2Busy, isW1Busy, isW2Busy, isW3Busy, isI1Blocked, isI2Blocked;
	private static double blockedProportionI1, blockedProportionI2;
	private static Random randomNum;
	private static RandomNumberGenerator RNGC1, RNGC2, RNGC3, RNGW1, RNGW2, RNGW3;

	public static enum bufferType{BC1W1, BC1W2, BC1W3, BC2W2, BC3W3};
	/**
	 * Initialize all the variables to their initial states and prime the simulation (both inspectors start inspecting
	 * components).
	 */
	public static void initialize(int[] args) {
		int[] seeds = new int[6];
		for(int i = 0; i < args.length; i++) {
			seeds[i] = args[i];
			System.out.println(seeds[i]);
		}
		FEL = new PriorityQueue<Event>();
		clock=0.0;
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
		totalBlockedTimeI1=0.0;
		totalBlockedTimeI2=0.0;
		startBlockedTimeI1=0.0;
		startBlockedTimeI2=0.0;
		blockedProportionI1=0.0;
		blockedProportionI2=0.0;
		double a = 3000*sqrt(6003);
		RNGC1 = new RandomNumberGenerator(a,0,3001,seeds[0],0.09654);
		RNGC2 = new RandomNumberGenerator(a,0,3001,seeds[1],0.064363);
		RNGC3 = new RandomNumberGenerator(a,0,3001,seeds[2],0.048467);
		RNGW1 = new RandomNumberGenerator(a,0,3001,seeds[3],0.217182777);
		RNGW2 = new RandomNumberGenerator(a,0,3001,seeds[4],0.09015);
		RNGW3 = new RandomNumberGenerator(a,0,3001,seeds[5],0.113693469);
		isI1Busy=false;
		isI2Busy=false;
		randomNum = new Random();
	}

	/**
	 * Generate a random number variate based on the desired location (inspector or workstation
	 * @param location - use the location to determine which distribution to use
	 * @return - return a random number variate
	 */
	public static double getRandomTime(Event.eventLocation location, Component component){
		double time = -1.0;
		switch (location){
			case I1:
				time = RNGC1.generateRandomVariate();
				break;
			case I2:
				if(component.getId() == 2){
					time = RNGC2.generateRandomVariate();
				} else{
					time = RNGC3.generateRandomVariate();
				}
				break;
			case W1:
				time = RNGW1.generateRandomVariate();
				break;
			case W2:
				time = RNGW2.generateRandomVariate();
				break;
			case W3:
				time = RNGW3.generateRandomVariate();
				break;
		}
		return time;
	}

	/**
	 * Constructor to be used for testing
	 */
	public Model(int[] args){
		initialize(args);
	}

	/**
	 * Runs a while loops that moves the simulation forward. Calls the initialization method and the report generating
	 * methods.
	 * @param args - int[] representing the seeds for the Random Number Generators
	 */
	public static void runSimulation(int[] args) {
		Event nextEvent = null;
		initialize(args);
		//Create first Finish Inspection events for both inspectors (initial state of simulation)
		scheduleEvent(Event.eventType.FI, new Component(1, Component.serviceType.INSPECTOR), Event.eventLocation.I1);    //Inspector 1
		scheduleEvent(Event.eventType.FI, new Component(randomNum.nextInt(2)+2,
				Component.serviceType.INSPECTOR), Event.eventLocation.I2);   //Inspector 2
		isI1Busy=true;
		isI2Busy=true;

		while(!FEL.isEmpty() && (clock<chosenTime)){
			nextEvent = FEL.poll();
			System.out.println("Clock is: "+clock);
			System.out.println(nextEvent);
			if(nextEvent != null){
				clock=nextEvent.getTime();
				processEvent(nextEvent);
			}
		}
		getBlockedProportions();
		generateReport();
	}

	/**
	 * Calculate the proportion of time that the inspectors were blocked throughout the simulation.
	 */
	public static void getBlockedProportions() {
		if(startBlockedTimeI2 != 0.0){
			totalBlockedTimeI2 += clock - startBlockedTimeI2;
		}
		if(startBlockedTimeI1 != 0.0){
			totalBlockedTimeI1 += clock - startBlockedTimeI1;
		}
		blockedProportionI1 = totalBlockedTimeI1/chosenTime;
		blockedProportionI2 = totalBlockedTimeI2/chosenTime;
	}

	/**
	 * Figure out which type of event needs processing and call the appropriate function.
	 * @param nextEvent - the event that needs to be processed
	 */
	public static void processEvent(Event nextEvent) {
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
	public static void scheduleEvent(Event.eventType type, Component component, Event.eventLocation location){
		double time = 0.0;
		
		time = getRandomTime(location, component);

		if(location == Event.eventLocation.I1 || location == Event.eventLocation.I2){
			component.setWhichService(Component.serviceType.INSPECTOR);
		} else{
			component.setWhichService(Component.serviceType.WORKSTATION);
		}
		Event newEvent = new Event(type, time+clock, component, location);
		FEL.offer(newEvent);
	}

	public static void processEAEvent(Event event) {
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

	/**
	 * Process a Finish Inspection event. Finds which buffer the component should be added to and then calls a
	 * helper function to actually assign the component to the desired buffer. Schedules a new finish inspection event.
	 * Blocks the inspector if the buffer is full.
	 * @param event - The event that needs to be processed
	 */
	public static void processFIEvent(Event event) {
		bufferType buffer;
		Component c = event.getC();
		boolean componentAdded = false;
		int[] bufferSizes = new int[3];
		//Component 1
		if(c.getId() == 1){
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
					result = addToBuffer(bufferType.BC3W3, cI2);
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
	 * @param event event
	 */
	public static void checkToScheduleEAEvent(Event event) {
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
	 * @param buffer - the work station we want to add the component to
	 * @param c - the component to be added to the buffer
	 * @return true if the component was added to the buffer, false otherwise
	 */
	public static boolean addToBuffer(bufferType buffer, Component c){
		boolean componentAdded = false;
		switch(buffer){
			case BC1W1:
				if(bufferC1W1.size() < 2){
					componentAdded = bufferC1W1.add(c);
				}
				break;
			case BC1W2:
				if(bufferC1W2.size() < 2){
					componentAdded = bufferC1W2.add(c);
				}
				break;
			case BC1W3:
				if(bufferC1W3.size() < 2){
					componentAdded = bufferC1W3.add(c);
				}
				break;
			case BC2W2:
				if(bufferC2W2.size() < 2){
					componentAdded = bufferC2W2.add(c);
				}
				break;
			case BC3W3:
				if(bufferC3W3.size() < 2){
					componentAdded = bufferC3W3.add(c);
				}
				break;
		}
		return componentAdded;
	}

	private static void generateReport(){
		System.out.println("*** Final Report ***");
		System.out.println("Total product count: "+productCount);
		System.out.println("Total proportion Inspector 1 was blocked: "+blockedProportionI1);
		System.out.println("Total proportion Inspector 2 was blocked: "+blockedProportionI2);
	}

	public static int getProductCount() {
		return productCount;
	}

	public static void setProductCount(int productCount) {
		Model.productCount = productCount;
	}

	public static double getClock() {
		return clock;
	}

	public static void setClock(double clock) {
		Model.clock = clock;
	}

	public static double getChosenTime() {
		return chosenTime;
	}

	public static void setChosenTime(double chosenTime) {
		Model.chosenTime = chosenTime;
	}

	public static double getTotalBlockedTimeI1() {
		return totalBlockedTimeI1;
	}

	public static void setTotalBlockedTimeI1(double totalBlockedTimeI1) {
		Model.totalBlockedTimeI1 = totalBlockedTimeI1;
	}

	public static double getTotalBlockedTimeI2() {
		return totalBlockedTimeI2;
	}

	public static void setTotalBlockedTimeI2(double totalBlockedTimeI2) {
		Model.totalBlockedTimeI2 = totalBlockedTimeI2;
	}

	public static double getStartBlockedTimeI1() {
		return startBlockedTimeI1;
	}

	public static void setStartBlockedTimeI1(double startBlockedTimeI1) {
		Model.startBlockedTimeI1 = startBlockedTimeI1;
	}

	public static double getStartBlockedTimeI2() {
		return startBlockedTimeI2;
	}

	public static void setStartBlockedTimeI2(double startBlockedTimeI2) {
		Model.startBlockedTimeI2 = startBlockedTimeI2;
	}

	public static Queue<Event> getFEL() {
		return FEL;
	}

	public static void setFEL(Queue<Event> FEL) {
		Model.FEL = FEL;
	}

	public static ArrayList<Component> getBufferC1W1() {
		return bufferC1W1;
	}

	public static void setBufferC1W1(ArrayList<Component> bufferC1W1) {
		Model.bufferC1W1 = bufferC1W1;
	}

	public static ArrayList<Component> getBufferC1W2() {
		return bufferC1W2;
	}

	public static void setBufferC1W2(ArrayList<Component> bufferC1W2) {
		Model.bufferC1W2 = bufferC1W2;
	}

	public static ArrayList<Component> getBufferC1W3() {
		return bufferC1W3;
	}

	public static void setBufferC1W3(ArrayList<Component> bufferC1W3) {
		Model.bufferC1W3 = bufferC1W3;
	}

	public static ArrayList<Component> getBufferC2W2() {
		return bufferC2W2;
	}

	public static void setBufferC2W2(ArrayList<Component> bufferC2W2) {
		Model.bufferC2W2 = bufferC2W2;
	}

	public static ArrayList<Component> getBufferC3W3() {
		return bufferC3W3;
	}

	public static void setBufferC3W3(ArrayList<Component> bufferC3W3) {
		Model.bufferC3W3 = bufferC3W3;
	}

	public static Component getBlockedI1Component() {
		return blockedI1Component;
	}

	public static void setBlockedI1Component(Component blockedI1Component) {
		Model.blockedI1Component = blockedI1Component;
	}

	public static Component getBlockedI2Component() {
		return blockedI2Component;
	}

	public static void setBlockedI2Component(Component blockedI2Component) {
		Model.blockedI2Component = blockedI2Component;
	}

	public static boolean isIsI1Busy() {
		return isI1Busy;
	}

	public static void setIsI1Busy(boolean isI1Busy) {
		Model.isI1Busy = isI1Busy;
	}

	public static boolean isIsI2Busy() {
		return isI2Busy;
	}

	public static void setIsI2Busy(boolean isI2Busy) {
		Model.isI2Busy = isI2Busy;
	}

	public static boolean isIsW1Busy() {
		return isW1Busy;
	}

	public static void setIsW1Busy(boolean isW1Busy) {
		Model.isW1Busy = isW1Busy;
	}

	public static boolean isIsW2Busy() {
		return isW2Busy;
	}

	public static void setIsW2Busy(boolean isW2Busy) {
		Model.isW2Busy = isW2Busy;
	}

	public static boolean isIsW3Busy() {
		return isW3Busy;
	}

	public static void setIsW3Busy(boolean isW3Busy) {
		Model.isW3Busy = isW3Busy;
	}

	public static boolean isIsI1Blocked() {
		return isI1Blocked;
	}

	public static void setIsI1Blocked(boolean isI1Blocked) {
		Model.isI1Blocked = isI1Blocked;
	}

	public static boolean isIsI2Blocked() {
		return isI2Blocked;
	}

	public static void setIsI2Blocked(boolean isI2Blocked) {
		Model.isI2Blocked = isI2Blocked;
	}

	public static double getBlockedProportionI1() {
		return blockedProportionI1;
	}

	public static void setBlockedProportionI1(double blockedProportionI1) {
		Model.blockedProportionI1 = blockedProportionI1;
	}

	public static double getBlockedProportionI2() {
		return blockedProportionI2;
	}

	public static void setBlockedProportionI2(double blockedProportionI2) {
		Model.blockedProportionI2 = blockedProportionI2;
	}

	public static Random getRandomNum() {
		return randomNum;
	}

	public static void setRandomNum(Random randomNum) {
		Model.randomNum = randomNum;
	}

	public static RandomNumberGenerator getRNGC1() {
		return RNGC1;
	}

	public static void setRNGC1(RandomNumberGenerator RNGC1) {
		Model.RNGC1 = RNGC1;
	}

	public static RandomNumberGenerator getRNGC2() {
		return RNGC2;
	}

	public static void setRNGC2(RandomNumberGenerator RNGC2) {
		Model.RNGC2 = RNGC2;
	}

	public static RandomNumberGenerator getRNGC3() {
		return RNGC3;
	}

	public static void setRNGC3(RandomNumberGenerator RNGC3) {
		Model.RNGC3 = RNGC3;
	}

	public static RandomNumberGenerator getRNGW1() {
		return RNGW1;
	}

	public static void setRNGW1(RandomNumberGenerator RNGW1) {
		Model.RNGW1 = RNGW1;
	}

	public static RandomNumberGenerator getRNGW2() {
		return RNGW2;
	}

	public static void setRNGW2(RandomNumberGenerator RNGW2) {
		Model.RNGW2 = RNGW2;
	}

	public static RandomNumberGenerator getRNGW3() {
		return RNGW3;
	}

	public static void setRNGW3(RandomNumberGenerator RNGW3) {
		Model.RNGW3 = RNGW3;
	}
}

