package io.github.zebalu.aoc2017

import groovy.transform.CompileDynamic

import java.time.Duration
import java.time.Instant
import java.util.function.IntFunction
import java.util.function.LongPredicate
import java.util.function.Predicate
import java.util.regex.Pattern

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.MapConstructor

@CompileStatic
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


    @CompileDynamic
    Day15() {
        def lines = input.lines().toList()
        aStart = (lines[0] =~ PATTERN)[0][1] as int
        bStart = (lines[1] =~ PATTERN)[0][1] as int
    }

    @CompileStatic
    @Override
    protected void solve1() {
        def aa = new AcceptAll()
        def generatorA = new RandomGenerator(aStart, A_MUL, DIV, aa).iterator()
        def generatorB = new RandomGenerator(bStart, B_MUL, DIV, aa).iterator()
        int counter = 0
        40_000_000.times {
            if ((generatorA.next() & 0xffff) == (generatorB.next() & 0xffff)) {
                ++counter
            }
        }
        solution1 = counter
    }

    @CompileStatic
    @Override
    protected void solve2() {
        def generatorA = new RandomGenerator(aStart, A_MUL, DIV, new DividableBy4()).iterator()
        def generatorB = new RandomGenerator(bStart, B_MUL, DIV, new DividableBy8()).iterator()
        int counter = 0
        5_000_000.times {
            if ((generatorA.next() & 0xffff) == (generatorB.next() & 0xffff)) {
                ++counter
            }
        }
        solution2 = counter
    }

    @Canonical
    @MapConstructor(includeFields = true)
    @CompileStatic
    private static final class RandomGenerator implements Iterable<Integer> {
        private final int start
        private final int mul
        private final int div
        private final LongPredicate filter

        RandomGenerator(int start, int mul, int div, LongPredicate filter) {
            this.start = start;
            this.mul = mul;
            this.div = div;
            this.filter = filter;
        }

        @Override
        Iterator<Integer> iterator() {
            long[] num = new long[]{(long) start};
            return new Iterator<Integer>() {
                @Override
                boolean hasNext() {
                    return true;
                }

                @Override
                Integer next() {
                    num[0] = (num[0] * mul) % div;
                    while (!filter.test(num[0])) {
                        num[0] = (num[0] * mul) % div;
                    }
                    return (int) num[0];
                }
            };
        }
    }

    private static final class AcceptAll implements LongPredicate {

        @Override
        boolean test(long value) {
            return true
        }
    }

    private static final class DividableBy4 implements LongPredicate {

        @Override
        boolean test(long value) {
            return (value & 3) == 0
        }

    }

    private static final class DividableBy8 implements LongPredicate {

        @Override
        boolean test(long value) {
            return (value & 7) == 0
        }

    }
}