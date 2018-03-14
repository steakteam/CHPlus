package kr.rvs.chplus.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Junhyeong Lim on 2017-07-05.
 */
public class ContainerMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        return (methodName.equals("a") || methodName.equals("canUse"))
                && args.length == 1 && method.getParameterTypes()[0].getSimpleName().equals("EntityHuman")

                ? isReachable() : methodProxy.invokeSuper(o, args);
    }

    private boolean isReachable() {
        return true;
    }
}
