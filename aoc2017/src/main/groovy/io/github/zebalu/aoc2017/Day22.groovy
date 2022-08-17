package io.github.zebalu.aoc2017

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.MapConstructor

import java.util.stream.Collectors

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
            for(String l in lines) {
                for(int i=0; i<l.size(); ++i) {
                    def c = l.charAt(i)
                    if(c == (char)'#') {
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
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof  Coord) {
                Coord c = (Coord) obj;
                return x == c.x && y == c.y;
            }
            return false;
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
            new Coord(x+rhs.x, y+rhs.y)
        }
    }

    private static class EvolvedCarrier {
        Coord direction = new Coord(0,1);
        Coord position = new Coord(0,0);
        int causedInfections = 0;
        void stepOn(ComplexInfectionMap map) {
            char state = map.getAt((Coord)position);
            char nextState = turnState(state);
            if(state == (char)'.') {
                direction.turnLeft();
            } else if(state == (char)'W') {
            } else if(state == (char)'#') {
                direction.turnRight();
            } else if(state == (char)'F') {
                direction.turnBack();
            }
            if(nextState == (char)'#') {
                ++causedInfections;
            }
            map.putAt((Coord)position, (char)nextState);
            position=position.plus(direction);
        }

        private char turnState(char state) {
            if(state.is((char)'.')) {
                return (char)'W'
            } else if(state.is((char)'W')) {
                return (char)'#'
            } else if(state.is((char)'#')) {
                return (char)'F'
            } else if(state.is((char)'F')) {
                return (char)'.'
            } else {
                throw new IllegalStateException("unknown state: ${state}");
            }
        }

        int getCausedInfections() {
            return causedInfections;
        }
    }

    private static class ComplexInfectionMap {
        private Map<Coord, Character> infectedNodes = new HashMap<>();

        ComplexInfectionMap(String desc) {
            var lines = desc.lines().collect(Collectors.toList());
            int move = lines.size().intdiv(2);
            var y = move;
            for(String l: lines) {
                for(int i=0; i<l.length(); ++i) {
                    var c = l.charAt(i);
                    if(c.is((char)'#')) {
                        int x = i - move;
                        infectedNodes.put(new Coord(x,y), (char)'#');
                    }
                }
                y-=1;
            }
        }

        char getAt(Coord coord) {
            return (char)infectedNodes.getOrDefault((Coord)coord, (char)'.');
        }

        void putAt(Coord coord, char c) {
            if(c.is((char)'.')) {
                infectedNodes.remove(coord);
            } else {
                infectedNodes.put((Coord)coord, (char)c);
            }
        }
    }
}
