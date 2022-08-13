package io.github.zebalu.aoc2017

import java.util.regex.Pattern

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class Day13 extends AbstractDay {
    static void main(String[] args) {
        new Day13().solve()
    }
    
    private final Configuration conf

    Day13() {
        conf = new Configuration(input)
    }
    @Override
    protected void solve1() {
        solution1 = conf.calculateSeverity()
    }
    @Override
    protected void solve2() {
        int delay = 0
        boolean caught = true
        while(caught) {
            ++delay
            conf.scanners=delay
            caught = conf.isCaughtAnywhere()
        }
        solution2 = delay
    }
    private static class Configuration {
        private static final Pattern PATTERN = ~/^(?<row>\d+): (?<width>\d+)$/
        private final Map rows = [:]
        private final int max
        int scanners = 0
        @CompileDynamic
        Configuration(String desc) {
            desc.lines().each {
                def (_, row, width) = (it =~ PATTERN)[0]
                rows[row as int]=(width as int)
            }
            max = rows.keySet().max()
        }
        int calculateSeverity() {
            int sumSeverity = 0
            int traveler = -1
            while(traveler < max) {
                ++traveler
                if(isScannerIn(traveler)) {
                    sumSeverity += traveler * (int)(rows[traveler])
                }
                ++scanners
            }
            return sumSeverity
        }

        boolean isCaughtAnywhere() {
            int traveler = -1
            while(traveler < max) {
                ++traveler
                if(isScannerIn(traveler)) {
                    return true
                }
                ++scanners
            }
            return false
        }

        private boolean isScannerIn(int depth) {
            rows.containsKey(depth) && positionOf(depth) == 0
        }

        private int positionOf(int scanner) {
            if(!rows.containsKey(scanner)) {
                return -1
            }
            int width = rows[scanner]
            int remains = scanners % ((width-1) * 2 )
            if(remains > width-1) {
                remains -= width
                return (width - remains)-2
            }
            return remains
        }
    }
}
