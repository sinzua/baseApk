package com.parse;

import java.util.AbstractList;
import java.util.List;

class Lists {

    private static class Partition<T> extends AbstractList<List<T>> {
        private final List<T> list;
        private final int size;

        public Partition(List<T> list, int size) {
            this.list = list;
            this.size = size;
        }

        public List<T> get(int location) {
            int start = location * this.size;
            return this.list.subList(start, Math.min(this.size + start, this.list.size()));
        }

        public int size() {
            return (int) Math.ceil(((double) this.list.size()) / ((double) this.size));
        }
    }

    Lists() {
    }

    static <T> List<List<T>> partition(List<T> list, int size) {
        return new Partition(list, size);
    }
}
