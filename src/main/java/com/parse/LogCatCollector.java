package com.parse;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class LogCatCollector {
    private static final int DEFAULT_TAIL_COUNT = 100;

    LogCatCollector() {
    }

    protected static String collectLogCat(String bufferName) {
        IOException e;
        BoundedLinkedList<String> logcatBuf = null;
        try {
            ArrayList<String> commandLine = new ArrayList();
            commandLine.add("logcat");
            if (bufferName != null) {
                commandLine.add("-b");
                commandLine.add(bufferName);
            }
            int tailCount = -1;
            List<String> logcatArgumentsList = new ArrayList(Arrays.asList(ACRA.getConfig().logcatArguments()));
            int tailIndex = logcatArgumentsList.indexOf("-t");
            if (tailIndex > -1 && tailIndex < logcatArgumentsList.size()) {
                tailCount = Integer.parseInt((String) logcatArgumentsList.get(tailIndex + 1));
                if (Compatibility.getAPILevel() < 8) {
                    logcatArgumentsList.remove(tailIndex + 1);
                    logcatArgumentsList.remove(tailIndex);
                    logcatArgumentsList.add("-d");
                }
            }
            if (tailCount <= 0) {
                tailCount = 100;
            }
            BoundedLinkedList<String> logcatBuf2 = new BoundedLinkedList(tailCount);
            try {
                commandLine.addAll(logcatArgumentsList);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()])).getInputStream()));
                Log.d(ACRA.LOG_TAG, "Retrieving logcat output...");
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    logcatBuf2.add(line + "\n");
                }
                logcatBuf = logcatBuf2;
            } catch (IOException e2) {
                e = e2;
                logcatBuf = logcatBuf2;
                Log.e(ACRA.LOG_TAG, "LogCatCollector.collectLogcat could not retrieve data.", e);
                return logcatBuf.toString();
            }
        } catch (IOException e3) {
            e = e3;
            Log.e(ACRA.LOG_TAG, "LogCatCollector.collectLogcat could not retrieve data.", e);
            return logcatBuf.toString();
        }
        return logcatBuf.toString();
    }
}
