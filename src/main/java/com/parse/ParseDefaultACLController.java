package com.parse;

import java.lang.ref.WeakReference;

class ParseDefaultACLController {
    ParseACL defaultACL;
    boolean defaultACLUsesCurrentUser;
    ParseACL defaultACLWithCurrentUser;
    WeakReference<ParseUser> lastCurrentUser;

    ParseDefaultACLController() {
    }

    public void set(ParseACL acl, boolean withAccessForCurrentUser) {
        this.defaultACLWithCurrentUser = null;
        this.lastCurrentUser = null;
        if (acl != null) {
            ParseACL newDefaultACL = acl.copy();
            newDefaultACL.setShared(true);
            this.defaultACL = newDefaultACL;
            this.defaultACLUsesCurrentUser = withAccessForCurrentUser;
            return;
        }
        this.defaultACL = null;
    }

    public ParseACL get() {
        if (this.defaultACLUsesCurrentUser && this.defaultACL != null) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                if ((this.lastCurrentUser != null ? (ParseUser) this.lastCurrentUser.get() : null) != currentUser) {
                    ParseACL newDefaultACLWithCurrentUser = this.defaultACL.copy();
                    newDefaultACLWithCurrentUser.setShared(true);
                    newDefaultACLWithCurrentUser.setReadAccess(currentUser, true);
                    newDefaultACLWithCurrentUser.setWriteAccess(currentUser, true);
                    this.defaultACLWithCurrentUser = newDefaultACLWithCurrentUser;
                    this.lastCurrentUser = new WeakReference(currentUser);
                }
                return this.defaultACLWithCurrentUser;
            }
        }
        return this.defaultACL;
    }
}
