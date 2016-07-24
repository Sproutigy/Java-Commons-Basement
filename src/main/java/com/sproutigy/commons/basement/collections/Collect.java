package com.sproutigy.commons.basement.collections;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Collect {
    private Collect() {
    }


    @SuppressWarnings("unchecked")
    public static <E> E[] asArray(E... elements) {
        return elements;
    }

    public static Object[] asArray(Iterator iterator) {
        return asLinkedList(iterator).toArray();
    }

    public static Object[] asArray(Iterator iterator, int size) {
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++) {
            if (!iterator.hasNext()) break;
            array[i] = iterator.next();
        }
        return array;
    }

    public static Object[] asArray(Iterable iterable) {
        return asArray(iterable.iterator());
    }

    public static Object[] asArray(Collection collection) {
        return collection.toArray();
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] asArray(Class<? extends E> componentType, Object[] array) {
        final E[] target = (E[]) Array.newInstance(componentType, array.length);
        for (int i = 0; i < array.length; i++) {
            target[i] = (E) array;
        }
        return target;
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] asArray(Class<? extends E> componentType, Collection<? extends E> collection) {
        final E[] target = (E[]) Array.newInstance(componentType, collection.size());
        collection.toArray(target);
        return target;
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] asArray(Class<? extends E> componentType, Iterable<? extends E> iterable) {
        return asArray(componentType, iterable.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] asArray(Class<? extends E> componentType, Iterator<? extends E> iterator) {
        List<E> list = asLinkedList(iterator);
        final E[] target = (E[]) Array.newInstance(componentType, list.size());
        list.toArray(target);
        return target;
    }


    public static <E> Enumeration<E> asEnumeration(final E... elements) {
        return new Enumeration<E>() {
            private int i = 0;

            @Override
            public boolean hasMoreElements() {
                return i < elements.length;
            }

            @Override
            public E nextElement() {
                if (!hasMoreElements()) {
                    throw new NoSuchElementException();
                }

                E element = elements[i];
                i++;
                return element;
            }
        };
    }

    public static <E> Enumeration<E> asEnumeration(final Iterator<? extends E> iterator) {
        return new Enumeration<E>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public E nextElement() {
                return iterator.next();
            }
        };
    }

    public static <E> Enumeration<E> asEnumeration(final Iterable<? extends E> iterable) {
        return asEnumeration(iterable.iterator());
    }


    public static <E> Iterator<E> asIterator(E... elements) {
        return asList(elements).iterator();
    }

    public static <E> Iterator<E> asIterator(final Enumeration<? extends E> enumeration) {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public E next() {
                return enumeration.nextElement();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <E> Iterator<E> asIterator(Iterable<? extends E> iterable) {
        final Iterator<? extends E> iterator = iterable.iterator();

        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }


    public static <E> Iterable<E> asIterable(E... elements) {
        return asList(elements);
    }

    public static <E> Iterable<E> asIterable(Enumeration<? extends E> enumeration) {
        return asList(enumeration);
    }

    public static <E> Iterable<E> asIterable(Iterator<? extends E> iterator) {
        return asList(iterator);
    }

    public static <E> Iterable<E> asIterable(Iterable<? extends E> iterable) {
        return asList(iterable);
    }


    public static <E> List<E> asList(E... elements) {
        return Arrays.asList(elements);
    }

    public static <E> List<E> asList(Enumeration<? extends E> enumeration) {
        return asLinkedList(enumeration);
    }

    public static <E> List<E> asList(Iterator<? extends E> iterator) {
        return asLinkedList(iterator);
    }

    public static <E> List<E> asList(Iterable<? extends E> iterable) {
        return asLinkedList(iterable);
    }

    public static <E> List<E> asList(Collection<? extends E> collection) {
        return asArrayList(collection);
    }


    public static <E> Set<E> asSet(E... elements) {
        return asLinkedHashSet(elements);
    }

    public static <E> Set<E> asSet(Enumeration<? extends E> enumeration) {
        return asLinkedHashSet(enumeration);
    }

    public static <E> Set<E> asSet(Iterator<? extends E> iterator) {
        return asLinkedHashSet(iterator);
    }

    public static <E> Set<E> asSet(Iterable<? extends E> iterable) {
        return asLinkedHashSet(iterable);
    }

    public static <E> Set<E> asSet(Collection<? extends E> collection) {
        return asLinkedHashSet(collection);
    }


    public static <E> ArrayList<E> asArrayList(E... elements) {
        ArrayList<E> target = new ArrayList<>(elements.length);
        Collections.addAll(target, elements);
        return target;
    }

    public static <E> ArrayList<E> asArrayList(Enumeration<? extends E> enumeration) {
        ArrayList<E> target = new ArrayList<>();
        to(target, enumeration);
        return target;
    }

    public static <E> ArrayList<E> asArrayList(Iterator<? extends E> iterator) {
        ArrayList<E> target = new ArrayList<>();
        to(target, iterator);
        return target;
    }

    public static <E> ArrayList<E> asArrayList(Iterable<? extends E> iterable) {
        return asArrayList(iterable.iterator());
    }

    public static <E> ArrayList<E> asArrayList(Collection<? extends E> collection) {
        return new ArrayList<>(collection);
    }


    public static <E> LinkedList<E> asLinkedList(E... elements) {
        LinkedList<E> target = new LinkedList<>();
        Collections.addAll(target, elements);
        return target;
    }

    public static <E> LinkedList<E> asLinkedList(Enumeration<? extends E> enumeration) {
        LinkedList<E> target = new LinkedList<>();
        to(target, enumeration);
        return target;
    }

    public static <E> LinkedList<E> asLinkedList(Iterator<? extends E> iterator) {
        LinkedList<E> target = new LinkedList<>();
        to(target, iterator);
        return target;
    }

    public static <E> LinkedList<E> asLinkedList(Iterable<? extends E> iterable) {
        return asLinkedList(iterable.iterator());
    }

    public static <E> LinkedList<E> asLinkedList(Collection<? extends E> collection) {
        return new LinkedList<>(collection);
    }


    public static <E> CopyOnWriteArrayList<E> asCopyOnWriteArrayList(E... elements) {
        CopyOnWriteArrayList<E> target = new CopyOnWriteArrayList<>();
        Collections.addAll(target, elements);
        return target;
    }

    public static <E> CopyOnWriteArrayList<E> asCopyOnWriteArrayList(Enumeration<? extends E> enumeration) {
        CopyOnWriteArrayList<E> target = new CopyOnWriteArrayList<>();
        to(target, enumeration);
        return target;
    }

    public static <E> CopyOnWriteArrayList<E> asCopyOnWriteArrayList(Iterator<? extends E> iterator) {
        CopyOnWriteArrayList<E> target = new CopyOnWriteArrayList<>();
        to(target, iterator);
        return target;
    }

    public static <E> CopyOnWriteArrayList<E> asCopyOnWriteArrayList(Iterable<? extends E> iterable) {
        return asCopyOnWriteArrayList(iterable.iterator());
    }

    public static <E> CopyOnWriteArrayList<E> asCopyOnWriteArrayList(Collection<? extends E> collection) {
        return new CopyOnWriteArrayList<>(collection);
    }


    public static <E> HashSet<E> asHashSet(E... elements) {
        HashSet<E> target = new HashSet<>(elements.length);
        Collections.addAll(target, elements);
        return target;
    }

    public static <E> HashSet<E> asHashSet(Enumeration<? extends E> enumeration) {
        HashSet<E> target = new HashSet<>();
        to(target, enumeration);
        return target;
    }

    public static <E> HashSet<E> asHashSet(Iterator<? extends E> iterator) {
        HashSet<E> target = new HashSet<>();
        to(target, iterator);
        return target;
    }

    public static <E> HashSet<E> asHashSet(Iterable<? extends E> iterable) {
        return asHashSet(iterable.iterator());
    }

    public static <E> HashSet<E> asHashSet(Collection<? extends E> collection) {
        return new HashSet<>(collection);
    }


    public static <E> LinkedHashSet<E> asLinkedHashSet(E... elements) {
        LinkedHashSet<E> target = new LinkedHashSet<>(elements.length);
        Collections.addAll(target, elements);
        return target;
    }

    public static <E> LinkedHashSet<E> asLinkedHashSet(Enumeration<? extends E> enumeration) {
        LinkedHashSet<E> target = new LinkedHashSet<>();
        to(target, enumeration);
        return target;
    }

    public static <E> LinkedHashSet<E> asLinkedHashSet(Iterator<? extends E> iterator) {
        LinkedHashSet<E> target = new LinkedHashSet<>();
        to(target, iterator);
        return target;
    }

    public static <E> LinkedHashSet<E> asLinkedHashSet(Iterable<? extends E> iterable) {
        return asLinkedHashSet(iterable.iterator());
    }

    public static <E> LinkedHashSet<E> asLinkedHashSet(Collection<? extends E> collection) {
        return new LinkedHashSet<>(collection);
    }


    public static <E> TreeSet<E> asTreeSet(E... elements) {
        TreeSet<E> target = new TreeSet<>();
        Collections.addAll(target, elements);
        return target;
    }

    public static <E> TreeSet<E> asTreeSet(Enumeration<? extends E> enumeration) {
        TreeSet<E> target = new TreeSet<>();
        to(target, enumeration);
        return target;
    }

    public static <E> TreeSet<E> asTreeSet(Iterator<? extends E> iterator) {
        TreeSet<E> target = new TreeSet<>();
        to(target, iterator);
        return target;
    }

    public static <E> TreeSet<E> asTreeSet(Iterable<? extends E> iterable) {
        return asTreeSet(iterable.iterator());
    }

    public static <E> TreeSet<E> asTreeSet(Collection<? extends E> collection) {
        return new TreeSet<>(collection);
    }


    public static <E> int to(Collection<E> target, E... elements) {
        Collections.addAll(target, elements);
        return elements.length;
    }

    public static <E> int to(Collection<E> target, Enumeration<? extends E> elements) {
        int count = 0;
        while (elements.hasMoreElements()) {
            target.add(elements.nextElement());
            count++;
        }
        return count;
    }

    public static <E> int to(Collection<E> target, Iterator<? extends E> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            target.add(iterator.next());
            count++;
        }
        return count;
    }

    public static <E> int to(Collection<E> target, Iterable<? extends E> iterable) {
        return to(target, iterable.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <E> int to(E[] array, Iterator<? extends E> iterator) {
        return to(array, 0, iterator);
    }

    public static <E> int to(E[] array, int offset, Iterator<? extends E> iterator) {
        int count = 0;
        for (int i = offset; ; i++) {
            if (iterator.hasNext()) {
                E element = iterator.next();
                array[i] = element;
                count++;
            } else {
                break;
            }
        }
        return count;
    }


    public static <E> boolean containsAny(Collection<E> target, E... elements) {
        for (E element : elements) {
            if (target.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public static <E> boolean containsAny(Collection<E> target, Enumeration<? extends E> enumeration) {
        while (enumeration.hasMoreElements()) {
            E element = enumeration.nextElement();
            if (target.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public static <E> boolean containsAny(Collection<E> target, Iterator<? extends E> iterator) {
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (target.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public static <E> boolean containsAny(Collection<E> target, Iterable<? extends E> iterable) {
        return containsAny(target, iterable.iterator());
    }


    public static <E> boolean containsAll(Collection<E> target, E... elements) {
        for (E element : elements) {
            if (target.contains(element)) {
                return false;
            }
        }
        return true;
    }

    public static <E> boolean containsAll(Collection<E> target, Enumeration<? extends E> enumeration) {
        while (enumeration.hasMoreElements()) {
            E element = enumeration.nextElement();
            if (target.contains(element)) {
                return false;
            }
        }
        return true;
    }

    public static <E> boolean containsAll(Collection<E> target, Iterator<? extends E> iterator) {
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (target.contains(element)) {
                return false;
            }
        }
        return true;
    }

    public static <E> boolean containsAll(Collection<E> target, Iterable<? extends E> iterable) {
        return containsAny(target, iterable.iterator());
    }


    public static <E> int remove(Collection<? extends E> target, E... elements) {
        int count = 0;
        for (E element : elements) {
            if (target.remove(element)) {
                count++;
            }
        }
        return count;
    }

    public static <E> int remove(Collection<? extends E> target, Enumeration<E> enumeration) {
        int count = 0;
        while (enumeration.hasMoreElements()) {
            if (target.remove(enumeration.nextElement())) {
                count++;
            }
        }
        return count;
    }

    public static <E> int remove(Collection<E> target, Iterator<? extends E> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            if (target.remove(iterator.next())) {
                count++;
            }
        }
        return count;
    }

    public static <E> int remove(Collection<E> target, Iterable<? extends E> iterable) {
        return remove(target, iterable.iterator());
    }


    public static <E> List<E> unmodifiable(List<? extends E> list) {
        return Collections.unmodifiableList(list);
    }

    public static <E> Set<E> unmodifiable(Set<? extends E> set) {
        return Collections.unmodifiableSet(set);
    }

    public static <E> SortedSet<E> unmodifiable(SortedSet<E> sortedSet) {
        return Collections.unmodifiableSortedSet(sortedSet);
    }

    public static <E> Collection<E> unmodifiable(Collection<? extends E> collection) {
        return Collections.unmodifiableCollection(collection);
    }

    public static <K, V> Map<K, V> unmodifiable(Map<K, V> map) {
        return Collections.unmodifiableMap(map);
    }

    public static <E> Iterator<E> unmodifiable(final Iterator<? extends E> iterator) {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    public static <E> Iterable<E> unmodifiable(final Iterable<? extends E> iterable) {
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return unmodifiable(iterable.iterator());
            }
        };
    }

    public static <K, V> Map.Entry<K, V> unmodifiable(final Map.Entry<K, V> entry) {
        return new Map.Entry<K, V>() {
            @Override
            public K getKey() {
                return entry.getKey();
            }

            @Override
            public V getValue() {
                return entry.getValue();
            }

            @Override
            public V setValue(V value) {
                throw new UnsupportedOperationException("setValue");
            }
        };
    }

}
