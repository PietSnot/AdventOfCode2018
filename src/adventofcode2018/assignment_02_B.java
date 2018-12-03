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

public class assignment_02_B {
    
    public static void main(String... args) {
        
        List<String> list;
        try {
            var path = Paths.get("D:/JavaProgs/AdventOfCode2018/src/adventofcode2018/Resources", "input_assignment_02_A.txt");
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
