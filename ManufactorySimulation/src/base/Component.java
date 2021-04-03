/**
 * 
 */
package base;

/**
 * The Component class represents the Components that travel through the simulation
 */
public class Component {
	public enum serviceType{BLOCKING, INSPECTOR, WORKSTATION, BUFFER};
	private int id;
	//This determines what kind of service the component is undergoing. Whether it is blocked at an Inspector, being inspected
	//Being used to assemble a product at a workstation, or in a buffer
	private serviceType whichService;
	
	/**
	 * The Component constructor creates Component objects with the given id int, and which serviceType it starts with
	 * @param int id of the Component {1, 2, or 3}
	 * @param serviceType whichService is the service that the Component is undergoing
	 */
	public Component(int id, serviceType whichService) {
		this.id = id;
		this.whichService = whichService;
	}
	
	/**
	 * The toString function writes the defining traits of the Component
	 */
	public String toString() {
		return ("C"+id+" with service: " + whichService);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the whichService
	 */
	public serviceType getWhichService() {
		return whichService;
	}

	/**
	 * @param whichService the whichService to set
	 */
	public void setWhichService(serviceType whichService) {
		this.whichService = whichService;
	}
}
