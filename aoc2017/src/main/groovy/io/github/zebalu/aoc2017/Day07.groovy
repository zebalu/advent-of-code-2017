package io.github.zebalu.aoc2017

import java.util.regex.Pattern

import groovy.transform.Canonical
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class Day07 extends AbstractDay {

    static void main(String... args) {
        new Day07().solve()
    }
    
    private Map<String, Node> tree

    Day07() {
        Map<String, Node> raw = Map.ofEntries(
            input.lines().collect { Node.read((String)it) }.collect { Map.entry(it.name, it) }
            .toArray(new Map.Entry<String, Node>[0])
            )
        raw.values().each { n -> n.depNames.each { n.addDep(raw[it]) } }
        tree=raw
    }

    @Override
    protected void solve1() {
        solution1 = tree.values().find { it.root }.name
    }
    @Override
    protected void solve2() {
        Node current = tree[solution1]
        while(current.hasSubWeightDifference()) {
            current = current.findInbalancedSub()
        }
        solution2 = current.correctedWeight
    }

    @Canonical
    private static class Node {
        private final static Pattern PATTERN = ~/^(?<name>[a-z]+) \((?<weight>\d+)\)(?<deps> -> [a-z, ]+)?$/
        String name
        int weight
        List<String> depNames = []
        List<Node> deps = []
        Node parent
        Integer fullWeight
        boolean hasDeps() {
            return !depNames.isEmpty()
        }
        boolean isRoot() {
            return parent == null
        }
        void addDep(Node node) {
            deps.add(node)
            node.parent=this
        }
        @CompileDynamic
        int getFullWeight () {
            if(fullWeight == null) {
                fullWeight = weight + deps.inject(0) { int sum, Node node -> (int)sum + (int)(node.getFullWeight())  }
            }
            return fullWeight
        }
        boolean hasSubWeightDifference() {
            (deps.collect { it.fullWeight } as Set).size() > 1
        }
        Node findInbalancedSub() {
            Map<Integer, List<Node>> stat = [:]
            deps.each { stat.computeIfAbsent(it.fullWeight, {_ -> []}).add(it) }
            stat.values().find { it.size() == 1 }[0]
        }
        int getCorrectedWeight() {
            weight - (fullWeight - parent.deps.find { it.name != name }.fullWeight)
        }
        @CompileDynamic
        static Node read(String nodeStr) {
            def (_, name, weight, rawDeps) = (nodeStr =~ PATTERN)[0]
            new Node(name: name, weight: weight as int, depNames: cutDeps(rawDeps), deps: [], parent: null)
        }
        @CompileDynamic
        static List<String> cutDeps(String deps) {
            if(deps == null) {
                []
            } else {
                deps.substring(' -> '.size()).split(', ')
            }
        }
    }
}
