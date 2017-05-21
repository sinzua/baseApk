package com.parse;

import bolts.Task;
import java.util.List;

interface ParseQueryController {
    <T extends ParseObject> Task<Integer> countAsync(State<T> state, ParseUser parseUser, Task<Void> task);

    <T extends ParseObject> Task<List<T>> findAsync(State<T> state, ParseUser parseUser, Task<Void> task);

    <T extends ParseObject> Task<T> getFirstAsync(State<T> state, ParseUser parseUser, Task<Void> task);
}
