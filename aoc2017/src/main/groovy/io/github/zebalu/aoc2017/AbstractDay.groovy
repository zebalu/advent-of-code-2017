package io.github.zebalu.aoc2017

abstract class AbstractDay {
    protected final String input
    
    protected AbstractDay() {
        input = readInput()
    }
    
    protected String solution1
    protected String solution2

    final String getSolution1() {
        solution1
    }
    final String getSolution2() {
        solution2
    }

    protected abstract void solve1()
    protected abstract void solve2()

    final void solve() {
        solve1()
        println solution1
        solve2()
        println solution2
    }

    protected final String readInput() {
        this.class.getResourceAsStream("/${this.class.simpleName.toLowerCase(Locale.ROOT)}.txt").withReader { it.text }
    }
}
