package dev.redio.Memory;

public interface Memory<T> {

    T get(int index);

    int size();
    
    void set(int index, T value);

    ReadOnlyMemory<T> asReadOnlyMemory();
}
