package com.sproutigy.commons.basement.collections;

import java.util.Iterator;

/**
 * LazyIterator keeps reference to Iterable source
 * and calls its iterator() method when it is needed
 * @param <E> element type
 */
public final class LazyIterator<E> implements Iterator<E> {

    private Iterable<E> source;
    private Iterator<E> innerIterator;

    public LazyIterator(Iterable<E> source) {
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        if (innerIterator == null) {
            innerIterator = source.iterator();
        }
        return innerIterator.hasNext();
    }

    @Override
    public E next() {
        if (innerIterator == null) {
            innerIterator = source.iterator();
        }
        return innerIterator.next();
    }

    @Override
    public void remove() {
        if (innerIterator == null) {
            innerIterator = source.iterator();
        }
        innerIterator.remove();
    }
}
