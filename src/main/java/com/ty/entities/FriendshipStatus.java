package com.ty.entities;

public class FriendshipStatus {
    private boolean following;
    private boolean incoming_request;
    private boolean is_private;
    private boolean outgoing_request;

    public boolean isFollowing() {
        return this.following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isIncoming_request() {
        return this.incoming_request;
    }

    public void setIncoming_request(boolean incoming_request) {
        this.incoming_request = incoming_request;
    }

    public boolean isOutgoing_request() {
        return this.outgoing_request;
    }

    public void setOutgoing_request(boolean outgoing_request) {
        this.outgoing_request = outgoing_request;
    }

    public boolean is_private() {
        return this.is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }
}
