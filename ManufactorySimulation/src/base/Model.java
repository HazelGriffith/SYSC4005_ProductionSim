/**
 * 
 */
package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Model {

	private static Stack<String> component1InspectionTimes,component2InspectionTimes,component3InspectionTimes,
			workstation1Times,workstation2Times,workstation3Times;

	public static int clock, productCount, chosenTime;
	private static Queue<Event> FEL;
	private static Component[][] buffers;

	private static boolean isI1Busy, isI2Busy, isW1Busy, isW2Busy, isW3Busy;
	private static double blockedTimeI1, blockedTimeI2, blockedProportionI1, blockedProportionI2;

	private static void initialize() throws FileNotFoundException {
		//Initialize the arrays and get the times from the files
		component1InspectionTimes = new Stack<String>();
		component1InspectionTimes = getTimes("servinsp1.dat");
		component2InspectionTimes = new Stack<String>();
		component2InspectionTimes = getTimes("servinsp22.dat");
		component3InspectionTimes = new Stack<String>();
		component3InspectionTimes = getTimes("servinsp23.dat");
		workstation1Times = new Stack<String>();
		workstation1Times = getTimes("ws1.dat");
		workstation2Times = new Stack<String>();
		workstation2Times = getTimes("ws2.dat");
		workstation3Times = new Stack<String>();
		workstation3Times = getTimes("ws3.dat");

		clock=0;
		productCount=0;
		chosenTime=60*12;
		buffers = new Component[5][2]; //Order: [0]C1 for W1, [1]C1 for W2, [2]C1 for W3, [3]C2 for W2, [4]C3 for W3
		isI1Busy=false;
		isI2Busy=false;
		isW1Busy=false;
		isW2Busy=false;
		isW3Busy=false;
		blockedTimeI1=0.0;
		blockedTimeI2=0.0;
		blockedProportionI1=0.0;
		blockedProportionI2=0.0;

		//Create first F1 events for both inspectors (initial state of simulation)
		scheduleEvent(FI, new Component(1));    //Inspector 1
		Random r = new Random();
		scheduleEvent(FI, new Component(r.nextInt(2)+2));   //Inspector 2

	}

	private static Stack<String> getTimes(String fileName){
		Stack<String> a = new Stack<String>();
		File f = new File(fileName);
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(s.hasNext()){
			String l = s.nextLine();
			//System.out.println(l);
			a.push(l);
		}
		System.out.println(a.size());
		return a;
	}

	public static void main(String[] args) throws FileNotFoundException {
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
	}

	private static void processEvent(Event nextEvent) {
		switch(nextEvent.getType()){
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

	//TODO: Figure out logic for scheduling SA events
	private static void scheduleEvent(Event.eventType type, Component component){
		int time=-1;
		switch(component.getType()){
			case 1:
				switch (type){
					case FI:
						time = clock+Integer.parseInt(component1InspectionTimes.pop());
						break;
					case SA:
						//time = clock+Integer.parseInt(component1InspectionTimes.pop());
					case EA:
						time = clock+Integer.parseInt(component1InspectionTimes.pop());
				}

				break;
			case 2:
				time = clock+Integer.parseInt(component2InspectionTimes.pop());
				break;
			case 3:
				time = clock+Integer.parseInt(component3InspectionTimes.pop());
				break;
		}
		Event newEvent = new Event(type, time, new Component(component.getId()));
		FEL.offer(newEvent);
	}

	private static void processEAEvent(Event event) {
		Component c = event.getComponent();
	}

	private static void processSAEvent(Event event) {
	}

	private static void processFIEvent(Event event) {
		Component c = event.getComponent();
		//Component 1
		if(c.getId() == 1){
			int max = -1;
			for(int i=0;i<3;i++){
				for(int j=0;j<2;j++){
					if(buffers[i][j] != null){

					}
				}

			}

		}
		//Component 2
		else if(c.getId == 2){

		}
		//Component 3
		else{

		}

	}

	public int getClock() {
		return clock;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public Queue<Event> getFEL() {
		return FEL;
	}

	public void setFEL(Queue<Event> FEL) {
		this.FEL = FEL;
	}

	public Component[][] getBuffers() {
		return buffers;
	}

	public void setBuffers(Component[][] buffers) {
		this.buffers = buffers;
	}

	public boolean isI1Busy() {
		return isI1Busy;
	}

	public void setI1Busy(boolean i1Busy) {
		isI1Busy = i1Busy;
	}

	public boolean isI2Busy() {
		return isI2Busy;
	}

	public void setI2Busy(boolean i2Busy) {
		isI2Busy = i2Busy;
	}

	public boolean isW1Busy() {
		return isW1Busy;
	}

	public void setW1Busy(boolean w1Busy) {
		isW1Busy = w1Busy;
	}

	public boolean isW2Busy() {
		return isW2Busy;
	}

	public void setW2Busy(boolean w2Busy) {
		isW2Busy = w2Busy;
	}

	public boolean isW3Busy() {
		return isW3Busy;
	}

	public void setW3Busy(boolean w3Busy) {
		isW3Busy = w3Busy;
	}

	public double getBlockedTimeI1() {
		return blockedTimeI1;
	}

	public void setBlockedTimeI1(double blockedTimeI1) {
		this.blockedTimeI1 = blockedTimeI1;
	}

	public double getBlockedTimeI2() {
		return blockedTimeI2;
	}

	public void setBlockedTimeI2(double blockedTimeI2) {
		this.blockedTimeI2 = blockedTimeI2;
	}

	public double getBlockedProportionI1() {
		return blockedProportionI1;
	}

	public void setBlockedProportionI1(double blockedProportionI1) {
		this.blockedProportionI1 = blockedProportionI1;
	}

	public double getBlockedProportionI2() {
		return blockedProportionI2;
	}

	public void setBlockedProportionI2(double blockedProportionI2) {
		this.blockedProportionI2 = blockedProportionI2;
	}

	public static Stack<String> getComponent1InspectionTimes() {
		return component1InspectionTimes;
	}

	public static void setComponent1InspectionTimes(Stack<String> component1InspectionTimes) {
		Model.component1InspectionTimes = component1InspectionTimes;
	}

	public static Stack<String> getComponent2InspectionTimes() {
		return component2InspectionTimes;
	}

	public static void setComponent2InspectionTimes(Stack<String> component2InspectionTimes) {
		Model.component2InspectionTimes = component2InspectionTimes;
	}

	public static Stack<String> getComponent3InspectionTimes() {
		return component3InspectionTimes;
	}

	public static void setComponent3InspectionTimes(Stack<String> component3InspectionTimes) {
		Model.component3InspectionTimes = component3InspectionTimes;
	}

	public static Stack<String> getWorkstation1Times() {
		return workstation1Times;
	}

	public static void setWorkstation1Times(Stack<String> workstation1Times) {
		Model.workstation1Times = workstation1Times;
	}

	public static Stack<String> getWorkstation2Times() {
		return workstation2Times;
	}

	public static void setWorkstation2Times(Stack<String> workstation2Times) {
		Model.workstation2Times = workstation2Times;
	}

	public static Stack<String> getWorkstation3Times() {
		return workstation3Times;
	}

	public static void setWorkstation3Times(Stack<String> workstation3Times) {
		Model.workstation3Times = workstation3Times;
	}
}

