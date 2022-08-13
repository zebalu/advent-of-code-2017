package io.github.zebalu.aoc2017

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
class Day25 extends AbstractDay {
    static void main(String[] args) {
        new Day25().solve()
    }
    @Override
    protected void solve1() {
        def lines = input.lines().toList()
        String initState = lines[0].split(' ')[3].substring(0, 1)
        int times = lines[1].split(' ')[5] as int
        List<List<String>> blocks = []
        List<String> block = []
        lines.drop(3).each { 
            if(it.size()==0) {
                blocks << (List<String>)block
                block = []
            } else {
                block << (String)it
            }
        }
        blocks << block
        Program program = new Program(blocks)
        TuringMachine turingMachine = new TuringMachine(state: initState)
        turingMachine.execute(program, times)
        solution1 = turingMachine.checkSum
    }
    @Override
    protected void solve2() {
        solution2 = ""
    }
    @Canonical
    static class TuringMachine {
        int position = 0
        String state
        Tape tape = new Tape()
        void execute(Program program, int times) {
            times.times { 
                program.executeOn(this)
            }
        }
        int getCurrent() {
            return (int)tape[position]
        }
        int setCurrent(int newValue) {
            tape[(int)position] = (int)newValue
        }
        void setMove(int value) {
            position+=value
        }
        int getCheckSum() {
            return tape.sumOnes()
        }
        @Override
        String toString() {
            "$position \t $state \t ${tape[position]}"
        }
    }
    @Canonical
    static class Program {
        Map<String, StateTransfer> nextStepMap = new HashMap<>()
        Program(List<List<String>> descBlocks) {
            for(block in descBlocks) {
                String state = block[0].split(' ')[2].substring(0, 1)
                nextStepMap[state] = new StateTransfer(block)
            }
        }
        StateTransfer getAt(String state) {
            return (StateTransfer)nextStepMap[state]
        }
        void executeOn(TuringMachine turinMachine) {
            def transfer = nextStepMap[turinMachine.state]
            def chs = transfer.getFor(turinMachine.current)
            turinMachine.current = chs.write
            turinMachine.move = chs.move
            turinMachine.state = chs.state
        }
    }
    @Canonical
    static class StateTransfer {
        Changes on0
        Changes on1
        StateTransfer(List<String> block) {
            on0 = new Changes(block.subList(2, 5))
            on1 = new Changes(block.drop(6))
        }
        Changes getFor(int value) {
            if(value == 0) {
                return on0
            } else {
                return on1
            }
        }
    }
    @Canonical
    static class Changes {
        int write
        int move
        String state
        Changes(List<String> chs) {
            write = chs[0].split('Write the value ')[1].substring(0, 1) as int
            move = chs[1].split('Move one slot to the ')[1] == "right." ? 1 : -1
            state = chs[2].split('Continue with state ')[1].substring(0, 1)
        }
    }
    @Canonical
    static class Tape {
        private Set<Integer> tape = new HashSet<>()
        int getAt(int position) {
            tape.contains(position) ? 1 : 0
        }
        void putAt(int position, int value) {
            if(value == 0) {
                tape.remove(position)
            } else {
                tape.add((int)position)
            }
        }
        int sumOnes() {
            return tape.size()
        }
    }
}
