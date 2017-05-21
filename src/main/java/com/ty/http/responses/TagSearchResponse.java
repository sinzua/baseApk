package com.ty.http.responses;

import com.ty.entities.Tag;
import java.util.ArrayList;

public class TagSearchResponse extends BasicResponse {
    private ArrayList<Tag> results;

    public ArrayList<Tag> getResults() {
        return this.results;
    }

    public void setResults(ArrayList<Tag> results) {
        this.results = results;
    }
}
