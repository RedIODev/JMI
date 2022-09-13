package dev.redio.Memory;

class Array<T> implements Memory<T> {

    private final T[] array;

    Array(T[] array) {
        this.array = array;
    }
    
    @Override
    public T get(int index) {
        return array[index];
    }

    @Override
    public int size() {
        return this.array.length;
    }

    @Override
    public void set(int index, T value) {
        this.array[index] = value;
    }

    @Override
    public ReadOnlyMemory<T> asReadOnlyMemory() {
        return new ReadOnlyArray<>(this.array);
    }

}

class ReadOnlyArray<T> implements ReadOnlyMemory<T> {

    private final T[] array;

    ReadOnlyArray(T[] array) {
        this.array = array;
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    @Override
    public int size() {
        return this.array.length;
    }

    @Override
    public Memory<T> toMemory() {
        return new Array<>(this.array.clone());
    }

}