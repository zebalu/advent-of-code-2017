package io.github.zebalu.aoc2017

import java.time.Duration
import java.time.Instant
import java.util.jar.JarFile

class WholeAdvent {
    private static final String PATH = WholeAdvent.class.packageName.replace('.', '/')


    static void main(String[] args) {
        def days = createDays()
        Instant start = Instant.now()
        days.each { it.solve() }
        Instant end = Instant.now()
        println "took: ${Duration.between(start, end).toMillis()} ms"
    }

    private static List<AbstractDay> createDays() {
        ClassLoader.
                systemClassLoader
                .getResources(PATH)
                .collect { it.protocol == "jar"? findClassesInJar(it) : it.readLines()}
                .flatten()
                .unique()
                .collect { it.substring(0, it.lastIndexOf('.'))}.sort()
                .collect{Class.forName("${WholeAdvent.class.packageName}.${it}") }
                .findAll { it.superclass == AbstractDay }
                .collect{ it.newInstance(new Object[0]) }
    }

    private static List<String> findClassesInJar(URL url) {
        url.openConnection()
                .getJarFile()
                .entries()
                .collect { it.name }
                .findAll{  it.startsWith(PATH) && it.endsWith('.class') }
                .collect { it.substring(PATH.size()+1) }
    }
}
