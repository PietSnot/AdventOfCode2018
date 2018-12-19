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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class Assignment_18 {
    
    Map<Point, Acre> terrain = new HashMap<>();
    boolean test = false;
    GameRule rule;
    
    //------------------------------------------------
    public static void main(String... args) {
        new Assignment_18().go();
    }
    
    //------------------------------------------------
    private void go() {
        processInput();
//        print(terrain);
        determineGameRule();
        long solutionA = solveA();
        System.out.println("solution A = " + solutionA);
        solveB();
    }
    
    //------------------------------------------------
    private void processInput() {
        try {
            var url = Assignment_18.class.getResource("Resources/input_assignment_18" + (test ? "_test" : "") + ".txt");
            var path = Paths.get(url.toURI());
            var input = Files.readAllLines(path);
            for (int row = 0; row < input.size(); row++) {
                String s = input.get(row);
                for (int col = 0; col < s.length(); col++) {
                    char c = s.charAt(col);
                    var acre = c == '#' ? Acre.of(row, col, AcreType.LUMBERYARD) : 
                                c == '|' ? Acre.of(row, col, AcreType.TREES)      : 
                                           Acre.of(row, col, AcreType.OPEN)
                    ;
                    terrain.put(new Point(row, col), acre);
                }
            }
            terrain.values().forEach(acre -> acre.setNeighbors(getNeighbors(acre, terrain)));
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("error reading and processing input!!!!!1");
        }
    }
    
    //------------------------------------------------
    private void determineGameRule() {
        rule = acre -> {
            var freqs = acre.neighbors.stream().collect(groupingBy(a -> a.acretype, counting()));
            Point p = acre.location;
            switch (acre.acretype) {
                case OPEN: 
                    return freqs.getOrDefault(AcreType.TREES, 0L) >= 3 ? Acre.of(p, AcreType.TREES) :
                                                                        Acre.of(p, AcreType.OPEN);
                case TREES: 
                    return freqs.getOrDefault(AcreType.LUMBERYARD, 0L) >= 3 ? Acre.of(p, AcreType.LUMBERYARD) : 
                                                                             Acre.of(p, AcreType.TREES);
                case LUMBERYARD: return 
                    (freqs.getOrDefault(AcreType.LUMBERYARD, 0L) >= 1 && freqs.getOrDefault(AcreType.TREES, 0L) >= 1) ? 
                        Acre.of(p, AcreType.LUMBERYARD) : 
                        Acre.of(p, AcreType.OPEN);
                default: throw new RuntimeException("unknowc AcreType!!!");
            }
        };
        Acre.setRule(rule);
    }
   
   //------------------------------------------------
   private long solveA() {
        LinkedList<Map<Point, Acre>> list = new LinkedList<>();
        list.add(new HashMap<>(terrain));
        
        for (int i = 1; i <= 10; i++) {
//            System.out.println("busy with generation: " + i);
            var newGeneration = list.getLast().entrySet().stream()
                .map(e -> new Duo(e.getKey(), Acre.of(e.getKey(), e.getValue().next().acretype)))
                .collect(toMap(duo -> duo.p, duo -> duo.acre))
            ;
            newGeneration.values().forEach(acre -> acre.setNeighbors(getNeighbors(acre, newGeneration)));
            list.addLast(newGeneration);
            long nrOfTrees = newGeneration.values().stream().filter(a -> a.acretype == AcreType.TREES).count();
            long nrOfLumber =newGeneration.values().stream().filter(a -> a.acretype == AcreType.LUMBERYARD).count();
            if (i % 10 == 0) System.out.format("Trees: %d, Lum: %d, resval: %d%n", nrOfTrees, nrOfLumber, nrOfTrees * nrOfLumber);
            
//            print(newGeneration);
                
        }
        var l = list.getLast().values().stream().filter(a -> a.acretype == AcreType.LUMBERYARD).count();
        var t = list.getLast().values().stream().filter(a -> a.acretype == AcreType.TREES).count();  
        return l * t;
    }
   
      //------------------------------------------------
   private long solveB() {
        LinkedList<Map<Point, Acre>> list = new LinkedList<>();
        list.add(new HashMap<>(terrain));
        
        for (int i = 1; i <= 3_000; i++) {
//            System.out.println("busy with generation: " + i);
            var newGeneration = list.getLast().entrySet().stream()
                .map(e -> new Duo(e.getKey(), Acre.of(e.getKey(), e.getValue().next().acretype)))
                .collect(toMap(duo -> duo.p, duo -> duo.acre))
            ;
            newGeneration.values().forEach(acre -> acre.setNeighbors(getNeighbors(acre, newGeneration)));
            list.addLast(newGeneration);
            long nrOfTrees = newGeneration.values().stream().filter(a -> a.acretype == AcreType.TREES).count();
            long nrOfLumber =newGeneration.values().stream().filter(a -> a.acretype == AcreType.LUMBERYARD).count();
            if (i % 100 == 0) System.out.format("generation: %d, Trees: %d, Lum: %d, resval: %d%n", i, nrOfTrees, nrOfLumber, nrOfTrees * nrOfLumber);
            
//            print(newGeneration);
                
        }
        var l = list.getLast().values().stream().filter(a -> a.acretype == AcreType.LUMBERYARD).count();
        var t = list.getLast().values().stream().filter(a -> a.acretype == AcreType.TREES).count();  
        return l * t;
    }
    
    //------------------------------------------------
    private List<Acre> getNeighbors(Acre a, Map<Point, Acre> terrain) {
        List<Point> points = new ArrayList<>();
        points.addAll(List.of(
            new Point(a.location.x - 1, a.location.y - 1),
            new Point(a.location.x - 1, a.location.y),
            new Point(a.location.x - 1, a.location.y + 1),
            new Point(a.location.x, a.location.y -1),
            new Point(a.location.x, a.location.y + 1),
            new Point(a.location.x + 1, a.location.y - 1),
            new Point(a.location.x + 1, a.location.y),
            new Point(a.location.x + 1, a.location.y + 1)
        ));
        var result = points.stream().filter(p -> terrain.containsKey(p)).map(terrain::get).collect(toList());
        return result;
    }
    
    //------------------------------------------------
    private void print(Map<Point, Acre> terrain) {
        int maxrow = terrain.keySet().stream().max(Comparator.comparing(e -> e.x)).get().x;
        var list = IntStream.rangeClosed(0, maxrow)
            .boxed()
            .map(i -> terrain.entrySet().stream()
                          .filter(e -> e.getKey().x == i)
                          .map(e -> e.getValue())
                          .sorted(Comparator.comparingInt((Acre acre) -> acre.location.y))
                          .collect(toList())
                 )
            .collect(toList())
        ;
        System.out.println("**************************************");
        list.stream().forEach(System.out::println);
    }
}   // end of class


