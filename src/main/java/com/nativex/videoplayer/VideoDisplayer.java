package com.nativex.videoplayer;

interface VideoDisplayer {
    void close();

    void displaySecondsRemaining(int i);

    void displaySecondsRemainingUntilSkippable(int i);

    void enableSkip(boolean z);

    void initializeVideo(float f);

    void isStuck(boolean z);
}
