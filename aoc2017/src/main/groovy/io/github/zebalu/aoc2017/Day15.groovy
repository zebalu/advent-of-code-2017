package io.github.zebalu.aoc2017

import groovy.transform.CompileDynamic
import java.util.regex.Pattern
import groovy.transform.CompileStatic

@CompileStatic
class Day15 extends AbstractDay {
    static void main(String... args) {
        new Day15().fancySolve()
    }
    private static final Pattern PATTERN = ~/^Generator . starts with (\d+)$/
    private static final int A_MUL = 16807
    private static final int B_MUL = 48271
    private static final int DIV = Integer.MAX_VALUE //2147483647

    private final int aStart
    private final int bStart


    @CompileDynamic
    Day15() {
        def lines = input.lines().toList()
        aStart = (lines[0] =~ PATTERN)[0][1] as int
        bStart = (lines[1] =~ PATTERN)[0][1] as int
    }

    @CompileStatic
    @Override
    protected void solve1() {
        long a = aStart
        long b = bStart
        int counter = 0
        for(int i=0; i<40_000_000; ++i) {
            a = (a * A_MUL) % DIV
            b = (b * B_MUL) % DIV
            if ((a & 0xffff) == (b & 0xffff)) {
                ++counter
            }
        }
        solution1 = counter
    }

    @CompileStatic
    @Override
    protected void solve2() {
        long a = aStart
        long b = bStart
        int counter = 0
        for(int i=0; i<5_000_000; ++i) {
            do {
                a = (a * A_MUL) % DIV
            } while(( a & 3) != 0)
            do {
                b = (b * B_MUL) % DIV
            } while((b & 7) != 0)
            if ((a & 0xffff) == (b & 0xffff)) {
                ++counter
            }
        }
        solution2 = counter
    }
}