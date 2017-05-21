package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Map;
import org.json.JSONObject;

class NetworkUserController implements ParseUserController {
    private static final int STATUS_CODE_CREATED = 201;
    private final ParseHttpClient client;
    private final ParseObjectCoder coder;
    private final boolean revocableSession;

    public NetworkUserController(ParseHttpClient client) {
        this(client, false);
    }

    public NetworkUserController(ParseHttpClient client, boolean revocableSession) {
        this.client = client;
        this.coder = ParseObjectCoder.get();
        this.revocableSession = revocableSession;
    }

    public Task<State> signUpAsync(State state, ParseOperationSet operations, String sessionToken) {
        return ParseRESTUserCommand.signUpUserCommand(this.coder.encode(state, operations, PointerEncoder.get()), sessionToken, this.revocableSession).executeAsync(this.client).onSuccess(new Continuation<JSONObject, State>() {
            public State then(Task<JSONObject> task) throws Exception {
                return ((Builder) ((Builder) NetworkUserController.this.coder.decode(new Builder(), (JSONObject) task.getResult(), ParseDecoder.get())).isComplete(false)).isNew(true).build();
            }
        });
    }

    public Task<State> logInAsync(String username, String password) {
        return ParseRESTUserCommand.logInUserCommand(username, password, this.revocableSession).executeAsync(this.client).onSuccess(new Continuation<JSONObject, State>() {
            public State then(Task<JSONObject> task) throws Exception {
                return ((Builder) ((Builder) NetworkUserController.this.coder.decode(new Builder(), (JSONObject) task.getResult(), ParseDecoder.get())).isComplete(true)).build();
            }
        });
    }

    public Task<State> logInAsync(State state, ParseOperationSet operations) {
        final ParseRESTUserCommand command = ParseRESTUserCommand.serviceLogInUserCommand(this.coder.encode(state, operations, PointerEncoder.get()), state.sessionToken(), this.revocableSession);
        return command.executeAsync(this.client).onSuccess(new Continuation<JSONObject, State>() {
            public State then(Task<JSONObject> task) throws Exception {
                boolean isNew;
                boolean isComplete = true;
                JSONObject result = (JSONObject) task.getResult();
                if (command.getStatusCode() == 201) {
                    isNew = true;
                } else {
                    isNew = false;
                }
                if (isNew) {
                    isComplete = false;
                }
                return ((Builder) ((Builder) NetworkUserController.this.coder.decode(new Builder(), result, ParseDecoder.get())).isComplete(isComplete)).isNew(isNew).build();
            }
        });
    }

    public Task<State> logInAsync(final String authType, final Map<String, String> authData) {
        final ParseRESTUserCommand command = ParseRESTUserCommand.serviceLogInUserCommand(authType, (Map) authData, this.revocableSession);
        return command.executeAsync(this.client).onSuccess(new Continuation<JSONObject, State>() {
            public State then(Task<JSONObject> task) throws Exception {
                boolean z = true;
                Builder builder = (Builder) ((Builder) NetworkUserController.this.coder.decode(new Builder(), (JSONObject) task.getResult(), ParseDecoder.get())).isComplete(true);
                if (command.getStatusCode() != 201) {
                    z = false;
                }
                return builder.isNew(z).putAuthData(authType, authData).build();
            }
        });
    }

    public Task<State> getUserAsync(String sessionToken) {
        return ParseRESTUserCommand.getCurrentUserCommand(sessionToken).executeAsync(this.client).onSuccess(new Continuation<JSONObject, State>() {
            public State then(Task<JSONObject> task) throws Exception {
                return ((Builder) ((Builder) NetworkUserController.this.coder.decode(new Builder(), (JSONObject) task.getResult(), ParseDecoder.get())).isComplete(true)).build();
            }
        });
    }

    public Task<Void> requestPasswordResetAsync(String email) {
        return ParseRESTUserCommand.resetPasswordResetCommand(email).executeAsync(this.client).makeVoid();
    }
}
