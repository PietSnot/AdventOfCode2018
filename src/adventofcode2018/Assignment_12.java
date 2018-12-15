/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 *
 * @author Piet
 */
public class Assignment_12 {
    
    private static TreeMap<Long, Character> state = new TreeMap<>();
    private static Map<ArrayList<Character>, Character> rules = new HashMap<>();
    private static Long MAX_GENERATION = 200L;
    private static Map<Long, String> solutionA = new TreeMap<>();
    
    public static void main(String... args) {
        processInput();
        
        long solutionA = solveA();
        System.out.println("solution A: " + solutionA);
        System.out.println();
        analyseSolutionA();
//        long solutionB = solveB();
//        System.out.println("solution B = " + solutionB);
    }
    
    private static void processInput() {
        var url = Assignment_12.class.getResource("Resources/input_assignment_12.txt");
        try {
            var path = Paths.get(url.toURI());
            var input = Files.readAllLines(path);
            parseInput(input);
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("error reading input!!!!");
        }
    }
    
    private static void parseInput(List<String> input) {
        char[] firstGeneration = input.get(0).substring(15).toCharArray();
        for (int i = 0; i < firstGeneration.length; i++) state.put((long) i, firstGeneration[i]);
        for (int i = 2; i < input.size(); i++) {
            String[] split = input.get(i).split(" => ");
            ArrayList<Character> list = new ArrayList<>();
            for (char c: split[0].toCharArray()) list.add(c);
            rules.put(list, split[1].charAt(0));
        }
    }
    
    private static long solveA() {
        System.out.format("generation %,2d%n", 0);
        System.out.println(makeString(state));
        System.out.println("************************************************");
        TreeMap<Long, Character> newState = new TreeMap<>(state);
        for (long generation = 1; generation <= MAX_GENERATION; generation++) {
            newState = nextGeneration(newState);
//            System.out.format("generation %,2d%n", generation);
            String s = makeString(newState);
//            System.out.println(s);
            if (generation >= 100) {
                solutionA.put(generation, s);
//                System.out.println(generation + ":" + newState);
            }
            
////            System.out.println("************************************************");
        }
        long result = newState.entrySet().stream()
            .filter(e -> e.getValue().equals('#'))
            .mapToLong(e -> e.getKey())
            .sum()
        ;
        
        long minindex = newState.keySet().stream().mapToLong(i -> i).min().getAsLong();
        System.out.println("minindex = " + minindex);
        System.out.println(newState);
        return result;
    }
    
    private static void analyseSolutionA() {
        solutionA.forEach((k, v) -> {
            long start = v.indexOf("#");
            long end = v.lastIndexOf("#");
            long length = end - start + 1;
            long delta = k - start;
            long flup = k * 178L + 1691L;
            long sum = IntStream.range(0, v.length()).filter(i -> v.charAt(i) == '#').sum();
            long total = LongStream.rangeClosed(start, end).sum();
            System.out.format("gen: %d, sum: %d, flup: %d, sum: %d, start: %d, end: %d, length: %d, delta: %d %n", k, total, flup, sum, start, end, length, delta);
        });
    }
    
    private static long solveB() {
        long N = 50_000_000_000L;
//        long N = MAX_GENERATION;
        long start = N - 79;
        long end = N + 98;
        long solutionB = LongStream.rangeClosed(start, end).sum();
        long flup = N * 178L + 1691L;
        System.out.println("flup = " + flup);
        return solutionB;
    }
    
    private static TreeMap<Long, Character> nextGeneration(TreeMap<Long, Character> state) {
        TreeMap<Long, Character> newState = new TreeMap<>(state);
        long minIndex = state.keySet().stream().min(Comparator.naturalOrder()).get();
        long maxIndex = state.keySet().stream().max(Comparator.naturalOrder()).get();
        for (long index = minIndex; index <= maxIndex + 2; index++) {
            List<Character> list = new ArrayList<>();
            for (long i = index - 2; i <= index + 2; i++) list.add(state.getOrDefault(i, '.'));
            newState.put(index, rules.get(list));
        }
        return newState;
    }
    
    private static String makeString(TreeMap<Long, Character> map) {
        StringBuilder sb = new StringBuilder();
        map.values().forEach(sb::append);
        return sb.toString();
    }
        
}
