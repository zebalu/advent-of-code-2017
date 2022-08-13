package io.github.zebalu.aoc2017

class Day02 extends AbstractDay {

    static void main(String[] args) {
        new Day02().solve()
        int a = 5
        int b = 2
    }

    private final List<List<Integer>> matrix;

    Day02() {
        matrix = input.lines().map { line ->
            line.split().findAll { !it.isEmpty() }
            .collect{
                it as int
            }
        }.toList()
    }

    @Override
    protected void solve1() {
        solution1 = matrix.collect { it.max() - it.min() }.sum()
    }

    @Override
    protected void solve2() {
        solution2 = matrix.collect { onlyEvenDivOf(it) }.sum()
    }

    private int onlyEvenDivOf(List<Integer> row) {
        for(int i = 0; i < row.size()-1; ++i) {
            for(int j = i + 1; j < row.size(); ++j) {
                if(row[i] % row[j] == 0 || row[j] % row[i] == 0) {
                    return row[i] > row[j] ? row[i].intdiv(row[j]) : row[j].intdiv(row[i])
                }
            }
        }
        throw new IllegalStateException("this row: ${row} does not fit description")
    }
}
