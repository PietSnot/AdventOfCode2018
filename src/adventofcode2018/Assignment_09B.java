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
import java.util.stream.Stream;

/**
 *
 * @author Piet
 */
public class Assignment_09B {
    
    static int NR_OF_PLAYERS;
    static int NR_OF_MARBLES;
    static boolean TEST = false;
    static boolean PRINT = false;
    static int LISTSIZE = 2500;
    static ArrayList<LinkedList<Integer>> game;
    
    public static void main(String... args) {
        initialize(30, 5808);
        long start = System.currentTimeMillis();
        long solutionB = playGameVersionB();
        long end = System.currentTimeMillis();
        System.out.println("solution B = " + solutionB);
        System.out.println("took " + (end - start) / 1000. + " seconds");
    }
    
    private static void initialize(int players, int marbles) {
        NR_OF_PLAYERS = TEST ? players :
                               458;
        NR_OF_MARBLES = TEST ? marbles :
                               71308 * 10;
    }
    
    private static long playGameVersionB() {
        game = new ArrayList<>();
        long[] players = new long[NR_OF_PLAYERS];
        IntStream.range(0, NR_OF_MARBLES / LISTSIZE + 1).forEach(i -> game.add(new LinkedList<>()));
        game.get(0).add(0);
        game.get(0).add(1);
        int currentLogicalIndex = 1;
        int currentPlayer = 2;
        int currentLogicalArraylength = 2;
        
        for (int currentMarble = 2; currentMarble < NR_OF_MARBLES; currentMarble++) {
            if (currentMarble % 100_000 == 0) System.out.println(currentMarble / 100_000);
            if (currentMarble % 23 == 0) {
                int logicalIndexToBeRemoved = currentLogicalIndex - 7;
                if (logicalIndexToBeRemoved < 0) logicalIndexToBeRemoved += currentLogicalArraylength;
                Data p = translateLogicalToPhysical(logicalIndexToBeRemoved, currentLogicalArraylength);
                players[currentPlayer] += currentMarble + game.get(p.physicalArray).remove(p.physicalIndex);
                currentLogicalIndex = logicalIndexToBeRemoved;
                currentLogicalArraylength--;
                adjustPhysicalArrays(p, false);
                p = translateLogicalToPhysical(currentLogicalIndex, currentLogicalArraylength);
                if (PRINT) print(currentPlayer, currentMarble, p);
                currentPlayer = (currentPlayer + 1) % NR_OF_PLAYERS;
            }
            else {
                currentLogicalIndex += 2;
                Data p = translateLogicalToPhysical(currentLogicalIndex, currentLogicalArraylength);
                store(currentMarble, p);
                currentLogicalIndex = p.logicalIndex;
                currentLogicalArraylength++;
                if (PRINT) print(currentPlayer, currentMarble, p);
                currentPlayer = (currentPlayer + 1) % players.length;  
            }
        }
        return Arrays.stream(players).max().getAsLong();        
    }
    
    private static Data translateLogicalToPhysical(int logicalIndex, int logicalArraylength) {
        if (logicalIndex != logicalArraylength) logicalIndex %= logicalArraylength;
        Data p = Data.of(logicalIndex / LISTSIZE, logicalIndex % LISTSIZE, logicalIndex);
        return p;
    }

    private static void store(int marble, Data d) {
        if (d.physicalIndex == game.get(d.physicalArray).size()) game.get(d.physicalArray).add(marble);
        else game.get(d.physicalArray).add(d.physicalIndex, marble);
        adjustPhysicalArrays(d, true);
    }
    
    private static void adjustPhysicalArrays(Data d, boolean add) {
        if (add) {
//            Stream.iterate(d.physicalArray, 
//                           i -> game.get(i).size() > LISTSIZE, 
//                           i -> {game.get(i + 1).add(0, game.get(i).removeLast()); return i + 1;}
//            );
            for (int array = d.physicalArray; array < game.size(); array++) {
                if (game.get(array).size() > LISTSIZE) {
                    game.get(array + 1).add(0, game.get(array).removeLast());
                }
                else break;
            }
        }
        else {
            for (int array = d.physicalArray; array < game.size(); array++) {
                if (game.get(array).size() == LISTSIZE - 1 && !game.get(array + 1).isEmpty()) {
                    game.get(array).add(game.get(array + 1).removeFirst());
                }
                else break;
            }
        }
    }
    
    private static void print(int currentPlayer, int currentMarble, Data d) {
        System.out.format("[%d] ", currentPlayer);
        for (int array = 0; array < game.size(); array++) {
            if (game.get(array).isEmpty()) break;
            for (int index = 0; index < game.get(array).size(); index++) {
                if (array == d.physicalArray && index == d.physicalIndex) System.out.format("[%d] ", game.get(array).get(index));
                else System.out.format("%d ", game.get(array).get(index));
            }
        }
        System.out.println();
    }
}

class Data {
    int physicalArray;
    int physicalIndex;
    int logicalIndex;
    
    private Data(int physicalArray, int physicalIndex, int logicalIndex) {
        this.physicalArray = physicalArray;
        this.physicalIndex = physicalIndex;
        this.logicalIndex =  logicalIndex;
    }
    
    public static Data of(int arraynr, int index, int newIndex) {
        return new Data(arraynr, index, newIndex);
    }
    
    @Override
    public String toString() {
        return String.format("phys.array: %,d  phys,index: %,d, logical index", physicalArray, physicalIndex, logicalIndex);
    }
}
