package org.codehaus.jackson.sym;

public final class Name1 extends Name {
    static final Name1 sEmptyName = new Name1("", 0, 0);
    final int mQuad;

    Name1(String name, int hash, int quad) {
        super(name, hash);
        this.mQuad = quad;
    }

    static final Name1 getEmptyName() {
        return sEmptyName;
    }

    public boolean equals(int quad) {
        return quad == this.mQuad;
    }

    public boolean equals(int quad1, int quad2) {
        return quad1 == this.mQuad && quad2 == 0;
    }

    public boolean equals(int[] quads, int qlen) {
        return qlen == 1 && quads[0] == this.mQuad;
    }
}
