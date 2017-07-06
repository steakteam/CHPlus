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
        Object ret;
        if (method.getName().equals("a") && args.length == 1
                && method.getParameterTypes()[0].getSimpleName().equals("EntityHuman")) {
            ret = isReachable();
        } else {
            ret = methodProxy.invokeSuper(o, args);
        }

        return ret;
    }

    private boolean isReachable() {
        return true;
    }
}
