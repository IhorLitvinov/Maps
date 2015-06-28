package ua.yandex.prioritymap;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PriorityHashMap<K extends Comparable, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final int initialCapacity;
    private final float loadFactor;
    private Node<K, V>[] table;
    private int size;
    private Comparator<Entry<K, V>> entryComparator =
            (firstEntry, secondEntry) -> {
                K firstKey = firstEntry.getKey();
                K secondKey = secondEntry.getKey();
                if (firstKey == null) {
                    return -1;
                }
                if (secondKey == null) {
                    return 1;
                }
                return firstKey.compareTo(secondKey);
            };

    static class Node<K extends Comparable, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        private Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }

        public Node getNextNode() {
            return nextNode;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    public PriorityHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public PriorityHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public PriorityHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(
                    "Initial capacity must be positive or zero.");
        }
        if (loadFactor >= 1 || loadFactor <= 0) {
            throw new IllegalArgumentException(
                    "Load factor must be between 0 and 1.");
        }
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        table = new Node[initialCapacity];
        size = 0;
    }

    private int getInsertPosition(Object key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        int position = hash % table.length;
        if (position < 0) {
            position = -position;
        }
        return position;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return findByKey(key) != null;
    }

    private Node<K, V> findByKey(Object key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode;
        currentNode = table[getInsertPosition(key)];
        while ((currentNode != null)
                && !(Objects.equals(currentNode.key, key))) {
            currentNode = currentNode.nextNode;
        }
        return currentNode;
    }

    @Override
    public boolean containsValue(Object value) {
        Node<K, V> currentNode;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                V nodeValue = currentNode.value;
                if (Objects.equals(value, nodeValue)) {
                    return true;
                }
                currentNode = currentNode.nextNode;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Node<K, V> foundNode = findByKey(key);
        if (foundNode == null) {
            return null;
        }
        return foundNode.value;
    }

    @Override
    public V put(K key, V value) {
        return putSingleValue(key, value);
    }

    private V putSingleValue(K key, V value) {
        Node<K, V> insertionNode = findByKey(key);
        V oldValue = null;
        if (insertionNode == null) {
            if (size >= table.length) {
                extendTable();
            }
            int position = this.getInsertPosition(key);
            Node<K, V> nextNode = table[position];
            table[position] = new Node<>(key, value, nextNode);
            size++;
            return null;
        }
        oldValue = insertionNode.value;
        insertionNode.value = value;
        return oldValue;
    }

    private void extendTable() {
        int newSize = (int) ((table.length + 1) / loadFactor);
        Node<K, V>[] oldTable = table;
        table = new Node[newSize];
        size = 0;
        Node<K, V> nextNode;
        for (Node<K, V> node : oldTable) {
            nextNode = node;
            while (nextNode != null) {
                putSingleValue(nextNode.key, nextNode.value);
                nextNode = nextNode.nextNode;
            }
        }
    }


    @Override
    public V remove(Object key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode;
        int position = getInsertPosition(key);
        currentNode = table[position];
        if (currentNode == null) {
            return null;
        }
        if (Objects.equals(currentNode.key, key)) {
            size--;
            table[position] = currentNode.nextNode;
            return currentNode.value;
        }
        Node<K, V> parent;
        parent = currentNode;
        currentNode = currentNode.nextNode;
        return removeChild(key, parent, currentNode);
    }

    private V removeChild(Object key, Node<K, V> startParent,
                          Node<K, V> startChild) {
        Node<K, V> parent = startParent;
        Node<K, V> child = startChild;
        for (;;) {
            if (child == null) {
                return null;
            }
            if (Objects.equals(child.key, key)) {
                size--;
                parent.nextNode = child.nextNode;
                return child.value;
            }
            parent = child;
            child = child.nextNode;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == null) {
            return;
        }
        for (Entry<? extends K, ? extends V> nextEntry : m.entrySet()) {
            putSingleValue(nextEntry.getKey(), nextEntry.getValue());
        }
    }

    @Override
    public void clear() {
        table = new Node[initialCapacity];
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        K[] keys = (K[]) new Comparable[size];
        int insertionIndex = 0;
        Node<K, V> currentNode;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                K nodeKey = currentNode.key;
                keys[insertionIndex] = nodeKey;
                insertionIndex++;
                currentNode = currentNode.nextNode;
            }
        }
        return new ImmutableFictiveSet<>(keys);
    }

    @Override
    public Collection<V> values() {
        V[] values = (V[]) new Object[size];
        int insertionIndex = 0;
        Node<K, V> currentNode;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                V nodeValue = currentNode.value;
                values[insertionIndex] = nodeValue;
                insertionIndex++;
                currentNode = currentNode.nextNode;
            }
        }
        return new ImmutableFictiveSet<>(values);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Entry<K, V>[] entries = (Entry<K, V>[]) new Entry[size];
        int insertionIndex = 0;
        Node<K, V> currentNode;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                entries[insertionIndex] = currentNode;
                insertionIndex++;
                currentNode = currentNode.nextNode;
            }
        }
        Arrays.sort(entries, entryComparator);
        return new ImmutableFictiveSet<>(entries);
    }

    public V peekValueWithPriorityKey() {
        Node<K, V> maxKeyNode = null;
        Node<K, V> currentNode;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                if (maxKeyNode == null) {
                    if (currentNode.key == null) {
                        maxKeyNode = currentNode;
                    }
                } else if (entryComparator
                        .compare(maxKeyNode, currentNode) == -1) {
                    maxKeyNode = currentNode;
                }
                currentNode = currentNode.nextNode;
            }
        }
        if (maxKeyNode == null) {
            return null;
        }
        return maxKeyNode.value;
    }

    public V pollValueWithPriorityKey() {
        Node<K, V> maxKeyNode = null;
        Node<K, V> maxKeyNodeParent = null;
        int position = 0;
        for (int index = 0; index < table.length; index++) {
            Node<K, V> currentNode = table[index];
            Node<K, V> currentNodeParent = null;
            while (currentNode != null) {
                if (maxKeyNode == null) {
                    if (currentNode.key == null) {
                        maxKeyNode = currentNode;
                        maxKeyNodeParent = currentNodeParent;
                        position = index;
                    }
                } else if (entryComparator
                        .compare(maxKeyNode, currentNode) == -1) {
                    maxKeyNode = currentNode;
                    maxKeyNodeParent = currentNodeParent;
                    position = index;
                }
                currentNodeParent = currentNode;
                currentNode = currentNode.nextNode;
            }
        }
        if (maxKeyNode == null) {
            return null;
        }
        if (maxKeyNodeParent == null) {
            table[position] = maxKeyNode.nextNode;
        } else {
            maxKeyNodeParent.nextNode = maxKeyNode.nextNode;
        }
        return maxKeyNode.value;
    }

    private class ImmutableFictiveSet<T> implements Set<T> {
        private int size = PriorityHashMap.this.size;

        private T[] elements;

        private ImmutableFictiveSet(T[] elements) {
            this.elements = elements;
        }


        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean contains(Object o) {
            if (o == null) {
                for (T key : elements) {
                    if (key == null) {
                        return true;
                    }
                }
            } else {
                for (T key : elements) {
                    if (o.equals(key)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private int position = 0;

                @Override
                public boolean hasNext() {
                    return position < size;
                }

                @Override
                public T next() {
                    T next = elements[position];
                    position++;
                    return next;
                }
            };
        }

        @Override
        public Object[] toArray() {
            Object[] newArray = new Object[size];
            System.arraycopy(elements, 0, newArray, 0, size);
            return newArray;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            T[] array;
            if (a.length >= size) {
                array = a;
            } else {
                array = (T[]) Array.newInstance(a.getClass(), size);
            }
            System.arraycopy(elements, 0, array, 0, size);
            if (array.length > size) {
                array[size] = null;
            }
            return array;
        }

        @Override
        public boolean add(T t) {
            throw new UnsupportedOperationException(
                    "Set is immutable.");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException(
                    "Set is immutable.");
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object element : c) {
                if (!contains(element)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException(
                    "Set is immutable.");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException(
                    "Set is immutable.");
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException(
                    "Set is immutable.");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException(
                    "Set is immutable.");
        }
    }
}
