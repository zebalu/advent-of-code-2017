package io.github.zebalu.aoc2017

import java.time.Duration
import java.time.Instant

import groovy.transform.CompileStatic

@CompileStatic
class Day21 extends AbstractDay {
    public static void main(String[] args) {
        Instant s = Instant.now()
        new Day21().solve()
        println Duration.between(s, Instant.now())
    }
    private static final String START_DESC=".#./..#/###"
    
    private Map<Integer, CharMatrix> smallRules = new HashMap<>()
    private Map<Integer, CharMatrix> largeRules = new HashMap<>()
    
    Day21() {
        input.lines().each { String line ->
            String[] parts = line.split(" => ")
            CharMatrix rule = new CharMatrix(parts[0])
            CharMatrix replacement = new CharMatrix(parts[1])
            Set<Integer> reps = rule.toAllInts()
            reps.each { int i ->
                if(rule.hight == 2) {
                    smallRules[i] = (CharMatrix)replacement
                } else {
                    largeRules[i] = (CharMatrix)replacement
                }
            }
        }
    }
    @Override
    protected void solve1() {
        solution1 = update(new CharMatrix(START_DESC), 5).lit
    }
    @Override
    protected void solve2() {
        solution2 = update(new CharMatrix(START_DESC), 18).lit   
    }
    
    private CharMatrix update(CharMatrix matrix, int times) {
        def current = matrix
        times.times { 
            def pieces = current.even ? current.cutToPieces(2) : current.cutToPieces(3)
            int newPieceHeight = pieces[0][0].hight+1
            def updated = new CharMatrix(pieces.size()*newPieceHeight)
            for(int i=0; i<pieces.size(); ++i) {
                for(int j=0; j<pieces[i].size(); ++j) {
                    def repl = getReplacementOf((CharMatrix)pieces[i][j])
                    repl.setOn(updated, i*newPieceHeight, j*newPieceHeight)
                }
            }
            current = updated
        }
        return current
    }
    
    private CharMatrix getReplacementOf(CharMatrix rule) {
        int repr = rule.toInt()
        if(rule.hight==2) {
            return (CharMatrix)smallRules[repr]
        } else {
            return (CharMatrix)largeRules[repr]
        }
    }
    static class CharMatrix {
        static final int SIMPLE_RULE_SIZE="../.. => ..#/#.#/###".size()
        char[][] chars
        
        CharMatrix(String desc) {
            def lines = desc.split('/')
            chars=new char[lines.size()][]
            for(int i=0; i<lines.size(); ++i) {
                chars[i] = lines[i].toCharArray()
            }
        }
        
        CharMatrix(int size) {
            chars = new char[size][]
            for(int i=0; i<size; ++i) {
                chars[i] = new char[size]
            }
        }
         
        private CharMatrix(CharMatrix cm) {
            chars = new char[cm.chars.size()][]
            for(int i=0; i<chars.size(); ++i) {
                chars[i] = new char[cm.chars[i].size()]
                for(int j=0; j<chars[i].size(); ++j) {
                    chars[i][j] = cm.chars[i][j]
                }
            }
        }
        
        CharMatrix verticalFlip() {
            def flipped = new CharMatrix(this)
            for(int i=0; i<chars.size(); ++i) {
                for(int j=0; j<=chars[i].size()/2; ++j) {
                    int mirror = chars[i].size()-j-1
                    flipped.chars[i][j] = chars[i][mirror]
                    flipped.chars[i][mirror] = chars[i][j]
                }
            }
            return flipped
        }
        
        CharMatrix turnLeft() {
            def turned = new CharMatrix(this)
            for(int i=0; i<chars.size(); ++i) {
                for(int j=0; j<chars[i].size(); ++j) {
                    int mx = chars[i].size()-1
                    turned.chars[mx-j][i] = chars[i][j]
                }
            }
            return turned
        }
        
        String toLine() {
            StringJoiner sj = new StringJoiner("/")
            for(ch in chars) {
                sj.add(new String(ch))
            }
            return sj.toString()
        }
        
        int toInt() {
            int res = 0
            for(chs in chars) {
                for(ch in chs) {
                    res *= 2
                    if(ch == '#') {
                        res += 1
                    }
                }
            }
            return res
        }
        
        int getHight() {
            return chars.size()
        }
        
        void setOn(CharMatrix cm, int i0, int j0) {
            for(int i=0; i<chars.size(); ++i) {
                for(int j=0; j<chars[i].size(); ++j) {
                    cm.chars[i0+i][j0+j]=chars[i][j]
                }
            }
        }
                
        Set<Integer> toAllInts() {
            List<CharMatrix> ms = []
            Set<Integer> result = new HashSet<>()
            def curr = new CharMatrix(this)
            4.times {
                ms << curr
                curr = curr.turnLeft()
            }
            for(m in ms) {
                result << m.toInt()
                result << m.verticalFlip().toInt()
            }
            return result
        }
        
        List<List<CharMatrix>> cutToPieces(int size) {
            List<List<CharMatrix>> result = []
            for(int i0=0; i0<chars.size(); i0+=size) {
                List<CharMatrix> row = []
                for(int j0=0; j0<chars[i0].size(); j0+=size) {
                    def cm = new CharMatrix(size)
                    for(int i=0; i<size; ++i) {
                        for(int j=0; j<size; ++j) {
                            cm.chars[i][j] = chars[i0+i][j0+j]
                        }
                    }
                    row << (CharMatrix)cm
                }
                result.add((List<CharMatrix>)row)
            }
            return result
        }
        
        boolean isEven() {
            hight%2==0
        }
        
        int getLit() {
            int lit = 0
            for(cs in chars) {
                for(c in cs) {
                    if(c == '#') {
                        ++lit
                    }
                }
            }
            return lit
        }
    }
}
