package io.github.zebalu.aoc2017

import java.util.function.Function

import groovy.transform.CompileStatic

import java.util.function.IntFunction

@CompileStatic
class Day05 extends AbstractDay {
    @Override
    protected void solve1() {
        int[] instructions = input.lines().collect { it as int } as int[]
        solution1 = countSteps(instructions, (i) -> i+1 )
    }
    
    @Override
    protected void solve2() {
        int[] instructions = input.lines().collect { it as int } as int[]
        solution2 = countSteps(instructions, (i ) -> 3<=i ? i-1 : i+1 )
    }
    
    private int countSteps(int[] instructions, IntFunction<Integer> newValue) {
        int steps = 0
        int pointer = 0
        while(0 <= pointer && pointer < instructions.size() ) {
            int current = instructions[pointer]
            int np = pointer + instructions[pointer]
            instructions[pointer] = (Integer) newValue(current)
            ++steps
            pointer = np
        }
        return steps
    }
}
