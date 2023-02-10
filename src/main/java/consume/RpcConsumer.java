package consume;


import api.IRpcHello;
import consume.proxy.RpcProxy;

public class RpcConsumer {
	
	public static void main(String[] args) {
		
		//本机一个人在玩
		//自娱自乐
		//肯定是用动态代理来实现的,传给它一个接口，返回一个实例，伪代理
		IRpcHello rpcHello = RpcProxy.create(IRpcHello.class);
		String r = rpcHello.hello("Tom老师");
		System.out.println(r);
		
		
	}
	
}