//************************************************************************
enum AcreType {
    OPEN("."), LUMBERYARD("#"), TREES("|");
    private final String symbol;
    
    //------------------------------------------------
    AcreType(String symbol) {
        this.symbol = symbol;
    }
    
    //------------------------------------------------
    @Override
    public String toString() {
        return symbol;
    }
}

//************************************************************************
interface GameRule {
    public Acre next(Acre acre);
}

//************************************************************************
class Acre {
    final AcreType acretype;
    final Point location;
    List<Acre> neighbors;
    static GameRule rule;
    
    //------------------------------------------------
    private Acre(int row, int col, AcreType a) {
        location = new Point(row, col);
        acretype = a;
    }
    
    //------------------------------------------------
    public static Acre of(int row, int col, AcreType a) {
        return new Acre(row, col, a);
    }    
    
    //------------------------------------------------
    public static Acre of(Point p, AcreType a) {
        return new Acre(p.x, p.y, a);
    }
    
    //------------------------------------------------
    public static void setRule(GameRule rule) {
        Acre.rule = rule;
    }
    
    //------------------------------------------------
    public void setNeighbors(List<Acre> list) {
        neighbors = new ArrayList<>(list);
    }
    
   //------------------------------------------------
   public Acre next() {
        if (rule == null) throw new RuntimeException("Gamerule has not been set!!!!");
        Acre acre = rule.next(this);
        return acre;
    }
      
    //------------------------------------------------
    @Override 
    public String toString() {
        return acretype == AcreType.LUMBERYARD ? "#" : acretype == AcreType.TREES ? "|" : ".";
    }
}

//**********************************************************************
class Duo {
    Point p;
    Acre acre;
    Duo(Point p, Acre a) {
        this.p = p;
        acre = a;
    }
}
