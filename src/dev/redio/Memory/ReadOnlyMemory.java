package dev.redio.Memory;

public interface ReadOnlyMemory<T> {

    T get(int index);

    int size();

    Memory<T> toMemory();
}
