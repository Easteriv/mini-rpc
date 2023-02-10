package api;



/**
 * Defining the interface for the RPC.
 * @author zhaojiejun
 */
public interface IRpcHello {

	/**
	 * Given a name, return a greeting for that name.
	 *
	 * @param name The name of the method.
	 * @return A string
	 */
	String hello(String name);
	
}
