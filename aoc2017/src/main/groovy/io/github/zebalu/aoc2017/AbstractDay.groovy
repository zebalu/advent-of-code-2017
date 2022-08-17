package io.github.zebalu.aoc2017

import java.time.Duration
import java.time.Instant

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
    
    final void fancySolve() {
        println ":::::::::: \t ${this.class.simpleName} \t ::::::::::"
        Instant start = Instant.now()
        solve1()
        Instant middle = Instant.now()
        solve2()
        Instant end = Instant.now()
        println solution1
        println solution2
        println ''
        println "solution 1 took:\t ${Duration.between(start, middle).toMillis()} ms"
        println "solution 2 took:\t ${Duration.between(middle, end).toMillis()} ms"
        println "     ${this.class.simpleName} took:\t ${Duration.between(start, end).toMillis()} ms"
        println "========== \t ${this.class.simpleName} \t =========="
    }

    protected final String readInput() {
        String fileName = "${this.class.simpleName.toLowerCase(Locale.ROOT)}.txt"
        File f = new File(fileName)
        if(f.exists()) {
            f.text
        } else {
            this.class.getResourceAsStream("/${fileName}").withReader { it.text }
        }
    }
}
