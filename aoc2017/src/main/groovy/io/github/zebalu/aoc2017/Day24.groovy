package io.github.zebalu.aoc2017

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.MapConstructor

@CompileStatic
class Day24 extends AbstractDay {
    static void main(String... args) {
        new Day24().solve()
    }
    private final List<MagneticPiece> pieces
    private Bridge strongestBridge = null
    private Bridge longestStrongestBridge = null
    Day24() {
        pieces = input.lines().collect { (MagneticPiece)new MagneticPiece((String)it) }
    }
    @Override
    protected void solve1() {
        BitSet unused = new BitSet(pieces.size())
        unused.set(0, unused.size(), true)
        State start = new State(new Bridge(lastPort: 0, length: 0, strength: 0), unused)
        Queue<State> toCheck = new LinkedList<>()
        toCheck.add((State)start)
        strongestBridge = start.bridge
        longestStrongestBridge = start.bridge
        while(toCheck.peek() != null) {
            State curr = (State)toCheck.poll()
            def possibleNexts = curr.possibleExtens(pieces)
            if(possibleNexts.isEmpty()) {
                if(strongestBridge.strength<curr.bridge.strength) {
                    strongestBridge = curr.bridge
                }
                if(longestStrongestBridge.compareTo(curr.bridge)<0) {
                    longestStrongestBridge = curr.bridge
                }
            } else {
                for(n in possibleNexts) {
                    toCheck.add((State)curr.extend(n, pieces))
                }
            }
        }
        solution1 = strongestBridge.strength
    }
    @Override
    protected void solve2() {
        solution2 = longestStrongestBridge.strength
    }
    
    @Canonical
    @MapConstructor
    static class State {
        Bridge bridge
        BitSet unused
        List<Integer> possibleExtens(List<MagneticPiece> pieces) {
            def res = new LinkedList<Integer>()
            for(int i=0; i<pieces.size(); ++i) {
                if(unused.get(i) && pieces[i].hasPort(bridge.lastPort)) {
                    res.add(i)
                }
            }
            return res
        }
        State extend(int next, List<MagneticPiece> pieces) {
            BitSet cut = (BitSet) (unused.clone())
            cut.set(next, false)
            return new State(new Bridge(bridge, pieces[next]), cut)
        }
    }
    
    @Canonical
    @MapConstructor
    static class Bridge implements Comparable<Bridge> {
        static final Comparator<Bridge> LENGTH_STRENGTH_COMPARATOR = Comparator.<Bridge>comparingInt({it.length}).thenComparingInt({it.strength})
        final int strength
        final int length
        final int lastPort
        Bridge(Bridge toExtend, MagneticPiece piece) {
            strength = toExtend.strength+piece.value
            length = toExtend.length+1
            lastPort = piece.other(toExtend.lastPort)
        }
        @Override
        public int compareTo(Bridge o) {
            return LENGTH_STRENGTH_COMPARATOR.compare(this, o)
        }
    }
    
    @Canonical
    @MapConstructor
    static final class MagneticPiece {
        int port1
        int port2
        MagneticPiece(String desc) {
            def parts = desc.split("/")
            port1 = parts[0] as int
            port2 = parts[1] as int
        }
        int getValue() {
            return port1+port2
        }
        boolean hasPort(int port) {
            return port1 == port || port2 == port
        }
        int other(int port) {
            if (port1 == port) {
                return port2
            } else {
                return port1
            }
        }
        @Override
        int hashCode() {
            port1*1_000+port2
        }
        @Override
        String toString() {
            "${port1}/${port2}"
        }
    }
}
