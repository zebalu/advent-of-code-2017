package io.github.zebalu.aoc2017

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.MapConstructor

@CompileStatic
class Day22 extends AbstractDay{
    static void main(String[] args) {
        new Day22().solve()
    }
    @Override
    protected void solve1() {
        InfectionMap im = new InfectionMap(input)
        Carrier vc = new Carrier()
        10_000.times { 
            vc.stepOn(im)
        }
        solution1 = vc.causedInfections
    }
    @Override
    protected void solve2() {
        ComplexInfectionMap cim = new ComplexInfectionMap(input)
        EvolvedCarrier ec = new EvolvedCarrier()
        10_000_000.times { 
            ec.stepOn(cim)
        }
        solution2 = ec.causedInfections
    }
    
    
    private static class InfectionMap {
        private Set<Coord> infectedNodes = new HashSet<>()
        
        InfectionMap(String desc) {
            def lines = desc.lines().toList()
            int move = lines.size().intdiv(2)
            def y = move
            for(l in lines) {
                for(int i=0; i<l.size(); ++i) {
                    def c = l.charAt(i)
                    if(c == '#') {
                        int x = i - move
                        infectedNodes.add(new Coord(x,y))
                    }
                }
                y-=1
            }
        }
        
        boolean getAt(Coord coord) {
            return infectedNodes.contains(coord)
        }
        
        void putAt(Coord coord, boolean infect) {
            if(infect) {
                infectedNodes.add((Coord)coord)
            } else {
                infectedNodes.remove(coord)
            }
        }
    }
    
    private static class Carrier {
        Coord direction = new Coord(0,1)
        Coord position = new Coord(0,0)
        int causedInfections = 0
        void stepOn(InfectionMap map) {
            boolean infected = map[position]
            if(infected) {
                direction.turnRight()
            } else {
                direction.turnLeft()
                ++causedInfections
            }
            map[(Coord)position]=(boolean)!infected
            position+=direction
        }
    }
    
    @Canonical
    @MapConstructor
    private static class Coord {
        int x
        int y
        @Override
        int hashCode() {
            return x*10_000+y
        }
        void turnLeft() {
            int t = x
            x = -y
            y = t
        }
        void turnRight() {
            int t = x
            x = y
            y = -t
        }
        void turnBack() {
            x = -x
            y = -y
        }
        Coord plus(Coord rhs) {
            new Coord(x: x+rhs.x, y: y+rhs.y)
        }
    }
    
    private static class EvolvedCarrier {
        Coord direction = new Coord(0,1)
        Coord position = new Coord(0,0)
        int causedInfections = 0
        void stepOn(ComplexInfectionMap map) {
            char state = map[(Coord)position]
            char nextState = turnState(state)
            if(state == '.') {
                direction.turnLeft()
            } else if(state == 'W') {
            } else if(state == '#') {
                direction.turnRight()
            } else if(state == 'F') {
                direction.turnBack()
            }
            if(nextState == '#') {
                ++causedInfections
            }
            map[(Coord)position]=(char)nextState
            position+=direction
        }
        
        private char turnState(char state) {
            switch(state) {
                case '.': return 'W' as char
                case 'W': return '#' as char
                case '#': return 'F' as char
                case 'F': return '.' as char
                default: throw new IllegalStateException("unknown state: ${state}")
            }
        }
    }
    
    private static class ComplexInfectionMap {
        private Map<Coord, Character> infectedNodes = new HashMap<>()
        
        ComplexInfectionMap(String desc) {
            def lines = desc.lines().toList()
            int move = lines.size().intdiv(2)
            def y = move
            for(l in lines) {
                for(int i=0; i<l.size(); ++i) {
                    def c = l.charAt(i)
                    if(c == '#') {
                        int x = i - move
                        infectedNodes.put(new Coord(x,y), '#' as char)
                    }
                }
                y-=1
            }
        }
        
        char getAt(Coord coord) {
            return (char)infectedNodes.getOrDefault((Coord)coord, '.' as char)
        }
        
        void putAt(Coord coord, char c) {
            if(c == '.') {
                infectedNodes.remove(coord)
            } else {
                infectedNodes[(Coord)coord] = (char)c
            }
        }
    }
}
