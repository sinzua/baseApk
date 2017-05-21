package com.parse;

class ParseSetOperation implements ParseFieldOperation {
    private final Object value;

    public ParseSetOperation(Object newValue) {
        this.value = newValue;
    }

    public Object getValue() {
        return this.value;
    }

    public Object encode(ParseEncoder objectEncoder) {
        return objectEncoder.encode(this.value);
    }

    public ParseFieldOperation mergeWithPrevious(ParseFieldOperation previous) {
        return this;
    }

    public Object apply(Object oldValue, String key) {
        return this.value;
    }
}
