package com.parse;

import bolts.Task;
import java.util.Map;

interface ParseUserController {
    Task<State> getUserAsync(String str);

    Task<State> logInAsync(State state, ParseOperationSet parseOperationSet);

    Task<State> logInAsync(String str, String str2);

    Task<State> logInAsync(String str, Map<String, String> map);

    Task<Void> requestPasswordResetAsync(String str);

    Task<State> signUpAsync(State state, ParseOperationSet parseOperationSet, String str);
}
