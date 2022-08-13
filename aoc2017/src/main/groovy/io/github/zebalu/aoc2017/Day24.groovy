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
        State start = new State(new Bridge(lastPort: 0, length: 0, strength: 0), new HashSet<MagneticPiece>(pieces))
        Queue<State> toCheck = new LinkedList<>()
        toCheck.add((State)start)
        strongestBridge = start.bridge
        longestStrongestBridge = start.bridge
        while(toCheck.peek() != null) {
            State curr = (State)toCheck.poll()
            def possibleNexts = curr.possibleExtens()
            if(possibleNexts.isEmpty()) {
                if(strongestBridge.strength<curr.bridge.strength) {
                    strongestBridge = curr.bridge
                }
                if(longestStrongestBridge<curr.bridge) {
                    longestStrongestBridge = curr.bridge
                }
            } else {
                for(n in possibleNexts) {
                    toCheck.add((State)curr.extend(n))
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
        Set<MagneticPiece> unused
        List<MagneticPiece> possibleExtens() {
            def res = new LinkedList<MagneticPiece>()
            for(MagneticPiece p in unused) {
                if(p.hasPort(bridge.lastPort)) {
                    res << (MagneticPiece)p
                }
            }
            return (List<MagneticPiece>)res
        }
        State extend(MagneticPiece next) {
            def cut = new HashSet<MagneticPiece>((Set<MagneticPiece>)unused)
            cut.remove(next)
            return new State(bridge: new Bridge(bridge, next), unused: cut)
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
