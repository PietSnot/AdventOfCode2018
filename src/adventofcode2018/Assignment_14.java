/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class Assignment_14 {
    
    private static List<Integer> recipes = new ArrayList(List.of(3, 7));
    static int after = 556_061;
    private static final int recipesToGo = after + 15 ; // 556_061;
    
    public static void main(String... args) {
        long solution = solveA();
        int solutionB = solveB();
        System.out.println("solution B: " + solutionB);
        
    }
    
    private static long solveA() {
        int player1 = 0;
        int player2 = 1;
        long valuePlayer1 = recipes.get(player1);
        long valuePlayer2 = recipes.get(player2);
        
        while (recipes.size() < recipesToGo) {
//            if (recipes.size() % 10_000 == 0) System.out.println(recipes.size());
            var newValue = "" + (recipes.get(player1) + recipes.get(player2));
            newValue.chars().map(i -> i - '0').boxed().forEach(recipes::add);
            player1 = (player1 + recipes.get(player1) + 1) % recipes.size();
            player2 = (player2 + recipes.get(player2) + 1) % recipes.size();
        }
        
        IntStream.range(after, after + 10).forEach(i -> System.out.print(recipes.get(i)));
        System.out.println();
        int highestScore = recipes.stream().max(Comparator.naturalOrder()).get();
        System.out.println("highest score: " + highestScore);
        return 0;
    }
    
    private static int solveB() {
        List<Integer> listToFind = List.of(5, 5, 6, 0, 6, 1);
        recipes.clear();
        recipes.addAll(List.of(3, 7));
        int player1 = 0;
        int player2 = 1;
        long valuePlayer1 = recipes.get(player1);
        long valuePlayer2 = recipes.get(player2);
        
        while (true) {
            if (recipes.size() % 10_000 == 0) System.out.println(recipes.size());
            var newValue = "" + (recipes.get(player1) + recipes.get(player2));
            newValue.chars().map(i -> i - '0').boxed().forEach(recipes::add);
            player1 = (player1 + recipes.get(player1) + 1) % recipes.size();
            player2 = (player2 + recipes.get(player2) + 1) % recipes.size();
            int t = recipes.size();
            if (t >= listToFind.size() + 2) {
                if (recipes.subList(t - listToFind.size() - 2, t - 2).equals(listToFind)) return t - listToFind.size() - 2;
                if (recipes.subList(t - listToFind.size() - 1, t - 1).equals(listToFind)) return t - listToFind.size() - 1;
                if (recipes.subList(t - listToFind.size(), t        ).equals(listToFind)) return t - listToFind.size();
            }
        }
    }
    
    private static int determineNextPosition(int index, int delta) {
        // this method returns the new position, but in this way:
        // if the array is: 1 2 3 4, and a player standing at position 0, moving
        // 7 further, I thought thr new position would be:
        // 2 3 4 3 2 1 2, so 2. But latee I undestood that reaching the
        // end of the array, the player jumps back to the beginning.... much easier
        int reken = index + delta + 2;
        int newIndex = reken % recipes.size();
        boolean even = reken / recipes.size() % 2 == 0;
        newIndex = even ? newIndex : recipes.size() - newIndex;
        return newIndex;
    }
    
    private static void print(int player1, int player2) {
        String p1 = "(%s) ";
        String p2 = "[%s] ";
        String p3 = " %s ";
        IntStream.range(0, recipes.size()).forEach(i -> {
            System.out.format(i == player1 ?  p1 : i == player2 ? p2 : p3, recipes.get(i));
        });
        System.out.println();
    }
}
