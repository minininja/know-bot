package org.dorkmaster.knowbot.util;

/**
 * A sorted pair, semi general purpose except that sorting on the second item is inverted
 * @param <A>
 * @param <B>
 */
public class Pair<A extends Comparable, B extends Comparable> implements Comparable<Pair> {
    protected A first;
    protected B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    @Override
    public int compareTo(Pair incoming) {
        // not quite general purpose
        int c = first.compareTo(incoming.getFirst());
        return c != 0 ? c : -1 * second.compareTo(incoming.getSecond());
    }
}
