package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

class NetworkObjectController implements ParseObjectController {
    private ParseHttpClient client;
    private ParseObjectCoder coder = ParseObjectCoder.get();

    public NetworkObjectController(ParseHttpClient client) {
        this.client = client;
    }

    public Task<State> fetchAsync(final State state, String sessionToken, final ParseDecoder decoder) {
        ParseRESTCommand command = ParseRESTObjectCommand.getObjectCommand(state.objectId(), state.className(), sessionToken);
        command.enableRetrying();
        return command.executeAsync(this.client).onSuccess(new Continuation<JSONObject, State>() {
            public State then(Task<JSONObject> task) throws Exception {
                JSONObject result = (JSONObject) task.getResult();
                return NetworkObjectController.this.coder.decode(state.newBuilder().clear(), result, decoder).isComplete(true).build();
            }
        });
    }

    public Task<State> saveAsync(final State state, ParseOperationSet operations, String sessionToken, final ParseDecoder decoder) {
        ParseRESTObjectCommand command = ParseRESTObjectCommand.saveObjectCommand(state, this.coder.encode(state, operations, PointerEncoder.get()), sessionToken);
        command.enableRetrying();
        return command.executeAsync(this.client).onSuccess(new Continuation<JSONObject, State>() {
            public State then(Task<JSONObject> task) throws Exception {
                JSONObject result = (JSONObject) task.getResult();
                return NetworkObjectController.this.coder.decode(state.newBuilder().clear(), result, decoder).isComplete(false).build();
            }
        });
    }

    public List<Task<State>> saveAllAsync(List<State> states, List<ParseOperationSet> operationsList, String sessionToken, List<ParseDecoder> decoders) {
        int i;
        int batchSize = states.size();
        List<ParseRESTObjectCommand> commands = new ArrayList(batchSize);
        ParseEncoder encoder = PointerEncoder.get();
        for (i = 0; i < batchSize; i++) {
            State state = (State) states.get(i);
            commands.add(ParseRESTObjectCommand.saveObjectCommand(state, this.coder.encode(state, (ParseOperationSet) operationsList.get(i), encoder), sessionToken));
        }
        List<Task<JSONObject>> batchTasks = ParseRESTObjectBatchCommand.executeBatch(this.client, commands, sessionToken);
        List<Task<State>> tasks = new ArrayList(batchSize);
        for (i = 0; i < batchSize; i++) {
            state = (State) states.get(i);
            final ParseDecoder decoder = (ParseDecoder) decoders.get(i);
            tasks.add(((Task) batchTasks.get(i)).onSuccess(new Continuation<JSONObject, State>() {
                public State then(Task<JSONObject> task) throws Exception {
                    JSONObject result = (JSONObject) task.getResult();
                    return NetworkObjectController.this.coder.decode(state.newBuilder().clear(), result, decoder).isComplete(false).build();
                }
            }));
        }
        return tasks;
    }

    public Task<Void> deleteAsync(State state, String sessionToken) {
        ParseRESTObjectCommand command = ParseRESTObjectCommand.deleteObjectCommand(state, sessionToken);
        command.enableRetrying();
        return command.executeAsync(this.client).makeVoid();
    }

    public List<Task<Void>> deleteAllAsync(List<State> states, String sessionToken) {
        int i;
        int batchSize = states.size();
        List<ParseRESTObjectCommand> commands = new ArrayList(batchSize);
        for (i = 0; i < batchSize; i++) {
            ParseRESTObjectCommand command = ParseRESTObjectCommand.deleteObjectCommand((State) states.get(i), sessionToken);
            command.enableRetrying();
            commands.add(command);
        }
        List<Task<JSONObject>> batchTasks = ParseRESTObjectBatchCommand.executeBatch(this.client, commands, sessionToken);
        List<Task<Void>> tasks = new ArrayList(batchSize);
        for (i = 0; i < batchSize; i++) {
            tasks.add(((Task) batchTasks.get(i)).makeVoid());
        }
        return tasks;
    }
}
