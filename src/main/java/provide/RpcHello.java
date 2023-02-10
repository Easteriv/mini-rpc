package provide;


import api.IRpcHello;

/**
 * @author zhaojiejun
 */
public class RpcHello implements IRpcHello {

	@Override
	public String hello(String name) {
		return "Hello , " + name + "!";
	}

}
