package io.github.zebalu.aoc2017

import java.util.regex.Pattern

import groovy.transform.Canonical
import groovy.transform.MapConstructor

class Day08 extends AbstractDay {
    
    static void main(String... args) {
        new Day08().solve()
    }
    
    private final List<Instruction> instructions 
    private final Computer computer = new Computer()
    
    Day08() {
        instructions = input.lines().collect { Instruction.read(it) }
        instructions.each { it.execute(computer) }
    }
    
    @Override
    protected void solve1() {
        solution1 = computer.maxRegister
    }
    @Override
    protected void solve2() {
        solution2 = computer.maxValueEver
    }
    
    @Canonical
    private static final class Instruction {
        private static final Pattern PATTERN = ~/([^ ]+) ([^ ]+) ([^ ]+) if ([^ ]+) ([^ ]+) ([^ ]+)/
        
        String register
        String task
        int change
        String dependency
        String compare
        int boundary
        
        void execute(Computer computer) {
            if(!comparea(computer)) {
                return
            }
            switch(task) {
                case 'inc': computer[register] += change; return
                case 'dec': computer[register] -= change; return
                default   : throw new IllegalStateException("unknown task: $task")
            }
        }
        
        private boolean comparea(Computer computer) {
            int depVal = computer[dependency]
            switch(compare) {
                case '==': return depVal == boundary
                case '!=': return depVal != boundary
                case '<=': return depVal <= boundary
                case '>=': return depVal >= boundary
                case '<' : return depVal <  boundary
                case '>' : return depVal >  boundary
                default: throw new IllegalStateException("unknown compare: ${compare}")
            }
        }
        
        static Instruction read(String s) {
            def (_, re,ta,ch,de,co,bo) = (s =~ PATTERN)[0]
            return new Instruction(register: re, task: ta, change: ch as int, dependency: de, compare: co, boundary: bo as int)
        }
    }
    
    private static class Computer {
        Map<String, Integer> registers = new HashMap<>()
        int maxValueEver = Integer.MIN_VALUE
        
        int getAt(String register) {
            return registers.computeIfAbsent(register, {_ -> 0})
        }
        
        void putAt(String register, int value) {
            registers[register] = value
            if(value>maxValueEver) {
                maxValueEver = value
            }
        }
        
        int getMaxRegister() {
            registers.values().max()
        }
    }
}
