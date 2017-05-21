package com.parse;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

class LocalIdManager {
    private final File diskPath;
    private final Random random = new Random();

    private static class MapEntry {
        String objectId;
        int retainCount;

        private MapEntry() {
        }
    }

    LocalIdManager(File root) {
        this.diskPath = new File(root, "LocalId");
    }

    private boolean isLocalId(String localId) {
        if (!localId.startsWith("local_")) {
            return false;
        }
        for (int i = 6; i < localId.length(); i++) {
            char c = localId.charAt(i);
            if ((c < '0' || c > '9') && (c < 'a' || c > 'f')) {
                return false;
            }
        }
        return true;
    }

    private synchronized MapEntry getMapEntry(String localId) {
        MapEntry entry;
        if (isLocalId(localId)) {
            try {
                JSONObject json = ParseFileUtils.readFileToJSONObject(new File(this.diskPath, localId));
                entry = new MapEntry();
                entry.retainCount = json.optInt("retainCount", 0);
                entry.objectId = json.optString("objectId", null);
            } catch (Exception e) {
                entry = new MapEntry();
                return entry;
            } catch (JSONException e2) {
                JSONException jSONException = e2;
                entry = new MapEntry();
                return entry;
            }
        }
        throw new IllegalStateException("Tried to get invalid local id: \"" + localId + "\".");
        return entry;
    }

    private synchronized void putMapEntry(String localId, MapEntry entry) {
        if (isLocalId(localId)) {
            JSONObject json = new JSONObject();
            try {
                json.put("retainCount", entry.retainCount);
                if (entry.objectId != null) {
                    json.put("objectId", entry.objectId);
                }
                File file = new File(this.diskPath, localId);
                if (!this.diskPath.exists()) {
                    this.diskPath.mkdirs();
                }
                try {
                    ParseFileUtils.writeJSONObjectToFile(file, json);
                } catch (IOException e) {
                }
            } catch (JSONException je) {
                throw new IllegalStateException("Error creating local id map entry.", je);
            }
        }
        throw new IllegalStateException("Tried to get invalid local id: \"" + localId + "\".");
    }

    private synchronized void removeMapEntry(String localId) {
        if (isLocalId(localId)) {
            ParseFileUtils.deleteQuietly(new File(this.diskPath, localId));
        } else {
            throw new IllegalStateException("Tried to get invalid local id: \"" + localId + "\".");
        }
    }

    synchronized String createLocalId() {
        String localId;
        localId = "local_" + Long.toHexString(this.random.nextLong());
        if (!isLocalId(localId)) {
            throw new IllegalStateException("Generated an invalid local id: \"" + localId + "\". " + "This should never happen. Contact us at https://parse.com/help");
        }
        return localId;
    }

    synchronized void retainLocalIdOnDisk(String localId) {
        MapEntry entry = getMapEntry(localId);
        entry.retainCount++;
        putMapEntry(localId, entry);
    }

    synchronized void releaseLocalIdOnDisk(String localId) {
        MapEntry entry = getMapEntry(localId);
        entry.retainCount--;
        if (entry.retainCount > 0) {
            putMapEntry(localId, entry);
        } else {
            removeMapEntry(localId);
        }
    }

    synchronized String getObjectId(String localId) {
        return getMapEntry(localId).objectId;
    }

    synchronized void setObjectId(String localId, String objectId) {
        MapEntry entry = getMapEntry(localId);
        if (entry.retainCount > 0) {
            if (entry.objectId != null) {
                throw new IllegalStateException("Tried to set an objectId for a localId that already has one.");
            }
            entry.objectId = objectId;
            putMapEntry(localId, entry);
        }
    }

    synchronized boolean clear() throws IOException {
        boolean z = false;
        synchronized (this) {
            String[] files = this.diskPath.list();
            if (files != null) {
                if (files.length != 0) {
                    String[] arr$ = files;
                    int len$ = arr$.length;
                    int i$ = 0;
                    while (i$ < len$) {
                        String fileName = arr$[i$];
                        if (new File(this.diskPath, fileName).delete()) {
                            i$++;
                        } else {
                            throw new IOException("Unable to delete file " + fileName + " in localId cache.");
                        }
                    }
                    z = true;
                }
            }
        }
        return z;
    }
}
