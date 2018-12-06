/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author Piet
 */
public class Assignment_06 {
    
    private static List<PietsPoint> data = new ArrayList<>();
    private static Map<Point, List<PietsPoint>> grid = new HashMap<>();
    private static int maxRow, maxColumn;
    
    public static void main(String... args) {
        processInput();
        determineMaxRowAndColumn();
        determineDistances();
        int solutionA = solveA();
        System.out.format("oplossing A: %,d %n", solutionA);
        long solutionB = solveB();
        System.out.format("oplossing B: %,d %n", solutionB);
    }
    
    private static void processInput() {
        var url = Assignment_06.class.getResource("Resources/input_assignment_06_A.txt");
        try {
            var path = Paths.get(url.toURI());
            List<String> temp = Files.readAllLines(path);
            for (String s: temp) {
                int comma = s.indexOf(',');
                int row = Integer.parseInt(s.substring(0, comma).trim());
                int column = Integer.parseInt(s.substring(comma + 1).trim());
                data.add(new PietsPoint(row, column));
            }
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Error reading input!!!!");
        }
    }
     
    private static void determineMaxRowAndColumn() {
        maxRow = data.stream().mapToInt(p -> p.x).max().getAsInt();
        maxColumn = data.stream().mapToInt(p -> p.y).max().getAsInt();
    }
    
    private static void determineDistances() {
        for (int row = 0; row <= maxRow; row++) {
            for (int col = 0; col <= maxColumn; col++) {
                Point p = new Point(row, col);
                grid.put(p, closestPoints(p));
            }
        }
    }
        
    private static List<PietsPoint> closestPoints(Point point) {
        TreeMap<Integer, List<PietsPoint>> map = data.stream()
            .collect(groupingBy(p -> distance(p, point), TreeMap::new, mapping(p -> p, toList())));
        return map.firstEntry().getValue();
    }
    
    private static int solveA() {
        Set<PietsPoint> infinities = grid.entrySet().stream()
            .filter(e -> isEdgePoint(e.getKey()))
            .flatMap(e -> e.getValue().stream())
            .collect(toSet())
        ;
        Map<PietsPoint, List<Point>> result1 = grid.entrySet().stream()
            .filter(e -> e.getValue().size() == 1)
            .collect(groupingBy(e -> e.getValue().get(0), mapping(e -> e.getKey(), toList())))
        ;
        int result2 = result1.entrySet().stream()
            .filter(e -> !infinities.contains(e.getKey()))
            .mapToInt(e -> e.getValue().size())
            .max()
            .getAsInt()
        ;
        return result2;
    }
    
    private static long solveB() {
        Map<Point, Integer> allDistances = grid.keySet().stream()
            .collect(toMap(p -> p, p -> getTotalDistance(p)))
        ;
        var resultB = allDistances.entrySet().stream()
            .filter(e -> e.getValue() < 10_000)
            .count()
        ;
        return resultB;
    }
    
    private static int distance(Point p, Point q) {
        return Math.abs(p.x - q.x) + Math.abs(p.y - q.y);
    }
    
    private static boolean isEdgePoint(Point p) {
        return p.x == 0 || p.x == maxRow || p.y == 0 || p.y == maxColumn;
    }
    
    private static int getTotalDistance(Point point) {
        return data.stream().mapToInt(p -> distance(point, p)).sum();
    }
    
//    private static <K, V extends Comparable<V>> TreeMap<V, List<K>> transform(Map<K, V> map) {
//        var result = map.entrySet().stream()
//            .collect(groupingBy(e -> e.getValue(), 
//                                () -> new TreeMap<>(), 
//                                mapping(e -> e.getKey(), toList()))
//            );
//        return result;
//    }
}

class PietsPoint extends Point {
    PietsPoint() {this(0, 0);}
    
    PietsPoint(int x, int y) {super(x, y);}
}    
