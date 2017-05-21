package com.parse;

import bolts.Continuation;
import bolts.Task;
import com.parse.ParseRequest.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("_EventuallyPin")
class EventuallyPin extends ParseObject {
    public static final String PIN_NAME = "_eventuallyPin";
    public static final int TYPE_COMMAND = 3;
    public static final int TYPE_DELETE = 2;
    public static final int TYPE_SAVE = 1;

    public EventuallyPin() {
        super("_EventuallyPin");
    }

    boolean needsDefaultACL() {
        return false;
    }

    public String getUUID() {
        return getString("uuid");
    }

    public int getType() {
        return getInt("type");
    }

    public ParseObject getObject() {
        return getParseObject("object");
    }

    public String getOperationSetUUID() {
        return getString("operationSetUUID");
    }

    public String getSessionToken() {
        return getString("sessionToken");
    }

    public ParseRESTCommand getCommand() throws JSONException {
        JSONObject json = getJSONObject("command");
        if (ParseRESTCommand.isValidCommandJSONObject(json)) {
            return ParseRESTCommand.fromJSONObject(json);
        }
        if (ParseRESTCommand.isValidOldFormatCommandJSONObject(json)) {
            return null;
        }
        throw new JSONException("Failed to load command from JSON.");
    }

    public static Task<EventuallyPin> pinEventuallyCommand(ParseObject object, ParseRESTCommand command) {
        int type = 3;
        JSONObject json = null;
        if (!command.httpPath.startsWith("classes")) {
            json = command.toJSONObject();
        } else if (command.method == Method.POST || command.method == Method.PUT) {
            type = 1;
        } else if (command.method == Method.DELETE) {
            type = 2;
        }
        return pinEventuallyCommand(type, object, command.getOperationSetUUID(), command.getSessionToken(), json);
    }

    private static Task<EventuallyPin> pinEventuallyCommand(int type, ParseObject obj, String operationSetUUID, String sessionToken, JSONObject command) {
        final EventuallyPin pin = new EventuallyPin();
        pin.put("uuid", UUID.randomUUID().toString());
        pin.put("time", new Date());
        pin.put("type", Integer.valueOf(type));
        if (obj != null) {
            pin.put("object", obj);
        }
        if (operationSetUUID != null) {
            pin.put("operationSetUUID", operationSetUUID);
        }
        if (sessionToken != null) {
            pin.put("sessionToken", sessionToken);
        }
        if (command != null) {
            pin.put("command", command);
        }
        return pin.pinInBackground(PIN_NAME).continueWith(new Continuation<Void, EventuallyPin>() {
            public EventuallyPin then(Task<Void> task) throws Exception {
                return pin;
            }
        });
    }

    public static Task<List<EventuallyPin>> findAllPinned() {
        return findAllPinned(null);
    }

    public static Task<List<EventuallyPin>> findAllPinned(Collection<String> excludeUUIDs) {
        ParseQuery<EventuallyPin> query = new ParseQuery(EventuallyPin.class).fromPin(PIN_NAME).ignoreACLs().orderByAscending("time");
        if (excludeUUIDs != null) {
            query.whereNotContainedIn("uuid", excludeUUIDs);
        }
        return query.findInBackground().continueWithTask(new Continuation<List<EventuallyPin>, Task<List<EventuallyPin>>>() {
            public Task<List<EventuallyPin>> then(Task<List<EventuallyPin>> task) throws Exception {
                final List<EventuallyPin> pins = (List) task.getResult();
                List<Task<Void>> tasks = new ArrayList();
                for (EventuallyPin pin : pins) {
                    ParseObject object = pin.getObject();
                    if (object != null) {
                        tasks.add(object.fetchFromLocalDatastoreAsync().makeVoid());
                    }
                }
                return Task.whenAll(tasks).continueWithTask(new Continuation<Void, Task<List<EventuallyPin>>>() {
                    public Task<List<EventuallyPin>> then(Task<Void> task) throws Exception {
                        return Task.forResult(pins);
                    }
                });
            }
        });
    }
}
