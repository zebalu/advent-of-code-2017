package io.github.zebalu.aoc2017

import groovy.transform.Canonical
import io.github.zebalu.aoc2017.Day10.SingleKnotHash

class Day14 extends AbstractDay {
    static void main(String[] args) {
        new Day14().solve()
    }
    private final List<String> matrix;
    Day14() {
        matrix = (0..127 as List).collect { calcBinaryKnotHash("${input}-${it}") }
    }

    private String calcBinaryKnotHash(String key) {
        FullKnotHash fkh = new FullKnotHash(key)
        fkh.execute()
        fkh.toBin()
    }
    @Override
    protected void solve1() {
        solution1 = matrix.collect { it.toCharArray().findAll { it == '1' }.size() }.sum()
    }
    @Override
    protected void solve2() {
        solution2 = findRegions().size()
    }

    private List<Set<Coord>> findRegions() {
        Set<Coord> visited = new HashSet<>()
        List<Set<Coord>> groups = new LinkedList<>()
        for(int y=0; y<matrix.size(); ++y) {
            for(int x=0; x<matrix[y].size(); ++x) {
                Coord c = new Coord(x,y)
                if(!visited.contains(c)) {
                    if(isOne(c)) {
                        groups.add(walkGroup(c, visited))
                    } else {
                        visited.add(c)
                    }
                }
            }
        }
        return groups
    }

    private boolean isOne(Coord coord) {
        return coord.y>=0 && coord.y<matrix.size() && coord.x>=0 && coord.x<matrix[coord.y].size() && matrix[coord.y][coord.x] == '1'
    }

    private Set<Coord> walkGroup(Coord c, Set<Coord> visited) {
        List<Coord> toCheck = new LinkedList<>([c])
        Set<Coord> found = new HashSet<>()
        while(!toCheck.isEmpty()) {
            Coord next = toCheck.removeAt(0)
            if(!visited.contains(next) && isOne(next)) {
                next.adjecents.findAll{ isOne(it) && !visited.contains(it) }.each { toCheck.add(it) }
                found.add(next)
            }
            visited.add(next)
        }
        return found
    }

    private static class SingleKnotHash {
        final List<Integer> data
        final List<Integer> lengths
        int position = 0
        int skip = 0
        SingleKnotHash(List<Integer> data, List<Integer> lengths) {
            this.data = data
            this.lengths = lengths
        }

        void execute() {
            for(length in lengths) {
                reverse(position, position+length)
                position+=length+skip
                ++skip
            }
        }

        private void reverse(int from, int to) {
            int upper = to-1
            int lower = from
            while(lower < upper) {
                swap(lower%data.size(), upper%data.size())
                ++lower
                --upper
            }
        }

        private void swap(int i, int j) {
            def temp = data[i]
            data[i] = data[j]
            data[j] = temp
        }
    }

    private static class FullKnotHash {
        final SingleKnotHash skh
        FullKnotHash(String lengthString) {
            skh = new SingleKnotHash(new ArrayList<>(0..255 as List<Integer>), lengthString.toCharArray().collect { it as int } + [17, 31, 73, 47, 23])
        }

        void execute() {
            64.times { skh.execute() }
        }

        String toHex() {
            List<Integer> xored = xorData()
            convertToHexadecimalString(xored)
        }

        String toBin() {
            List<Integer> xored = xorData()
            convertToBinaryString(xored)
        }

        private convertToHexadecimalString(List xored) {
            xored.collect { String.format("%02x", it) }.join('')
        }
        private convertToBinaryString(List xored) {
            xored.collect { String.format("%08d", new BigInteger("$it").toString(2) as int) }.join('')
        }

        private List xorData() {
            List<Integer> xored = new ArrayList<>()
            for(int i=0; i<skh.data.size(); i+=16) {
                int base = skh.data[i]
                for(int j= i+1; j<i+16; ++j) {
                    base^=skh.data[j]
                }
                xored.add(base)
            }
            return xored
        }
    }

    @Canonical
    private static class Coord {
        int x
        int y
        
        @Override
        int hashCode() {
            return (x<<10)+y
        }
        
        @Override
        String toString() {
            "[${x}, ${y}]"
        }

        List<Coord> getAdjecents() {
            return [
                new Coord(x-1, y),
                new Coord(x, y+1),
                new Coord(x+1, y),
                new Coord(x, y-1)
            ]
        }
    }
}
