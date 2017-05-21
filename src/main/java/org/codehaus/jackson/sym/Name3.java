package org.codehaus.jackson.sym;

public final class Name3 extends Name {
    final int mQuad1;
    final int mQuad2;
    final int mQuad3;

    Name3(String name, int hash, int q1, int q2, int q3) {
        super(name, hash);
        this.mQuad1 = q1;
        this.mQuad2 = q2;
        this.mQuad3 = q3;
    }

    public boolean equals(int quad) {
        return false;
    }

    public boolean equals(int quad1, int quad2) {
        return false;
    }

    public boolean equals(int[] quads, int qlen) {
        return qlen == 3 && quads[0] == this.mQuad1 && quads[1] == this.mQuad2 && quads[2] == this.mQuad3;
    }
}
