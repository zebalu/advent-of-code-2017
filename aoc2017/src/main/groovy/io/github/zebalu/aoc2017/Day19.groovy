package io.github.zebalu.aoc2017

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
class Day19 extends AbstractDay {
    static void main(String... args) {
        new Day19().solve()
    }
    private static final java.util.regex.Pattern LETTER = ~/[A-Z]/
    
    private final List<String> matrix
    private int steps = 0
    
    Day19() {
        matrix = input.lines().toList()
    }
    
    @Override
    protected void solve1() {
        Coord position = findStartPosition()
        Coord direction = new Coord(0,1)
        String current = this[position]
        StringBuilder collector = new StringBuilder()
        steps = 0
        while(isWayForward(current)) {
            ++steps
            if(isLetter(current)) {
                collector.append(current)
            } else if(isCrossRoad(current)) {
                direction = findNewDirection(position, direction)
            }
            position += direction
            current = this[position]
        }
        solution1 = collector.toString()
    }
    
    @Override
    protected void solve2() {
        solution2 = steps
    }

    private Coord findStartPosition() {
        Coord position = new Coord(0,0)
        while(!isWay(this[position])) {
            position = position.right
        }
        return position
    }
    
    private Coord findNewDirection(Coord position, Coord direction) {
        def left = this[position + direction.turnLeft()]
        def right = this[position + direction.turnRight()]
        def before = this[position + direction.turnBack()]
        if(isWayForward(left) && left != before) {
            return direction.turnLeft()
        } else if(isWayForward(right) && right != before) {
            return direction.turnRight()
        }
        throw new IllegalStateException("there is no way forward!")
    }
    
    private String getAt(Coord coord) {
        matrix.getAt(coord.y)?.getAt(coord.x)?:'*'
    }
    private boolean isWay(String chr) {
        return chr == '-' || chr == '|'
    }
    private boolean isCrossRoad(String chr) {
        return chr == '+'
    }
    private boolean isLetter(String chr) {
        LETTER.matcher(chr).matches()
    }
    private boolean isWayForward(String chr) {
        return isWay(chr) || isLetter(chr) || isCrossRoad(chr)
    }    
    @Canonical
    static class Coord {
        int x
        int y
        Coord getDown() {
            new Coord(x: x, y: y+1)
        }
        Coord getUp() {
            new Coord (x: x, y: y-1)
        }
        Coord getLeft() {
            new Coord(x: x-1, y: y)
        }
        Coord getRight() {
            new Coord(x: x+1, y: y)
        }
        Coord turnLeft() {
            new Coord(x: -y, y: x)
        }
        Coord turnRight() {
            new Coord(x: y, y: -x)
        }
        Coord turnBack() {
            new Coord(x: -x, y: -y)
        }
        Coord plus(Coord rhs) {
            new Coord(x: x+rhs.x, y: y+rhs.y)
        }
        @Override
        String toString() {
            "[x: $x, y: $y]"
        }
    }
}
