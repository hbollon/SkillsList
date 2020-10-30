package com.bitsplease.skillslist_server.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Custom Pair class who implements serialization
 * Use for data transfer between devices (Game instance, Object)
 * @param <F> -> Game
 * @param <S> -> Object
 */

public class Pair<F, S> implements Serializable {
    public final F first;
    public final S second;

    public Pair(F s, S o)
    {
        this.first = s;
        this.second = o;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> that = (Pair<?, ?>) o;
        return first.equals(that.first) &&
                second.equals(that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    public static <A, B> Pair<A, B> create(A a, B b){
        return new Pair<A, B>(a, b);
    }

}