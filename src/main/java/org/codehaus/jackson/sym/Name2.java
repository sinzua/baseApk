package org.codehaus.jackson.sym;

public final class Name2 extends Name {
    final int mQuad1;
    final int mQuad2;

    Name2(String name, int hash, int quad1, int quad2) {
        super(name, hash);
        this.mQuad1 = quad1;
        this.mQuad2 = quad2;
    }

    public boolean equals(int quad) {
        return false;
    }

    public boolean equals(int quad1, int quad2) {
        return quad1 == this.mQuad1 && quad2 == this.mQuad2;
    }

    public boolean equals(int[] quads, int qlen) {
        return qlen == 2 && quads[0] == this.mQuad1 && quads[1] == this.mQuad2;
    }
}
