package io.github.zebalu.aoc2017

import java.util.regex.Pattern

import groovy.transform.CompileStatic

@CompileStatic
class Day23 extends AbstractDay {
    
    static void main(String[] args) {
        new Day23().solve()
    }
    
    @Override
    protected void solve1() {
        def commands = input.lines().collect{new Command((String)it)}
        CoProcessor sc = new CoProcessor()
        sc.execute(commands)
        solution1 = sc.mulCounter
    }
    @Override
    protected void solve2() {
        def commands = input.lines().collect{new Command((String)it)}.take(8)
        CoProcessor sc = new CoProcessor()
        sc['a']=1
        sc.execute(commands)
        int nonPrimeCounter = 0
        for(long i=sc['b']; i<=sc['c']; i+=17) {
            if(!isPrime(i)) {
                ++nonPrimeCounter
            }
        }
        solution2 = nonPrimeCounter
    }
    
    public static boolean isPrime(long n) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        for (int i = 3; i <= Math.sqrt(n) + 1; i = i + 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
    
    private static abstract class AbstractExecutor {
        protected final Map<String, Long> registers = new HashMap<>()
        protected int pointer
        boolean blocked = false
        int mulCounter = 0
        final void execute(List<Command> commands) {
            while(pointer < commands.size() && executable) {
                Command c = commands[pointer]
                long v1 = c.literal1?(long)c.reg:this[(String)c.reg]
                long v2 = c.literal2?(long)c.amount:this[(String)c.amount]
                if(c.cmd == 'set') {
                    this[(String)c.reg] = v2
                } else if (c.cmd == 'sub') {
                    this[(String)c.reg] = v1-v2
                } else if(c.cmd == 'mul') {
                    this[(String)c.reg] = v1*v2
                    ++mulCounter
                } else if(c.cmd == 'jnz') {
                    if(v1!=0) {
                        pointer += (int)(v2 - 1)
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
        
    private static class CoProcessor extends AbstractExecutor {
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
    
    private static class Command {
        private static final Pattern PATTERN = ~/^[a-z]+$/
        final String cmd
        final def reg
        final def amount
        final boolean literal1
        final boolean literal2
        Command(String line) {
            def parts = line.split(' ')
            cmd = parts[0]
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
        }
        @Override
        public String toString() {
            "${cmd}\t${reg}\t${amount}"
        }
    }
}
