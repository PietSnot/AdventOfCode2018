/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Piet
 * 
 */

public class assignment_01_A {
    public static void main(String... args) {
        var path = Paths.get("D:/JavaProgs/AdventOfCode2018/src/adventofcode2018/Resources", "assignment_01_A.txt");
        try {
            var result = Files.lines(path).mapToInt(Integer::parseInt).sum();
            System.out.format("result = %,d %n", result);
        }
        catch (IOException e) {
            System.out.println("could not read input fiule");
        }
    }
}