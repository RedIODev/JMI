package dev.redio.Span;

import java.util.Objects;

public final class ReadOnlyCharSpan 
        extends ReadOnlySpan<Character>
    	implements CharSequence {

    public ReadOnlyCharSpan(char[] array) {
        this(array, 0, array.length);
    }

    public ReadOnlyCharSpan(char[] array, int start, int size) {
        super(memory, start, size);
    }

    public ReadOnlyCharSpan(String s) {
        this(s, 0, s.length());
    }

    public ReadOnlyCharSpan(String s, int start, int size) {
        Objects.checkFromToIndex(start, start + size, s.length());
        this.getter = s::charAt;
        this.start = start;
        this.size = size;
    }

    protected ReadOnlyCharSpan(CharIntFunction getter, int start, int size) {
        this.getter = getter;
        this.start = start;
        this.size = size;
    }

    @Override
    public int length() {
        return this.size;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public char charAt(int index) {
        Objects.checkIndex(index, this.size);
        return getter.charAt(this.start + index);
    }

    @Override
    public Character get(int index) {
        return this.charAt(index);
    }

    @Override
    public CharSpan toSpan() {
        return new CharSpan(this);
    }

    @Override
    public ReadOnlySpan<Character> slice(int start, int size) {
        Objects.checkFromToIndex(start, start + size, this.size);
        return new ReadOnlyCharSpan(this.getter, this.start + start, size);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        Objects.checkFromToIndex(start, end, this.size);
        return new ReadOnlyCharSpan(this.getter, this.start + start, end - start);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder(this.size);
        for (int i = 0; i < this.size; i++) 
            builder.append(this.charAt(i));
        return builder.toString();
    }
}


