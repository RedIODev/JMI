package dev.redio.Span;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import dev.redio.Memory.ReadOnlyMemory;

public class ReadOnlySpan<E>
        implements Iterable<E> {
    
    private final ReadOnlyMemory<E> memory;
    private final int start;
    public final int size;

    public ReadOnlySpan(ReadOnlyMemory<E> memory) {
        this(memory, 0, memory.size());
    }

    public ReadOnlySpan(ReadOnlyMemory<E> memory, int start, int size) {
        Objects.requireNonNull(memory);
        Objects.checkFromToIndex(start, start + size, memory.size());
        this.memory = memory;
        this.start = start;
        this.size = size;
    }

    public final E get(int index) {
        Objects.checkIndex(index, this.size);
        return memory.get(this.start + index);
    }

    public final Span<E> toSpan() {
        return new Span<>(this.memory.toMemory(), this.start, this.size);
    }

    public final ReadOnlySpan<E> slice(int start, int size) {
        Objects.checkFromToIndex(start, start + size, this.size);
        return new ReadOnlySpan<>(this.memory, this.start + start, size);
    }

    public final ReadOnlySpan<E> slice(int start) {
        return this.slice(start, this.size - start);
    }

    
    public final boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    
    public final boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!this.contains(e))
                return false;
        return true;
    }

    
    public final int indexOf(Object o) {
        for (int i = 0; i < this.size; i++)
            if (Objects.equals(this.get(i), o))
                return i;
        return -1;
    }

    
    public final int lastIndexOf(Object o) {
        for (int i = this.size - 1; i >= 0; i--)
            if (Objects.equals(this.get(i), o))
                return i;
        return -1;
    }

    
    public final boolean isEmpty() {
        return this.size == 0;
    }

    
    public final Stream<E> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    
    public final Stream<E> parallelStream() {
        return StreamSupport.stream(this.spliterator(), true);
    }

    
    public final Collection<E> asCollection() {
        final class Collect
                implements Collection<E> {
            
            public final int size() {
                return ReadOnlySpan.this.size;
            }

            
            public final boolean isEmpty() {
                return ReadOnlySpan.this.isEmpty();
            }

            
            public final boolean contains(Object o) {
                return ReadOnlySpan.this.contains(o);
            }

            
            public final Iterator<E> iterator() {
                return ReadOnlySpan.this.iterator();
            }

            
            public final Object[] toArray() {
                return ReadOnlySpan.this.toArray();
            }

            
            public final <T> T[] toArray(T[] a) {
                return ReadOnlySpan.this.toArray(a);
            }

            
            public final boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            
            public final boolean remove(Object o) {
                throw new UnsupportedOperationException();
            }

            
            public final boolean containsAll(Collection<?> c) {
                return ReadOnlySpan.this.containsAll(c);
            }

            
            public final boolean addAll(Collection<? extends E> c) {
                throw new UnsupportedOperationException();
            }

            
            public final boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            
            public final boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            
            public final void clear() {
                throw new UnsupportedOperationException();
            }
        }

        return new Collect();
    }

    @Override
    public final Iterator<E> iterator() {

        final class Iter
                implements Iterator<E> {
            private int index = 0;

            
            public final boolean hasNext() {
                return this.index < ReadOnlySpan.this.size;
            }

            
            public final E next() {
                if (this.index >= ReadOnlySpan.this.size)
                    throw new NoSuchElementException();
                return ReadOnlySpan.this.get(this.index++);
            }

        }

        return new Iter();
    }

    @Override
    public final Spliterator<E> spliterator() {

        final class Splitter
                implements Spliterator<E> {
            private final int size;
            private int index = 0;

            Splitter() {
                this(ReadOnlySpan.this.size, 0);
            }

            Splitter(int size, int index) {
                this.size = size;
                this.index = index;
            }

            
            public final boolean tryAdvance(Consumer<? super E> action) {
                if (this.index >= this.size)
                    return false;
                action.accept(ReadOnlySpan.this.get(this.index++));
                return true;
            }

            
            public final Spliterator<E> trySplit() {
                int newIndex = this.index;
                int midPoint = (this.index + this.size) >>> 1;
                if (this.index >= midPoint)
                    return null;
                this.index = midPoint;
                return new Splitter(newIndex, midPoint);
            }

            
            public final long estimateSize() {
                return (this.size - this.index);
            }

            
            public final int characteristics() {
                return ORDERED | SIZED | SUBSIZED | IMMUTABLE;
            }
        }

        return new Splitter();
    }

    
    public final Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = 0; i < this.size; i++)
            array[i] = this.get(i);
        return array;
    }

    
    @SuppressWarnings("unchecked")
    public final <T> T[] toArray(T[] dest) {
        if (dest.length < this.size)
            dest = (T[]) Array.newInstance(dest.getClass().getComponentType(), this.size);
        for (int i = 0; i < this.size; i++)
            dest[i] = (T) this.get(i);
        return dest;
    }

    
    @SuppressWarnings("unchecked")
    public final <T> T[] toArray(IntFunction<T[]> generator) {
        T[] array = generator.apply(this.size);
        for (int i = 0; i < this.size; i++)
            array[i] = (T) this.get(i);
        return array;
    }
}