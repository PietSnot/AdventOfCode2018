/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */

/******************************************************************************
 * Your puzzle answer was 7410.

* The first half of this puzzle is complete! It provides one gold star: *

* --- Part Two ---

* Confident that your list of box IDs is complete, you're ready to find the 
* boxes full of prototype fabric.

* The boxes will have IDs which differ by exactly one character at the same 
* in both strings. For example, given the following box IDs:

* abcde
* fghij
* klmno
* pqrst
* fguij
* axcye
* wvxyz

* The IDs abcde and axcye are close, but they differ by two characters (the 
* second and fourth). However, the IDs fghij and fguij differ by exactly one 
* character, the third (h and u). Those must be the correct boxes.

* What letters are common between the two correct box IDs? (In the example 
* above, this is found by removing the differing character from either ID, 
* producing fgij.)

* Although it hasn't changed, you can still get your puzzle input. 
 *
 */

public class assignment_02_B {
    
    public static void main(String... args) {
        
        List<String> list;
        try {
            var path = Paths.get("D:/JavaProgs/AdventOfCode2018/src/adventofcode2018/Resources", "assignment_02_A.txt");
            list = Files.readAllLines(path);
        }
        catch (IOException e) {
            System.out.println("error reading input file....");
            return;
        }
        
        IntStream.range(0, list.get(0).length()).forEach(i -> {
            var temp = list.stream()
                .collect(groupingBy(s -> removeChar(s, i), counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 2)
                .collect(toList())
            ;
            if (!temp.isEmpty()) System.out.println(temp);
        });     
    }
    
    private static String removeChar(String s, int pos) {
        var sb = new StringBuilder(s);
        sb.deleteCharAt(pos);
        return sb.toString();                 
    }
}
