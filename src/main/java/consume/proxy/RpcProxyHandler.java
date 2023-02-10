package consume.proxy;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
 * It's a handler that will be used to read the response from the server and store it in a field
 *
 * @author zhaojiejun
 */
@ChannelHandler.Sharable
public class RpcProxyHandler extends ChannelInboundHandlerAdapter{

	private Object result;
	public Object getResult(){
		return this.result;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		this.result = msg;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}
	
}
