/**
 * 
 */
package base;

/**
 * @author hazel
 *
 */
public class Component {
	public enum serviceType{BLOCKING, INSPECTOR, WORKSTATION, BUFFER};
	private int id;
	private serviceType whichService;
	
	public Component(int id, serviceType whichService) {
		this.id = id;
		this.whichService = whichService;
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
