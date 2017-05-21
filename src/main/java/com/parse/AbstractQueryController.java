package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.List;

abstract class AbstractQueryController implements ParseQueryController {
    AbstractQueryController() {
    }

    public <T extends ParseObject> Task<T> getFirstAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        return findAsync(state, user, cancellationToken).continueWith(new Continuation<List<T>, T>() {
            public T then(Task<List<T>> task) throws Exception {
                if (task.isFaulted()) {
                    throw task.getError();
                } else if (task.getResult() != null && ((List) task.getResult()).size() > 0) {
                    return (ParseObject) ((List) task.getResult()).get(0);
                } else {
                    throw new ParseException(101, "no results found for query");
                }
            }
        });
    }
}
