/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class Assignment_09 {
    
    static int NR_OF_PLAYERS;
    static int NR_OF_MARBLES;
    static boolean TEST = false;
    static boolean PRINT = false;
    static ArrayList<Integer> game;
    
    public static void main(String... args) {
        initialize(30, 5808);
        long start = System.currentTimeMillis();
        int solutionA = playGameVersionA();
        long end = System.currentTimeMillis();
        System.out.println("solution A = " + solutionA);
        System.out.println("took " + (end - start) / 1000. + " seconds");
    }
    
    private static void initialize(int players, int marbles) {
        NR_OF_PLAYERS = TEST ? players :
                             458;
        NR_OF_MARBLES = TEST ? marbles :
                             71308;
    }
        
    private static int playGameVersionA() {
        game = new ArrayList<>(NR_OF_MARBLES);
        int[] players = new int[NR_OF_PLAYERS];
        game.add(0);
        game.add(1);
        int currentIndex = 1;
        int currentPlayer = 2;
        int currentArraylength = 2;
        
        for (int currentMarble = 2; currentMarble < NR_OF_MARBLES; currentMarble++) {
//            if (currentMarble % 10_000 == 0) System.out.println(currentMarble);
            if (currentMarble % 23 == 0) {
                int indexToBeRemoved = currentIndex - 7;
                if (indexToBeRemoved < 0) indexToBeRemoved += currentArraylength;
                players[currentPlayer] += currentMarble + game.remove(indexToBeRemoved);
                currentIndex = indexToBeRemoved;
                currentArraylength--;
                if (PRINT) print(currentPlayer, currentMarble, players, currentIndex);
                currentPlayer = (currentPlayer + 1) % NR_OF_PLAYERS;
            }
            else {
                currentIndex += 2;
                if (currentIndex != currentArraylength) currentIndex %= currentArraylength;
                game.add(currentIndex, currentMarble);
                currentArraylength++;
                if (PRINT) print(currentPlayer, currentMarble, players, currentIndex);
                currentPlayer = (currentPlayer + 1) % players.length;  
            }
        }
        return Arrays.stream(players).max().getAsInt();        
    }
    
    private static void print(int currentPlayer, int currentMarble, int[] players, int currentIndex) {
        System.out.format("[%,4d: %,4d] ", currentPlayer, players[currentPlayer]);
        for (int i: game) {
            System.out.format(i == currentIndex ? "[%d]" : "%d", i);
        }
        System.out.println();
    }
}