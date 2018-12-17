/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class Assignment_16 {
    static Map<String, TriConsumer<Integer, Integer, Integer>> instructions;
    static List<Integer> registers = new ArrayList<>();
    static List<String> input;
    static List<List<String>> answers = new ArrayList<>();
    static Map<Integer, Set<String>> forPartB = new HashMap<>();
    static Map<Integer, String> instructionList = new HashMap<>();
    
    public static void main(String... args) {
        createInstructions();
//        test();
        processInput();
        long solutionA = solveA();
        System.out.println("solution A = " + solutionA);
        int solutionB = solveB();
        System.out.println("solution B = " + solutionB);
    }
    
    private static void createInstructions() {
        instructions = new HashMap<>();
        instructions.put("addr", (a, b, c) -> registers.set(c, registers.get(a) + registers.get(b)));
        instructions.put("addi", (a, b, c) -> registers.set(c, registers.get(a) + b));
        instructions.put("mulr", (a, b, c) -> registers.set(c, registers.get(a) * registers.get(b)));
        instructions.put("muli", (a, b, c) -> registers.set(c, registers.get(a) * b));
        instructions.put("banr", (a, b, c) -> registers.set(c, registers.get(a) & registers.get(b)));
        instructions.put("bani", (a, b, c) -> registers.set(c, registers.get(a) & b));
        instructions.put("borr", (a, b, c) -> registers.set(c, registers.get(a) | registers.get(b)));
        instructions.put("bori", (a, b, c) -> registers.set(c, registers.get(a) | b));
        instructions.put("setr", (a, b, c) -> registers.set(c, registers.get(a)));
        instructions.put("seti", (a, b, c) -> registers.set(c, a));
        instructions.put("gtir", (a, b, c) -> registers.set(c, a > registers.get(b) ? 1 : 0));
        instructions.put("gtri", (a, b, c) -> registers.set(c, registers.get(a) > b ? 1 : 0));
        instructions.put("gtrr", (a, b, c) -> registers.set(c, registers.get(a) > registers.get(b) ? 1 : 0));
        instructions.put("eqir", (a, b, c) -> registers.set(c, a.equals(registers.get(b)) ? 1 : 0));
        instructions.put("eqri", (a, b, c) -> registers.set(c, registers.get(a).equals(b) ? 1 : 0));
        instructions.put("eqrr", (a, b, c) -> registers.set(c, registers.get(a).equals(registers.get(b)) ? 1 : 0));
    }
    
    private static void processInput() {
        try {
            var url = Assignment_16.class.getResource(("Resources/input_assignment_16.txt"));
            var path = Paths.get(url.toURI());
            input = Files.readAllLines(path);
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("error reading and processing input!!!!");
        }
    }
    
    private static void test() {
//        var register = List.of(3, 3, 2, 2);
//        var instruction = List.of(5, 3, 1, 1);
//        System.out.println("*****************************");
//        System.out.println(register);
//        System.out.println(instruction);
//        test(register, instruction).entrySet().forEach(System.out::println);
//        register = List.of(0, 2, 2, 2);
//        instruction = List.of(0, 2, 1, 0);
//        System.out.println("*****************************");
//        System.out.println(register);
//        System.out.println(instruction);
//        test(register, instruction).entrySet().forEach(System.out::println);
//        register = List.of(3, 1, 2, 2);
//        instruction = List.of(2, 3, 2, 2);
//        System.out.println("*****************************");
//        System.out.println(register);
//        System.out.println(instruction);
//        test(register, instruction).entrySet().forEach(System.out::println);
//        var register = List.of(1, 2, 1, 3);
//        var instruction = List.of(6, 2, 2, 2);
//        System.out.println("*****************************");
//        System.out.println(register);
//        System.out.println(instruction);
//        var flup = test(register, instruction);
//        flup.entrySet().forEach(System.out::println);
    }
    
    private static Map<String, List<Integer>> test(List<Integer> register, List<Integer> instruction) {
        Map<String, List<Integer>> answers = new HashMap<>();
        instructions.forEach((k, v) -> {
            registers.clear();
            registers.addAll(register);
            v.process(instruction.get(1), instruction.get(2), instruction.get(3));
            answers.put(k, new ArrayList<>(registers));
        });
        return answers;
    }
   
    private static long solveA() {
        var befores = IntStream.range(0, input.size())
            .filter(i -> input.get(i).startsWith("Before"))
            .boxed()
            .collect(toList())
        ;
        answers.clear();
        for (int i: befores) {
            var output = parseBeforeAndAfter(input.get(i + 2));
            var instruction = Arrays.stream(input.get(i + 1).split("\\s+")).map(Integer::valueOf).collect(toList());
            List<String> temp = new ArrayList<>();
            instructions.forEach((k, v) -> {
                registers = parseBeforeAndAfter(input.get(i));
                v.process(instruction.get(1), instruction.get(2), instruction.get(3));
                if (registers.equals(output)) temp.add(k);
            });
            answers.add(temp);
            forPartB.computeIfAbsent(instruction.get(0), HashSet::new).addAll(temp);
        }
        
        var resultA = answers.stream().filter(a -> a.size() >= 3).count();
        return resultA;
    };
    
    private static int solveB() {
        while (instructionList.size() < 16) {
            for (var e: forPartB.entrySet()) {
                if (e.getValue().size() == 1) {
                    instructionList.putIfAbsent(e.getKey(), e.getValue().stream().findFirst().get());
                }
                else {
                    e.getValue().removeAll(instructionList.values());
                    if (e.getValue().size() == 1) {
                        instructionList.put(e.getKey(), e.getValue().stream().findFirst().get());
                    }
                }
            }
        }
        
        registers = Arrays.asList(0, 0, 0, 0);
        for (int i = 3130; i < input.size(); i++) {
            var instr = Arrays.stream(input.get(i).split("\\s+")).map(Integer::valueOf).collect(toList());
            instructions.get(instructionList.get(instr.get(0))).process(instr.get(1), instr.get(2), instr.get(3));
        }
        return registers.get(0);
    }
    
    private static List<Integer> parseBeforeAndAfter(String s) {
        int first = s.indexOf("[");
        int second = s.indexOf("]");
        s = s.substring(first + 1, second);
        var result = Arrays.stream(s.split(", ")).map(Integer::valueOf).collect(toList());
        return result;
    }
}

interface TriConsumer<A, B, C> {
    void process(A a, B b, C c);
}

