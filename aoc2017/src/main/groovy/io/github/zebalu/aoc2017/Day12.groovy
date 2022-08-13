package io.github.zebalu.aoc2017

import java.util.regex.Pattern

class Day12 extends AbstractDay {
    static void main(String[] args) {
        new Day12().solve()
    }

    private static final Pattern PATTERN = ~/^(?<id>\d+) <-> (?<connectedTo>.*)$/

    private final Map<Integer, Set<Integer>> graph = new HashMap<>()

    Day12() {
        input.lines().each {
            def (_, id, connectedTo) = (it =~ PATTERN)[0]
            graph[id as int] = (connectedTo.split(', ').collect { it as int } as Set)
        }
    }

    @Override
    protected void solve1() {
        Set<Integer> toCheck = new HashSet<>()
        Set<Integer> visited = new HashSet<>()
        Set<Integer> available = new HashSet<>()
        toCheck.add(0)
        while(!toCheck.isEmpty()) {
            Set<Integer> iterate = new HashSet<>(toCheck)
            toCheck.clear()
            for(n in iterate) {
                if(!visited.contains(n)) {
                    available.add(n)
                    visited.add(n)
                    def newNodes = graph[n]
                    for(i in newNodes) {
                        if(!visited.contains(i)) {
                            toCheck.add(i)
                        }
                    }
                }
            }
        }
        solution1 = available.size()
    }
    @Override
    protected void solve2() {
        def graphCopy = new HashMap<>(graph) 
        Set<Set<Integer>> groups = new HashSet<>()
        while(!graphCopy.isEmpty()) {
            int id = graphCopy.keySet().iterator().next();
            def group = findGroupOf(id, graphCopy)
            graphCopy.removeAll { k,v -> group.contains(k) }
            groups.add(group)
        }
        solution2 = groups.size()
    }
    
    private static Set<Integer> findGroupOf(int id, Map<Integer, Set<Integer>> graph) {
        Set<Integer> toCheck = new HashSet<>()
        Set<Integer> visited = new HashSet<>()
        Set<Integer> available = new HashSet<>()
        toCheck.add(id)
        while(!toCheck.isEmpty()) {
            Set<Integer> iterate = new HashSet<>(toCheck)
            toCheck.clear()
            for(n in iterate) {
                if(!visited.contains(n)) {
                    available.add(n)
                    visited.add(n)
                    def newNodes = graph[n]
                    for(i in newNodes) {
                        if(!visited.contains(i)) {
                            toCheck.add(i)
                        }
                    }
                }
            }
        }
        return available
    }
}
