/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class assignment_04_combined {
    
    private static final Map<Integer, List<Integer>> guards = new HashMap<>();
    // key: id of the guard
    // value: list of minutes asleep
    
    public static void main(String... args) {
        createGuardsMap();
        
        //*********************************************************************
        // part A
        //*********************************************************************
        Comparator<Map.Entry<Integer, List<Integer>>> comparator = Comparator.comparing(e -> e.getValue().size());
        var entry = guards.entrySet().stream()
            .max(comparator)
            .get()
        ;
        var minutesAsleep = entry.getValue().stream()
            .collect(groupingBy(i -> i, counting()))
        ;
        var max = minutesAsleep.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        
        var resultA = entry.getKey() * max.getKey();
        System.out.format("solution part A = %,d %n", resultA);
        
        //*********************************************************************
        // part B
        //*********************************************************************
        
        // creating Map<minute, Map<guard, frequency>>
        var mapPartB1 = guards.entrySet().stream()
            .flatMap(e -> e.getValue().stream().map(integer -> new Pair<>(integer, e.getKey())))
            .collect(groupingBy(pair -> pair.k,
                                groupingBy(pair -> pair.v, counting()))
             )
        ;
        
        // creating List<minute, Map.Entry<guard, frequency>>
        var mapPartB2 = mapPartB1.entrySet().stream()
            .flatMap(e -> e.getValue().entrySet().stream().map(f -> new Pair<>(e.getKey(), f)))
            .collect(toList())
        ;
        
        // creating a Comparator to find the max from above List
        Comparator<Pair<Integer, Map.Entry<Integer, Long>>> comparatorB = 
            Comparator.comparingLong(pair -> pair.v.getValue());
        
        
        // and here we go, result is of type Pair<Integer, Map.Entry<Integer, Long>>
        var resultB = mapPartB2.stream().max(comparatorB).get();
        
        int minute = resultB.k;
        long guard = resultB.v.getKey();
        System.out.format("solution part B = %,d * %,d = %,d %n", minute, guard, minute * guard);
    }
    
    //----------------------------------------------
    private static void createGuardsMap() {
        var url = assignment_04_combined.class.getResource("Resources/input_assignment_04_A.txt");
        try {
            var path = Paths.get(url.toURI());
            var inputs = Files.lines(path)
                .sorted()
                .map(s -> s.replaceAll("[\\[\\]\\-:#]", " "))
                .collect(toList())
            ;
            processInputrecords(inputs);
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("error while reading input file!!!!");
        }
    }
    
    //----------------------------------------------
    private static void processInputrecords(List<String> list) {
        var id = 0;
        LocalDateTime from = null, to = null;
        for (var s: list) {
            var strings = s.trim().split("\\s+");
            if (strings[5].equals("Guard")) {
                id = Integer.parseInt(strings[6]);
                guards.computeIfAbsent(id, k -> new ArrayList<>());
            } 
            else if (strings[5].equals("falls")) {
                from = getLocalDateTime(strings);
            }
            else if (strings[5].equals("wakes")) {
                to = getLocalDateTime(strings);
                guards.get(id).addAll(MinutesAsleep(from, to));
            }
        }
    }
    
    //----------------------------------------------
    private static LocalDateTime getLocalDateTime(String[] s) {
        int j = Integer.parseInt(s[0]);
        int m = Integer.parseInt(s[1]);
        int d = Integer.parseInt(s[2]);
        int h = Integer.parseInt(s[3]);
        int min = Integer.parseInt(s[4]);
        return LocalDateTime.of(j, m, d, h, min);
    }
    
    //----------------------------------------------
    public static List<Integer> MinutesAsleep(LocalDateTime from, LocalDateTime to) {
        var duration = Duration.between(from, to);
        var startminute = from.getMinute();
        return IntStream
            .range(startminute, startminute + (int) duration.toMinutes())
            .map(i -> i % 60)
            .boxed()
            .collect(toList())
        ;
    }
}

//****************************************************************************
// class Pair
//****************************************************************************
class Pair<K, V> {
    final K k;
    final V v;
    
    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }
   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Pair)) return false;
        Pair p = (Pair) o;
        return k.equals(p.k) && v.equals(p.v);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.k);
        hash = 37 * hash + Objects.hashCode(this.v);
        return hash;
    }
    
    @Override
    public String toString() {
        return k + ", " + v;
    }
}
