package io.github.zebalu.aoc2017

import groovy.transform.CompileStatic

@CompileStatic
class Day17 extends AbstractDay {
    static void main(String[] args) {
        new Day17().solve()
    }
    @Override
    protected void solve1() {
        def cb = new CircularBuffer(input as int)
        cb.fill(2017)
        solution1 = cb.afterOf(2017)
    }
    @Override
    protected void solve2() {
        def cb = new CircularBuffer(input as int)
        solution2 = cb.fakeFill(50_000_000)
    }
    private static final class CircularBuffer {
        private final int stepSize
        private final List<Integer> buffer = new LinkedList<>([0])
        int position = 0
        CircularBuffer(int stepSize) {
            this.stepSize=stepSize
        }
        void fill(int until) {
            until.times {
                int newVal = it + 1
                position = (position + stepSize) % buffer.size() + 1
                if(position>=buffer.size()) {
                    buffer << newVal
                } else {
                    buffer.add(position, newVal)
                }
            }
        }
        int afterOf(int value) {
            def i = buffer.iterator()
            def foundVal = false
            while(i.hasNext() && ! foundVal) {
                foundVal = i.next() == value
            }
            if(i.hasNext()) {
                i.next()
            } else {
                buffer[0]
            }
        }
        int fakeFill(int until) {
            int after0 = -1
            int size = buffer.size()
            int position = position
            until.times {
                int newVal = it + 1
                position = (position + stepSize) % size + 1
                if(position==1) {
                    after0 = newVal
                }
                ++size
            }
            return after0
        }
    }
}
