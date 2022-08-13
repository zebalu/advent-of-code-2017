package io.github.zebalu.aoc2017

class Day04 extends AbstractDay {
    
    @Override
    protected void solve1() {
        solution1 = input.lines().findAll { allUnique(it) }.size()
    }
    @Override
    protected void solve2() {
        solution2 = input.lines().findAll { allAnagrammUnique(it) }.size()
    }
    
    private boolean allUnique(String line) {
        def parts = line.split()
        def unique = parts as Set
        unique.size() == parts.size()
    }
    
    private boolean allAnagrammUnique(String line) {
        def parts = line.split().collect { it.chars().sorted().collect{ "$it" }.join('') }
        def unique = parts as Set
        unique.size() == parts.size()
    }
}
