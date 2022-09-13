package dev.redio.Memory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.security.InvalidParameterException;

import jdk.incubator.foreign.*;

public class MemoryH<T> {
    private final VarHandle memoryHandle;
    MemoryH(Object array) {
        if(!array.getClass().isArray())
            throw new InvalidParameterException();
        this.memoryHandle = MemoryHandles.insertCoordinates(MethodHandles.arrayElementVarHandle(array.getClass()), 0, array);
    }

    MemoryH(MemorySegment segment) {
        
    }

    T get(int index) {
        return (T)memoryHandle.get(index);
    }

    void set(int index, T value) {
        memoryHandle.set(index,value);
    } 
}
