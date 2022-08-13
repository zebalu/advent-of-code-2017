package io.github.zebalu.aoc2017

class Day10 extends AbstractDay {

    static void main(String[] args) {
        new Day10().solve()
    }
    
    @Override
    protected void solve1() {
        SingleKnotHash skh = new SingleKnotHash(new ArrayList<>(0..255 as List<Integer>), input.split(',').collect { it as int })
        skh.execute()
        solution1 = skh.data[0] * skh.data[1]
    }
    @Override
    protected void solve2() {
        FullKnotHash fkh = new FullKnotHash(new ArrayList<>(0..255 as List<Integer>), input)
        fkh.execute()
        solution2 = fkh.toHex()
    }

    private void reverse(int from, int to) {
        int upper = to-1
        for(int i=from; i<upper;) {
            swap(i%data.size(), upper%data.size())
            ++i
            --upper
        }
    }

    private void swap(int i, int j) {
        def temp = data[i]
        data[i] = data[j]
        data[j] = temp
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
        FullKnotHash(List<Integer> data, String lengthString) {
            skh = new SingleKnotHash(data, lengthString.toCharArray().collect { it as int } + [17, 31, 73, 47, 23])
        }

        void execute() {
            64.times { skh.execute() }
        }

        String toHex() {
            List<Integer> xored = xorData()
            convertToHexadecimalString(xored)
        }

        private convertToHexadecimalString(List xored) {
            xored.collect { String.format("%02x", it) }.join('')
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
}
