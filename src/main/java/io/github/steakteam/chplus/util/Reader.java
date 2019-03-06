package io.github.steakteam.chplus.util;

/**
 * Created by JunHyeong Lim on 2019-03-07
 */
public class Reader<T> {
    private final T[] array;
    private int pos = 0;

    public Reader(T[] array) {
        this.array = array;
    }

    public boolean isEmpty() {
        return array.length <= pos;
    }

    public boolean isRemain() {
        return !isEmpty();
    }

    public T get(int index) {
        return array[index];
    }

    public T read() {
        return get(pos++);
    }

    public int size() {
        return array.length;
    }

    public int remain() {
        return array.length - pos;
    }
}
