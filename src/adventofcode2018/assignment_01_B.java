/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */

//--- Part Two ---
//You notice that the device repeats the same frequency change list over and over.
//To calibrate the device, you need to find the first frequency it reaches twice.
//
//For example, using the same list of changes above, the device would loop as follows:
//
//Current frequency  0, change of +1; resulting frequency  1.
//Current frequency  1, change of -2; resulting frequency -1.
//Current frequency -1, change of +3; resulting frequency  2.
//Current frequency  2, change of +1; resulting frequency  3.
//(At this point, the device continues from the start of the list.)
//Current frequency  3, change of +1; resulting frequency  4.
//Current frequency  4, change of -2; resulting frequency  2, which has already been seen.
//In this example, the first frequency reached twice is 2. Note that your device 
//might need to repeat its list of frequency changes many times before a duplicate 
//frequency is found, and that duplicates might be found while in the middle of processing the list.
//
//Here are other examples:
//
//+1, -1 first reaches 0 twice.
//+3, +3, +4, -2, -4 first reaches 10 twice.
//-6, +3, +8, +5, -6 first reaches 5 twice.
//+7, +7, -2, -7, -4 first reaches 14 twice.
//What is the first frequency your device reaches twice?
//
//Although it hasn't changed, you can still get your puzzle input.


public class assignment_01_B {
    public static void main(String... args) {
        
        var path = Paths.get("D:/JavaProgs/AdventOfCode2018/src/adventofcode2018/Resources", "assignment_01_A.txt");
        List<Integer> list;
        
        try {
            list = Files.lines(path).map(Integer::valueOf).collect(toList());
        }
        catch (IOException e) {
            System.out.println("input fout!!!!");
            return;
        }
        
        //*********************************************************
        // first method. classic while loop
        //*********************************************************
        var set = new HashSet<Integer>();
        set.add(0);
        var index = 0;
        var runningTotal = 0;
        var nrOfLoops = 1;
        
        while (true) {
            runningTotal += list.get(index);
            if (set.contains(runningTotal)) {
                System.out.println("first double frequency: " + runningTotal);
                System.out.println("nr of loops: " + nrOfLoops);
                break;
            }
            else {
                set.add(runningTotal);
                index++;
                if (index == list.size()) {
                    nrOfLoops++;
                    index = 0;
                }
            }
        }
        
        //**************************************************************
        // second method, with Stream and some innocent side effects
        //**************************************************************
        int[] a = {0};
        set.clear();
        var result = IntStream
            .iterate(0, i -> i == list.size() - 1 ? 0 : i + 1)
            .map(i -> {a[0] += list.get(i); return a[0];})
            .filter(i -> !set.add(i))
            .findFirst()
            .getAsInt()
        ;
        System.out.println("with Stream: " + result);
    }
}
