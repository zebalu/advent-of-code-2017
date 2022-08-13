package io.github.zebalu.aoc2017

import java.util.function.Function
import java.util.function.Predicate

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
class Day03 extends AbstractDay {

    public static void main(String[] args) {
        new Day03().solve()
    }

    private final int inputNumber

    Day03() {
        inputNumber = input as int
    }

    @Override
    protected void solve1() {
        Map<Coord, Integer> stepMatrix = createMatrix(1, 2)
        def last = fill(stepMatrix, {c->stepMatrix.size()}, {c->stepMatrix[c]<inputNumber-1})
        solution1 = last.x.abs()+last.y.abs()
    }

    @Override
    protected void solve2() {
        Map<Coord, Integer> sumMatrix = createMatrix(1, 1)
        def last = fill(sumMatrix, {c->sumOf(c.adjecents(), sumMatrix)}, {c->sumMatrix[c]<inputNumber})
        solution2 = sumMatrix[last]
    }
    
    private Map<Coord, Integer> createMatrix(int firstValue, int secondValue) {
        Map<Coord, Integer> matrix = new HashMap<>()
        matrix.put(new Coord(0, 0), firstValue)
        matrix.put(new Coord(1, 0), secondValue)
        return matrix
    }

    private Coord fill(Map<Coord, Integer> matrix, Function<Coord, Integer> nextValue, Predicate<Coord> check) {
        Coord current = new Coord(1, 0)
        Coord walk = new Coord(0, 1)
        Coord left = walk.turnLeft()
        while(check(current)) {
            if(!matrix.containsKey(current+left)) {
                walk = left
                left = walk.turnLeft()
            }
            current = current + walk
            int lastValue = nextValue(current)
            matrix.put(current, lastValue)
        }
        return current
    }

    private int sumOf(List<Coord> adjecents, Map<Coord, Integer> matrix) {
        (int) adjecents.collect { matrix.getOrDefault(it, 0) }.sum()
    }

    @Canonical
    private static final class Coord {
        int x
        int y

        Coord turnLeft() {
            return new Coord(-y, x)
        }

        Coord plus(Coord c) {
            return new Coord(x+c.x, y+c.y)
        }

        List<Coord> adjecents() {
            [
                new Coord( x  , y+1 ),
                new Coord( x+1, y+1 ),
                new Coord( x+1, y   ),
                new Coord( x+1, y-1 ),
                new Coord( x  , y-1 ),
                new Coord( x-1, y-1 ),
                new Coord( x-1, y   ),
                new Coord( x-1, y+1 )
            ]
        }

        @Override
        int hashCode() {
            x * 1_000_000 + y
        }
    }
}
