package kr.rvs.chplus.util;

/**
 * Created by Junhyeong Lim on 2017-07-06.
 */
public class Storage<T> {
    private T handle;

    public Storage(T handle) {
        this.handle = handle;
    }

    public T getHandle() {
        return handle;
    }

    public void setHandle(T handle) {
        this.handle = handle;
    }
}
