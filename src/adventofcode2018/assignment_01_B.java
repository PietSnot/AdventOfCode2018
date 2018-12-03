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

public class assignment_01_B {
    public static void main(String... args) {
        
        var path = Paths.get("D:/JavaProgs/AdventOfCode2018/src/adventofcode2018/Resources", "input_assignment_01_A.txt");
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
            if (!set.add(runningTotal)) {
                System.out.println("first double frequency: " + runningTotal);
                System.out.println("nr of loops: " + nrOfLoops);
                break;
            }
            else {
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
