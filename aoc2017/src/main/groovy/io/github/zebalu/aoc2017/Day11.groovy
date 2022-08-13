package io.github.zebalu.aoc2017

import groovy.transform.Canonical

class Day11 extends AbstractDay {

    static void main(String[] args) {
        new Day11().solve()
    }

    @Override
    protected void solve1() {
        Coord coord = new Coord(0,0)
        for(d in input.split(',')) {
            coord = coord.move(d)
        }
        solution1 = coord.distance
    }
    @Override
    protected void solve2() {
        int max = Integer.MIN_VALUE
        Coord coord = new Coord(0,0)
        for(d in input.split(',')) {
            coord = coord.move(d)
            int distance = coord.distance
            if(distance>max) {
                max=distance
            }
        }
        solution2 = max
    }

    @Canonical
    private static class Coord {
        int x
        int y
        Coord move(String direction) {
            switch (direction) {
                case 'n' : return new Coord(x  , y+2)
                case 's' : return new Coord(x  , y-2)
                case 'ne': return new Coord(x+1, y+1)
                case 'nw': return new Coord(x-1, y+1)
                case 'sw': return new Coord(x-1, y-1)
                case 'se': return new Coord(x+1, y-1)
                default  : throw  new IllegalArgumentException("Unknown direction: ${direction}")
            }
        }

        int getDistance() {
            (x.abs()+y.abs()).intdiv(2)
        }
    }
}
