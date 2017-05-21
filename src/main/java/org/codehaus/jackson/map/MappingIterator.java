package org.codehaus.jackson.map;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.type.JavaType;

public class MappingIterator<T> implements Iterator<T> {
    protected static final MappingIterator<?> EMPTY_ITERATOR = new MappingIterator(null, null, null, null, false, null);
    protected final boolean _closeParser;
    protected final DeserializationContext _context;
    protected final JsonDeserializer<T> _deserializer;
    protected boolean _hasNextChecked;
    protected JsonParser _parser;
    protected final JavaType _type;
    protected final T _updatedValue;

    protected MappingIterator(JavaType type, JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) {
        this(type, jp, ctxt, deser, true, null);
    }

    protected MappingIterator(JavaType type, JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser, boolean closeParser, Object valueToUpdate) {
        this._type = type;
        this._parser = jp;
        this._context = ctxt;
        this._deserializer = deser;
        if (!(jp == null || jp.getCurrentToken() != JsonToken.START_ARRAY || jp.getParsingContext().inRoot())) {
            jp.clearCurrentToken();
        }
        this._closeParser = closeParser;
        if (valueToUpdate == null) {
            this._updatedValue = null;
        } else {
            this._updatedValue = valueToUpdate;
        }
    }

    protected static <T> MappingIterator<T> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    public boolean hasNext() {
        try {
            return hasNextValue();
        } catch (JsonMappingException e) {
            throw new RuntimeJsonMappingException(e.getMessage(), e);
        } catch (IOException e2) {
            throw new RuntimeException(e2.getMessage(), e2);
        }
    }

    public T next() {
        try {
            return nextValue();
        } catch (JsonMappingException e) {
            throw new RuntimeJsonMappingException(e.getMessage(), e);
        } catch (IOException e2) {
            throw new RuntimeException(e2.getMessage(), e2);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNextValue() throws IOException {
        if (this._parser == null) {
            return false;
        }
        if (!this._hasNextChecked) {
            JsonToken t = this._parser.getCurrentToken();
            this._hasNextChecked = true;
            if (t == null) {
                t = this._parser.nextToken();
                if (t == null) {
                    JsonParser jp = this._parser;
                    this._parser = null;
                    if (!this._closeParser) {
                        return false;
                    }
                    jp.close();
                    return false;
                } else if (t == JsonToken.END_ARRAY) {
                    return false;
                }
            }
        }
        return true;
    }

    public T nextValue() throws IOException {
        if (!this._hasNextChecked && !hasNextValue()) {
            throw new NoSuchElementException();
        } else if (this._parser == null) {
            throw new NoSuchElementException();
        } else {
            T result;
            this._hasNextChecked = false;
            if (this._updatedValue == null) {
                result = this._deserializer.deserialize(this._parser, this._context);
            } else {
                this._deserializer.deserialize(this._parser, this._context, this._updatedValue);
                result = this._updatedValue;
            }
            this._parser.clearCurrentToken();
            return result;
        }
    }
}
