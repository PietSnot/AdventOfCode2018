/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Piet
 */
public class Assignment_11 {
    
    private static boolean test = false;
    private static final int GSN = test ? 800 : 5468;
    private static final int MAX_COL = test ? 5 : 300;
    private static final int MAX_ROW = test ? 5 : 300;
    private static final int[][] GRID = new int[MAX_ROW + 1][MAX_COL + 1];
    private static final Map<Integer, Map<Integer, Integer>> partialSums = new HashMap<>();
    // pS(x, y, z), i means: the sum of the values from (x, 2) to (x, 2 + y - 1), and that
    // sum is z. So, y is the length of the side of the square
    
    public static void main(String... args) {
//        testPL(3, 5, 8);
//        testPL(122, 79, 57);
//        testPL(217, 196, 39);
//        testPL(101, 153, 71);
        for (int row = 1; row <= MAX_ROW; row++) {
            for (int col = 1; col <= MAX_ROW; col++) {
                GRID[row][col] = determinePowerLevelOfCell(row, col);
            }
        }
        long start = System.currentTimeMillis();
        Map<Integer, List<Point>> rectangles = new HashMap<>();
        for (int row = 2; row <= MAX_ROW - 3; row++) {
            for (int col = 2; col <= MAX_COL - 3; col++) {
                int value = determinePowerlevelOfRectangle(row, col);
                rectangles.computeIfAbsent(value, k -> new ArrayList<>()).add(new Point(row, col));
            }
        }
        int maximalValue = new TreeSet<>(rectangles.keySet()).last();
        long end = System.currentTimeMillis();
        System.out.println("maximum; rectangle: " + rectangles.get(maximalValue));
        System.out.println("duurde: " + (end - start) / 1000. + "seconds");
        
//        int solutionB = solveB();
        start = System.currentTimeMillis();
        Entry<Point, Entry<Integer, Integer>> solution = solveB();
        end = System.currentTimeMillis();
        System.out.println("solution B: " + solution);
        System.out.println("duurde: " + (end - start) / 1000. + "seconds");
    }
    
    private static void testPL(int row, int col, int gsn) {
        int RID = row + 10;
        int powerLevel = RID * col;
        powerLevel += gsn;
        powerLevel *= RID;
        int result = (powerLevel / 100) % 10;
        result -= 5;
    }
    
    private static int determinePowerLevelOfCell(int row, int col) {
        int RID = row + 10;
        int powerLevel = RID * col;
        powerLevel += GSN;
        powerLevel *= RID;
        int result = (powerLevel / 100) % 10;
        result -= 5;
        return result;
    }
    
    private static int determinePowerlevelOfRectangle(int row, int col) {
        int result = 0;
        for (int r = row; r < row + 3; r++) {
            for (int c = col; c < col + 3; c++) {
                result += GRID[r][c];
            }
        }
        return result;
    }
    
    private static Entry<Point, Entry<Integer, Integer>> solveB() {
        fillPartialSums();
        Map<Point, Map<Integer,Integer>> sums = new HashMap<>();
        for (int row = 2; row < MAX_ROW; row++) {
//            System.out.println(row);
            for (int col = 2; col < MAX_COL; col++) {
                Point p = new Point(row, col);
                Map<Integer, Integer> temp = new HashMap<>();
                sums.put(p, temp);
                temp.put(1, GRID[row][col]);
                for (int length = 2; length <= Math.min(MAX_ROW - row, MAX_COL - col); length++) {
                    temp.put(length, determineSquare(row, col, length));
                }
            }
        }
        
        Map<Point, Entry<Integer, Integer>> solution = sums.entrySet().stream()
            .collect(toMap(e -> e.getKey(), 
                           e -> e.getValue().entrySet().stream()
                               .collect(Collectors.maxBy(Comparator.comparing(f -> f.getValue())))
                               .get()
                          )
                     )
        ;
        
        Entry<Point, Entry<Integer, Integer>> temp = solution.entrySet().stream()
            .max(Comparator.comparing(e -> e.getValue().getValue())).get();
        return temp;
    }
    
    private static void fillPartialSums() {
        for (int row = 2; row < MAX_ROW; row++) {
            Map<Integer, Integer> temp = new HashMap<>();
            partialSums.put(row, temp);
            temp.put(1, GRID[row][2]);
            int sum = GRID[row][2];
            for (int col = 3; col < MAX_COL; col++) {
                sum += GRID[row][col];
                temp.put(col - 1, sum);
            }
        }
    }
    
    private static int determineSquare(int row, int col, int length) {
        int sum = 0;
        for (int r = row; r < row + length; r++) {
            int firstpart = partialSums.get(r).get(col + length - 2);
            int secondpart = col == 2 ? 0 : partialSums.get(r).get(col - 2);
            sum += firstpart - secondpart;
        }
        return sum;
    }
}
