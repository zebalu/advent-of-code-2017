package io.github.zebalu.aoc2017

import java.time.Duration
import java.time.Instant
import java.util.function.Predicate
import java.util.regex.Pattern

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.MapConstructor

class Day15 extends AbstractDay {
    static void main(String... args) {
        new Day15().solve()
    }
    private static final Pattern PATTERN = ~/^Generator . starts with (\d+)$/
    private static final int A_MUL = 16807
    private static final int B_MUL = 48271
    private static final int DIV = Integer.MAX_VALUE //2147483647

    private final int aStart
    private final int bStart


    Day15() {
        def lines = input.lines().toList()
        aStart = (lines[0] =~ PATTERN)[0][1] as int
        bStart = (lines[1] =~ PATTERN)[0][1] as int
    }

    @CompileStatic
    @Override
    protected void solve1() {
        def generatorA = new RandomGenerator(start: aStart, mul: A_MUL, div: DIV, filter: { true }).iterator()
        def generatorB = new RandomGenerator(start: bStart, mul: B_MUL, div: DIV, filter: { true }).iterator()
        int counter = 0
        40_000_000.times {
            if((generatorA.next() & 0xffff) == (generatorB.next() & 0xffff)) {
                ++counter
            }
        }
        solution1 = counter
    }
    @CompileStatic
    @Override
    protected void solve2() {
        def generatorA = new RandomGenerator(start: aStart, mul: A_MUL, div: DIV, filter: { (Long)it % 4 == 0 }).iterator()
        def generatorB = new RandomGenerator(start: bStart, mul: B_MUL, div: DIV, filter: { (Long)it % 8 == 0 }).iterator()
        int counter = 0
        5_000_000.times {
            if((generatorA.next() & 0xffff) == (generatorB.next() & 0xffff)) {
                ++counter
            }
        }
        solution2 = counter
    }

    @Canonical
    @MapConstructor(includeFields=true)
    @CompileStatic
    private static final class RandomGenerator implements Iterable<Integer> {
        private final int start
        private final int mul
        private final int div
        private final Predicate<Long> filter

        @Override
        Iterator<Integer> iterator() {
            long[] num = [(long)start]
            return new Iterator<Integer>() {
                        @Override
                        boolean hasNext() {
                            return true;
                        }
                        @Override
                        public Integer next() {
                            num[0] = (num[0] * mul) % div
                            while(!filter.test(num[0])) {
                                num[0] = (num[0] * mul) % div
                            }
                            return (int) num[0]
                        }
                    }
        }
    }
}
