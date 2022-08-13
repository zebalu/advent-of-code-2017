package io.github.zebalu.aoc2017

import groovy.transform.MapConstructor

class Day09 extends AbstractDay {

    static void main(String[] args) {
        new Day09().solve()
    }

    private final Group rootGroup

    Day09() {
        rootGroup = GroupReader.read(input)
    }

    @Override
    protected void solve1() {
        solution1 = rootGroup.totalScore
    }

    @Override
    protected void solve2() {
        solution2 = rootGroup.totalGarbage
    }

    private static class GroupReader {

        static Group read(String data) {
            new GroupReader(data).groups[0]
        }

        private final Iterator<String> iterator
        private boolean inGarbage = false
        private Group current = null
        private List<Group> groups = []

        private GroupReader(String string) {
            iterator = string.iterator()
            while (iterator.hasNext()) {
                String next = iterator.next()
                if(next == '!') {
                    iterator.next()
                } else if (inGarbage) {
                    handleGarbage(next)
                } else {
                    handleGroup(next)
                }
            }
        }

        private void handleGarbage(String next) {
            if(next == '>') {
                inGarbage = false
            } else {
                ++current.garbageLength
            }
        }

        private void handleGroup(String next) {
            if(next == '{') {
                appendNewSubgroup()
            } else if(next == '<') {
                inGarbage = true
            } else if(next == '}') {
                current = current.parent
            }
        }

        private void appendNewSubgroup() {
            Group g = new Group(parent: current)
            if(current) {
                current.subGroups.add(g)
            }
            groups.add(g)
            current = g
        }
    }

    @MapConstructor
    private static class Group {
        Group parent
        List<Group> subGroups = []

        Integer score = null
        int garbageLength = 0

        int getScore() {
            if (!this.score) {
                this.score = parent ? parent.getScore() + 1 : 1
            }
            this.score
        }

        int getTotalScore() {
            getScore() + subGroups.inject(0) { sum, group -> sum + group.getTotalScore() }
        }

        int getTotalGarbage() {
            garbageLength + subGroups.inject(0) { s, g -> s+g.getTotalGarbage() }
        }
    }
}
