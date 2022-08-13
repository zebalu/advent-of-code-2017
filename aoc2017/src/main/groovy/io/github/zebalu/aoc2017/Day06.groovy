package io.github.zebalu.aoc2017

class Day06 extends AbstractDay {
    
    static void main(String[] args) {
        new Day06().solve()
    }
    
    private List<Integer> banks
    private Set<String> states = new HashSet<>()
    private List<String> stateOrder = new LinkedList<>()
    private String state
    private int steps;
    
    @Override
    protected void solve1() {
        int[] banks = input.split().findAll{!it.isEmpty()}.collect { it as int }
        state = toState(banks)
        states.add(state)
        boolean loop = false
        while(!loop) {
            int max = findToSpread(banks)
            max = spread(banks, max)
            state = toState(banks)
            loop = !states.add(state)
            stateOrder.add(state)
            ++steps
        }
        solution1 = steps
    }

    @Override
    protected void solve2() {
        solution2 = steps - 1 - stateOrder.indexOf(state)
    }
    
    private int spread(int[] banks, int max) {
        int toSpread = banks[max]
        banks[max] = 0
        for(int i=1; i<=toSpread; ++i) {
            ++banks[(max+i) % banks.size()]
        }
        return max
    }

    private int findToSpread(int[] banks) {
        int max = 0
        for(int i=1; i<banks.size(); ++i) {
            if(banks[max] < banks[i]) {
                max = i
            }
        }
        return max
    }

    
    private String toState(int[] banks) {
        return banks.collect { "$it" }.join(' ')
    }
}
