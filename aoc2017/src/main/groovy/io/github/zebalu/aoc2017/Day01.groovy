package io.github.zebalu.aoc2017

import java.time.Instant

class Day01 extends AbstractDay {
    
    static void main(String[] args) {
        new Day01().solve()
    }
    
    @Override
    protected void solve1() {
        solution1 = (0..(input.size()-1)).collect { i -> input[i] == input[i-1] ? input[i] as int : 0 }.sum()
    }
    
    @Override
    protected void solve2() {
        Instant start
        def half = input.size().intdiv(2)
        solution2 = (0..(input.size()-1)).collect { i -> input[i] == input[i-half] ? input[i] as int : 0 }.sum()
    }
}
