package com.quickrant.util;

import java.util.Map;

public class MapEntry<Key, Value> implements Map.Entry<Key, Value>{

    private final Key key;
    private Value value;

    public MapEntry(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public Value setValue(Value value) {
        Value old = this.value;
        this.value = value;
        return old;
    }

}
