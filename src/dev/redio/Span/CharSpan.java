package dev.redio.Span;

import java.util.Objects;

public final class CharSpan 
        extends ReadOnlyCharSpan
        implements Span<Character> {

    private final IntCharBiConsumer setter;

    public CharSpan(char[] array) {
        super(array);
        this.setter = (i,v) -> array[i] = v;
    }

    public CharSpan(char[] array, int start, int size) {
        super(array, start, size);
        this.setter = (i,v) -> array[i] = v;
    }

    CharSpan(ReadOnlyCharSpan span) {
        this(copyData(span));
    }

    private CharSpan(CharIntFunction getter, IntCharBiConsumer setter, int start, int size) {
        super(getter, start, size);
        this.setter = setter;
    }

    

    public void placeCharAt(int index, char value) {
        Objects.checkIndex(index, this.size);
        this.setter.placeCharAt(this.start + index, value);
    }

    @Override
    public void set(int index, Character value) {
        this.placeCharAt(index, value);
    }

    @Override
    public Span<Character> slice(int start, int size) {
        Objects.checkFromToIndex(start, start + size, this.size);
        return new CharSpan(this.getter, this.setter, this.start + start, size);
    }

    private static char[] copyData(ReadOnlyCharSpan span) {
        char[] array = new char[span.size];
        for (int i = 0; i < span.size; i++) 
            array[i] = span.charAt(i);
        return array;
    }
    
}

interface IntCharBiConsumer {
    void placeCharAt(int index, char value);
}
