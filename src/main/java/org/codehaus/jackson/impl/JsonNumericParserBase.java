package org.codehaus.jackson.impl;

import org.codehaus.jackson.io.IOContext;

@Deprecated
public abstract class JsonNumericParserBase extends JsonParserBase {
    protected JsonNumericParserBase(IOContext ctxt, int features) {
        super(ctxt, features);
    }
}
