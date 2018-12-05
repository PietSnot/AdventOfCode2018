/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Piet
 */
public class assignment_05_A {
    private static String input;
    
    public static void main(String... args) {
        processInput();
        System.out.format("length of input: %,d %n", input.length());
        var sb = new StringBuilder();
        var solutionA = eliminatePolarities(input);
        System.out.format("solution A = %,d %n", solutionA);
        var solutionB = IntStream.rangeClosed('A', 'Z')
            .map(c -> eliminatePolarities(removeChars(input, (char) c)))
            .min()
            .getAsInt()
         ;   
         System.out.format("Solution B = %,d %n", solutionB);
    }
    
    private static void processInput() {
        var url = assignment_05_A.class.getResource("Resources/input_assignment_05_A.txt");
        try {
            var path = Paths.get(url.toURI());
            var strings = Files.readAllLines(path);
            input = strings.get(0);
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Error reading input!!!!");
        }
    }
    
    private static int eliminatePolarities(String input) {
        int difference = 'a' - 'A';
        var sb = new StringBuilder();
        for (var c: input.toCharArray()) {
            if (sb.length() == 0) {
                sb.append(c);
            }
            else if (Math.abs(c - sb.charAt(sb.length() - 1)) == difference) {
                sb.deleteCharAt(sb.length() - 1);
            }
            else sb.append(c);
        }
        return sb.length();
    }

    private static String removeChars(String s, char c) {
        var one = "" + c;
        var two = one.toLowerCase();
        var temp = s.replaceAll(one, "").replaceAll(two, "");
        return temp;
    }
}
