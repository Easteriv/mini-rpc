package handler;

import core.InvokerMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaojiejun
 * @date 2022/12/17 16:38
 **/
@ChannelHandler.Sharable
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    /**
     * 在注册中心注册的服务需要有一个容器存放
     */
    public static ConcurrentHashMap<String, Object> REGISTRY_MAP = new ConcurrentHashMap<String, Object>();

    private List<String> classCache = new ArrayList<String>();

    /**
     * 约定，只要写在provider包下面的所有类都认为是一个可以对外提供服务的实现类
     */
    public RegistryHandler(){
        scanClass("provide");
        doRegister();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Object result = new Object();

        //客户端传过来的调用信息
        InvokerMsg request = (InvokerMsg)msg;
        if(REGISTRY_MAP.containsKey(request.getClassName())){
            Object clazz = REGISTRY_MAP.get(request.getClassName());

            Method m = clazz.getClass().getMethod(request.getMethodName(), request.getParams());
            result = m.invoke(clazz, request.getValues());
        }

        ctx.writeAndFlush(result);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * IOC容器大概写一下,扫描出所有的Class
     * @param packageName 包名称
     */
    private  void scanClass(String packageName){
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        assert url != null;
        File dir = new File(url.getFile());
        for ( File file : Objects.requireNonNull(dir.listFiles())) {
            if(file.isDirectory()){
                scanClass(packageName + "." + file.getName());
            }else{
                classCache.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }


    //把扫描到class实例化，放到map中，这就是注册过程
    //注册的服务名字，叫接口名字
    //约定优于配置
    private void doRegister(){
        if(classCache.size() == 0){ return;}

        for (String className : classCache) {
            try {
                Class<?> clazz = Class.forName(className);
                //服务名称
                Class<?> interfaces = clazz.getInterfaces()[0];

                REGISTRY_MAP.put(interfaces.getName(), clazz.newInstance());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
