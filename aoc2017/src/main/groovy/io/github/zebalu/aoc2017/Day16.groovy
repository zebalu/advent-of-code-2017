package io.github.zebalu.aoc2017

import java.time.Duration
import java.time.Instant

class Day16 extends AbstractDay {
    static void main(String[] args) {
        new Day16().solve()
    }
    
    private final List<Command> commands
    private final Dancers dancers
    Day16() {
        dancers = new Dancers()
        commands = input.split(',').collect { new Command(it) }
    }
    @Override
    protected void solve1() {
        commands.each { it.apply(dancers) }
        solution1 = dancers.toSolution()
    }
    @Override
    protected void solve2() {
        int steps = 1
        while(dancers.dancers != Dancers.DEFAULT_DANCERS) { 
            commands.each { it.apply(dancers) }
            ++steps
        }
        int remaining = ((1_000_000_000-steps) % steps)
        remaining.times { 
            commands.each { it.apply(dancers) }
        }
        solution2 = dancers.toSolution()
    }
    
    static final class Dancers {
        private static final List<String> DEFAULT_DANCERS = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p']
        List<String> dancers =  new ArrayList<>(DEFAULT_DANCERS)
        int pointer = 0
        void spin(int move) {
            pointer -= move
        }
        void swapByName(String from, String to) {
            swapByAbsoluteIndex(dancers.indexOf(from), dancers.indexOf(to))
        }
        void swapByRelativeIndex(int from, int to) {
            swapByAbsoluteIndex((from+pointer)%dancers.size(), (to+pointer)%dancers.size())
        }
        String toSolution() {
            StringBuilder sb = new StringBuilder()
            for(int i=0; i<dancers.size(); ++i) {
                sb.append(dancers[(i+pointer)%dancers.size()])
            }
            sb.toString()
        }
        private void swapByAbsoluteIndex(int f, int t) {
            String temp = dancers[f]
            dancers[f]=dancers[t]
            dancers[t]=temp
        }        
    }
    
    private static class Command {
        final String type
        final def from
        final def to
        Command(String desc) {
            if(desc.startsWith('s')) {
                type = 's'
                from = desc.substring(1) as int
                to = null
            } else if(desc.startsWith('x')) {
                type = 'x'
                def parts = desc.substring(1).split('/')
                from = parts[0] as int
                to = parts[1] as int 
            } else if(desc.startsWith('p')) {
                type = 'p'
                def parts = desc.substring(1).split('/')
                from = parts[0]
                to = parts[1]
            } else {
                throw new IllegalArgumentException("can not parse command: '${desc}'")
            }
        }
        void apply(Dancers dancers) {
            if(type == 's') {
                dancers.spin((int)from)
            } else if(type == 'p') {
                dancers.swapByName((String)from, (String)to)
            } else if(type == 'x') {
                dancers.swapByRelativeIndex((int)from, (int)to)
            }
        }
    } 
}
