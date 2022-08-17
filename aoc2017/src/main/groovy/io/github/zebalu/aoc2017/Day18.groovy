package io.github.zebalu.aoc2017

import java.util.regex.Pattern

import groovy.transform.CompileStatic

@CompileStatic
class Day18 extends AbstractDay {
    static void main(String[] args) {
        new Day18().solve()
    }
    private final List<Command> commands
    Day18() {
        commands = (List<Command>) input.lines().collect { new Command((String)it) }
        
    }
    @Override
    protected void solve1() {
        SoundCard soundCard = new SoundCard()
        soundCard.execute(commands)
        solution1 = soundCard.recovered.first()
    }
    @Override
    protected void solve2() {
        List<Long> ch1 = []
        List<Long> ch2 = []
        Messenger msg1 = new Messenger(0, ch1, ch2)
        Messenger msg2 = new Messenger(1, ch2, ch1)
        while(msg1.executable || msg2.executable) {
            msg1.execute(commands)
            msg2.execute(commands)
        }
        solution2 = msg2.sendCounter 
    }
    
    private static abstract class AbstractExecutor {
        protected final Map<String, Long> registers = new HashMap<>()
        protected int pointer
        boolean blocked = false
        final void execute(List<Command> commands) {
            while(pointer < commands.size() && executable) {
                Command c = commands[pointer]
                if(c.triplet) {
                    long v1 = c.literal1?(long)c.reg:this[(String)c.reg]
                    long v2 = c.literal2?(long)c.amount:this[(String)c.amount]
                    if(c.cmd == 'set') {
                        this[(String)c.reg] = v2
                    } else if (c.cmd == 'add') {
                        this[(String)c.reg] = v1+v2
                    } else if(c.cmd == 'mul') {
                        this[(String)c.reg]=v1*v2
                    } else if(c.cmd == 'mod') {
                        this[(String)c.reg] = v1 % v2
                    } else if(c.cmd == 'jgz') {
                        if(v1>0) {
                            pointer += (int)(v2 - 1)
                        }
                    }
                } else {
                    long v1 = c.literal1 ? (long) c.reg : this[(String)c.reg]
                    if(c.cmd == 'snd') {
                        snd(v1)
                    } else if(c.cmd == 'rcv') {
                        this[(String)c.reg]=rcv(v1)
                    }
                }
                ++pointer
            }
        }
        
        protected abstract void snd(long value)
        protected abstract long rcv(long value)
        protected abstract boolean isExecutable()
        
        long getAt(String register) {
            registers.computeIfAbsent(register, { 0L })
        }
        void putAt(String register, long value) {
            registers[register] = value
        }
    }
    
    private static class SoundCard extends AbstractExecutor {
        private final List<Long> played = new LinkedList<>()
        final List<Long> recovered = new LinkedList<>()
        @Override
        protected void snd(long value) {
            played << value
        }
        @Override
        protected long rcv(long value) {
            if(value != 0) {
                blocked = true
                recovered << played.last()
                played.last()
            } else {
                value
            }
        }
        @Override
        protected boolean isExecutable() {
            return !blocked
        }
        long getAt(String register) {
            registers.computeIfAbsent(register, { 0L })
        }
        void putAt(String register, long value) {
            registers[register] = value
        }
    }
    
    private static class Messenger extends AbstractExecutor {
        private final List<Long> outgoing
        private final List<Long> incoming
        int sendCounter = 0
        int receiveCounter = 0
        final long id
        
        Messenger(long pinit, List<Long> incoming, List<Long> outgoing) {
            this['p'] = pinit
            this.incoming = incoming
            this.outgoing = outgoing
            id=pinit
        }
        
        @Override
        protected long rcv(long value) {
            if(incoming.isEmpty()) {
                blocked = true
                --pointer
                (long)value
            } else {
                blocked = false
                ++receiveCounter
                (Long)incoming.removeAt(0)
            }
        }
        @Override
        protected void snd(long value) {
            outgoing << (long)value
            ++sendCounter
        }
        @Override
        boolean isExecutable() {
            !blocked || !incoming.isEmpty()
        }
    }
    
    private static class Command {
        private static final Pattern PATTERN = ~/^[a-z]+$/
        final String cmd
        final def reg
        final def amount
        final boolean literal1
        final boolean literal2
        final boolean triplet
        Command(String line) {
            def parts = line.split(' ')
            cmd = parts[0]
            triplet = parts.size() == 3
            if(triplet) {
                if(PATTERN.matcher(parts[1]).matches()) {
                    reg = parts[1]
                    literal1=false
                } else {
                    reg = parts[1] as int
                    literal1=true
                }
                if(PATTERN.matcher(parts[2]).matches()) {
                    amount = parts[2]
                    literal2 = false
                } else {
                    amount = parts[2] as int
                    literal2 = true
                }
            } else {
                if(PATTERN.matcher(parts[1]).matches()) {
                    reg = parts[1]
                    literal1 = false
                } else {
                    reg = parts[1] as int
                    literal1 = true
                }
                literal2=false
                amount = null
            }
        }
    }
}
