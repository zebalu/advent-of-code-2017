package io.github.zebalu.aoc2017

import java.time.Duration
import java.time.Instant

import groovy.transform.Canonical
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.MapConstructor

@CompileStatic
class Day20 extends AbstractDay {
    
    static void main(String[] args) {
        new Day20().solve()
    }
    
    private List<Particle> particles;
    
    Day20() {
        int count = 0
        particles = input.lines().collect { new Particle(count++, (String)it) }
    }
    
    @Override
    protected void solve1() {
        long tMax = particles.collect { it.timeToTurn }.max()
        Comparator<Particle> comparator = Particle.getComparatorForTime(tMax)
        solution1 = particles.min(comparator).id
    }
    @Override
    protected void solve2() {
        Map<Long,Set<Integer>> collisionTimes = findAllCollisionTimes()
        def ids = particles.collect { it.id } as Set
        collisionTimes.keySet().sort().each { t ->
            def collides = new HashSet<>(collisionTimes[t])
            def missing = (new HashSet<Integer>(collides)).removeAll(ids)
            collides.removeAll(missing)
            if(collides.size()>1) {
                ids.removeAll(collides)
            }
        }
        solution2 = ids.size()
    }

    private Map<Long,Set<Integer>> findAllCollisionTimes() {
        Map<Long, Set<Integer>> collisionTimes = new HashMap<>()
        for(int i = 0 ; i<particles.size(); ++i) {
            for(int j=i+1; j<particles.size(); ++j) {
                def cts = particles[i].findCollisonTimesWith(particles[j])
                for(t in cts) {
                    collisionTimes.computeIfAbsent(t, {_ -> new HashSet<Integer>()}).addAll([particles[i].id, particles[j].id])
                }
            }
        }
        return collisionTimes
    }
    @MapConstructor
    @Canonical
    static class Particle {
        //final static Comparator<Particle> PARTICLE_COMPARATOR = Comparator.<Particle>comparingLong{it.acceleration.distance} .thenComparingLong{it.position.distance}.<Particle>thenComparingLong {it.velocity.distance}
        int id
        Coord position
        Coord velocity
        Coord acceleration
        //@CompileDynamic
        Particle(int id, String line) {
            this.id = id
            def parts = line.split(', ')
            //def (p,v,a) = line.split(', ')
            position = new Coord(parts[0])// new Coord(p)
            velocity = new Coord(parts[1])//new Coord(v)
            acceleration = new Coord(parts[2])//new Coord(a)
        }
        long getTimeToTurn() {
            long accTurn = velocity.turnsIn(acceleration)
            def posThen = position + (velocity * accTurn) + (acceleration *((accTurn*(accTurn+1)).intdiv(2)))
            def vThen = velocity + (acceleration*accTurn)
            accTurn + posThen.turnsIn(vThen)
        }
        void step() {
            velocity += acceleration
            position += velocity
        }
        long getDistance() {
            position.distance
        }
        Particle minus(Particle o) {
            return new Particle(position: position-o.position, velocity: velocity-o.velocity, acceleration: acceleration-o.acceleration)
        }
        List<Long> findCollisonTimesWith(Particle p) {
            def diff = this - p
            def res = possibleCollisionTimes(diff.position.x, diff.velocity.x, diff.acceleration.x)
            /*
            if(res == null) {
                res = possibleCollisionTimes(diff.position.y, diff.velocity.y, diff.acceleration.y)
                if(res == null) {
                    res = possibleCollisionTimes(diff.position.z, diff.velocity.z, diff.acceleration.z)
                }
            }*/
            def result = new ArrayList<Long>()
            if(res != null) {
            for(t in res) {
                if(//isCollidingAt(diff.position.x, diff.velocity.x, diff.acceleration.x, t) && 
                   isCollidingAt(diff.position.y, diff.velocity.y, diff.acceleration.y, t) &&
                   isCollidingAt(diff.position.z, diff.velocity.z, diff.acceleration.z, t) ) {
                   result << (long)t
                }
            }
            }
            return result
        }
        static Comparator<Particle> getComparatorForTime(long t) {
            Comparator.<Particle>comparingLong{it.acceleration.distance}.
                thenComparingLong{(it.velocity + (it.velocity + (it.acceleration * t))).distance}.
                thenComparingLong{(it.position + (it.velocity*t) + (it.acceleration * ((t*(t+1)).intdiv(2)))).distance}
        }
        static boolean isCollidingAt(long p, long v, long a, long t) {
            def A = a / 2.0
            def B = v + A
            def C = p
            return (A*t*t + B*t + C) == 0
        }
        static List<Long> possibleCollisionTimes(double p, double v, double a) {
            def A = a / 2.0
            def B = v + A
            def C = p
            if(A != 0) {
                return findTimeInParabole(A, B, C)
            } else {return []}/*if(B != 0) {
                return findTimeInRate(B, C)
            } else if(C != 0) {
                return []
            } else {
                return null
            }*/
        }

        private static List<Long> findTimeInRate(double B, double C) {
            def t = Math.round(C / B)
            if(t < 0) {
                return [(long)(t.abs())]
            }
            return []
        }

        private static List<Long> findTimeInParabole(double A, double B, double C) {
            def sq = B*B - 4*A*C
            if(sq <=  0) {
                return []
            }
            def sqrt = Math.sqrt(sq)
            def t1 = Math.round((-B + sqrt)/(2*A))
            def t2 = Math.round((-B - sqrt)/(2*A))
            def res = new ArrayList<Long>()
            if(t1 > 0) {
                res << (long)t1
            }
            if(t2 > 0) {
                res << (long)t2
            }
            return res
        }
    }
    @MapConstructor
    @Canonical
    static class Coord {
        long x
        long y
        long z
        Coord(String desc) {
            def parts = desc.substring(3, desc.length()-1).split(',')
            this.x = parts[0] as int
            this.y = parts[1] as int
            this.z = parts[2] as int 
        }
        long getDistance() {
            x.abs()+y.abs()+z.abs()
        }
        Coord plus(Coord lhs)  {
            new Coord(x: x+lhs.x, y: y+lhs.y, z: z+lhs.z)
        }
        Coord multiply(long t) {
            new Coord(x: x*t, y: y*t, z: z*t)
        }
        Coord div(long t) {
            new Coord(x: x.intdiv(t), y: y.intdiv(t), z: z.intdiv(t))
        }
        Coord minus(Coord o) {
            new Coord(x: x-o.x, y: y-o.y, z: z-o.z)
        }
        long turnsIn(Coord o) {
            long max = Long.MIN_VALUE
            if(Math.signum(x) != Math.signum(o.x) && o.x != 0) {
                max = Math.max(max, (long)Math.ceil(-x / (double)o.x))
            }
            if(Math.signum(y) != Math.signum(o.y) && o.y != 0) {
                max = Math.max(max, (long)Math.ceil(-y / (double)o.y))
            }
            if(Math.signum(z) != Math.signum(o.z) && o.z != 0) {
                max = Math.max(max, (long)Math.ceil(-z / (double)o.z))
            }
            max
        }
        
    }
}
