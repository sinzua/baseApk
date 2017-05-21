package com.parse;

import bolts.Task;
import java.util.List;

interface ParseObjectController {
    List<Task<Void>> deleteAllAsync(List<State> list, String str);

    Task<Void> deleteAsync(State state, String str);

    Task<State> fetchAsync(State state, String str, ParseDecoder parseDecoder);

    List<Task<State>> saveAllAsync(List<State> list, List<ParseOperationSet> list2, String str, List<ParseDecoder> list3);

    Task<State> saveAsync(State state, ParseOperationSet parseOperationSet, String str, ParseDecoder parseDecoder);
}
