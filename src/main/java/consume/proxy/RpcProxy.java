package consume.proxy;

import java.lang.reflect.Proxy;

/**
 * It creates a proxy object for the given interface, and when the proxy object is called, it will call the MethodProxy
 * class to execute the method
 *
 * @author zhaojiejun
 */
public class RpcProxy {
	public static <T> T create(Class<T> clazz){
		MethodProxy methodProxy = new MethodProxy(clazz);
		return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, methodProxy);
	}
}