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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class assignment_04_A {
    
    private static final Map<Integer, List<Integer>> guards = new HashMap<>();
    // key: id of the guard
    // value: list of minutes asleep
    
    public static void main(String... args) {
        createGuardsMap();
        Comparator<Map.Entry<Integer, List<Integer>>> comparator = Comparator.comparing(e -> e.getValue().size());
        var entry = guards.entrySet().stream()
            .max(comparator)
            .get()
        ;
        var minutesAsleep = entry.getValue().stream()
            .collect(groupingBy(i -> i, counting()))
        ;
        Comparator<Map.Entry<Integer, Long>> comparator2 = Comparator.comparingLong(e -> e.getValue());
        var max = minutesAsleep.entrySet().stream().max(comparator2).get();
        
        var result_A = entry.getKey() * max.getKey();
        System.out.println("result A = " + result_A);
        
        //*********************************************************************
        // part B
        //*********************************************************************
        
        // to produce: a Map<Integer, Map<Guard, Integer>> with 
        // key = minute
        // value = map of guard and the frequency of that minute
        
        var mapPartB1 = guards.entrySet().stream()
            .flatMap(e -> e.getValue().stream().map(integer -> new Pair<>(integer, e.getKey())))
            .collect(groupingBy(pair -> pair.k,
                                groupingBy(pair -> pair.v, counting()))
             )
        ;
        Comparator<Map.Entry<Integer,Long>> byValue = Map.Entry.comparingByValue();
        var mapPartB2 = mapPartB1.entrySet().stream()
            .collect(toMap(e -> e.getKey(), 
                           e -> e.getValue().entrySet().stream().max(byValue).get()
                           )
             )
        ;
        Comparator<Map.Entry<Integer, Map.Entry<Integer, Long>>> flup =
            Comparator.comparing(e -> e.getValue().getValue())
        ;
        var resultB = mapPartB2.entrySet().stream().max(flup).get();
        var minute = resultB.getKey();
        var guard = resultB.getValue().getKey();
        var freq = resultB.getValue().getValue();
        System.out.format("minute: %d , guard: %d, frequency of minute: %d %n", 
                          minute, guard, freq
        );
        System.out.format("result = %d * %d = %d %n", minute, guard, minute * guard);
    }
    
    private static void createGuardsMap() {
        URL url = assignment_04_A.class.getResource("Resources/input_assignment_04_A.txt");
        try {
            Path path = Paths.get(url.toURI());
            List<String> inputs = Files.lines(path)
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
    
    private static void processInputrecords(List<String> list) {
        var id = 0;
        LocalDateTime from = null, to = null;
        for (String s: list) {
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
    
    private static LocalDateTime getLocalDateTime(String[] s) {
        int j = Integer.parseInt(s[0]);
        int m = Integer.parseInt(s[1]);
        int d = Integer.parseInt(s[2]);
        int h = Integer.parseInt(s[3]);
        int min = Integer.parseInt(s[4]);
        return LocalDateTime.of(j, m, d, h, min);
    }
    
    public static List<Integer> MinutesAsleep(LocalDateTime from, LocalDateTime to) {
        var duration = Duration.between(from, to);
        var startminute = from.getMinute();
        var list = IntStream
            .range(startminute, startminute + (int) duration.toMinutes())
            .boxed()
            .collect(toList())
        ;
        return list;
    }
}

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
    public String toString() {
        return k + ", " + v;
    }
}
