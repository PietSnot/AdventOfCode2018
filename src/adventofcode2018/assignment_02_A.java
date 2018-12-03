/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import java.util.stream.Stream;

/**
 *
 * @author Piet
 */

public class assignment_02_A {
    
    public static void main(String... args) {
        try {
            var path = Paths.get("D:/JavaProgs/AdventOfCode2018/src/adventofcode2018/Resources", "input_assignment_02_A.txt");
            var frequencies = Files
                .lines(path)
                .flatMap(assignment_02_A::frequencySet)
                .filter(i -> i == 2 || i == 3)
                .collect(groupingBy(i -> i, counting()))
            ;
//            frequencies.entrySet().forEach(System.out::println);
            var twos = frequencies.get(2L);
            var threes = frequencies.get(3L);
            System.out.format("nr of 2's: %,d%n", twos);  
            System.out.format("nr of 3's: %,d%n", threes);  
            System.out.format("product is: %,d%n", twos * threes);
        }
        catch(IOException e) {
            System.out.println("error reading inputfile");
            return;
        }
    }
    
    private static Stream<Long> frequencySet(String s) {
        var map = s.chars().boxed().collect(Collectors.groupingBy(i -> i, counting()));
        return map.values().stream().distinct();
    }
}
