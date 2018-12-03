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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Piet
 */

public class assignment_03_combined {
    private static List<PietsRectangle> rectangles;
    
    public static void main(String... args) {
        createRectangles(readInputfile());
        var frequencyOfAllOccupiedPoints = rectangles.stream()
            .flatMap(PietsRectangle::getOccupiedPoints)
            .collect(groupingBy(p -> p, counting()))
        ;
        var result_A = frequencyOfAllOccupiedPoints.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .count()
        ;
        System.out.println("number of points shared by more than one whatever: " + result_A);
        System.out.println("**********************************");
        
        var setOfAllPointsWithFrequencyOne = frequencyOfAllOccupiedPoints.entrySet().stream()
            .filter(entry -> entry.getValue() == 1)
            .map(Entry::getKey)
            .collect(toCollection(HashSet::new))
        ;
        var result_B = rectangles.stream()
            .filter(r -> setOfAllPointsWithFrequencyOne.containsAll(r.getOccupiedPointsAsSet()))
            .collect(toList())
        ;
        System.out.println("rectangle(s) that has/have no overlaps");
        System.out.println(result_B);
    }
    
    private static Stream<String> readInputfile() {
        try {
           var url = assignment_03_combined.class.getResource("Resources/input_assignment_03_A.txt");
           var path = Paths.get(url.toURI());
           return Files.lines(path);
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Can't read inputfile!!!");
        }
    }
    
    private static void createRectangles(Stream<String> input) {
        rectangles = input.map(assignment_03_combined::createRectangleFromString).collect(toList());
    }
    
    private static PietsRectangle createRectangleFromString(String s) {
        // s in the form #1 @ 1,3: 4x4
        var split = s.replaceAll("[#@,:x]", " ");
        var scanner = new Scanner(split);
        var id = scanner.nextInt();
        var p = new Point(scanner.nextInt(), scanner.nextInt());
        var width = scanner.nextInt();
        var height = scanner.nextInt();
        scanner.close();
        return new PietsRectangle(id, p, width, height);
    }
}

class PietsRectangle {
    final int id;
    final Point leftTop;
    final int width, height;
    
    public PietsRectangle(int id, Point p, int width, int height) {
        this.id = id;
        leftTop = p;
        this.width = width;
        this.height = height;
    }
    
    public Stream<Point> getOccupiedPoints() {
        var stream = IntStream.range(0, width)
            .boxed()
            .flatMap(
                column -> IntStream.range(0, height)
                    .mapToObj(row -> new Point(leftTop.x + column, leftTop.y + row))
             )
        ;
        return stream;
        
        // or simply
//        List<Point> points = new ArrayList<>();
//        for (int column = 0; column < width; column++) {
//            for (int row = 0; row < height; row++) {
//                points.add(new Point(leftTop.x + column, leftTop.y + row));
//            }
//        }
//        return points;
    }
    
    public Set<Point> getOccupiedPointsAsSet() {
        return getOccupiedPoints().collect(toCollection(HashSet::new));
    }
    
    @Override
    public String toString() {
        return String.format("id: %,d , topleft: %s, width: %,d , height: %,d", id, leftTop, width, height);
    }
}
