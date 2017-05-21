package com.parse;

public interface LocationCallback extends ParseCallback2<ParseGeoPoint, ParseException> {
    void done(ParseGeoPoint parseGeoPoint, ParseException parseException);
}
