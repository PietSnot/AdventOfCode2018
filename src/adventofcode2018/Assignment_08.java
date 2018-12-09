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
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class Assignment_08 {
    
    private static Node root;
    private static List<Integer> input;
    static int index;
    static int level;
    static boolean test = false;
    
    public static void main(String... args) {
        getInput();
        createTree();
        int solutionA = determineSumOfMetas(root);
        System.out.println("solution A = " + solutionA);
        int solutionB = root.getValue();
        System.out.println("solution B = " + solutionB);
    }
    
    private static void getInput() {
        if (test) {
            input = List.of(2, 3, 0, 3, 10, 11, 12, 1, 1, 0, 1, 99, 2, 1, 1, 2);
            return;
        }
        var url = Assignment_08.class.getResource("Resources/input_assignment_08.txt");
        try {
            var path = Paths.get(url.toURI());
            var temp = Files.readAllLines(path);
            var strings = temp.get(0).trim().split("\\s+");
            input = Arrays.stream(strings).map(Integer::valueOf).collect(toList());
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("error reading input!!!");
        }
    }
    
    private static void createTree() {
        root = new Node();
        int nrOfSubnodesToMake = input.get(0);
        int nrOfMetas = input.get(1);
        index = 2;
        level = 0;
        constructTree(root, nrOfSubnodesToMake);
        IntStream.range(index, input.size()).forEach(i -> root.meta.add(input.get(i)));
        index += nrOfMetas;
    }
    
    private static void constructTree(Node currentNode, int nrOfNodesToMake) {
        level++;
//        System.out.println(level);
        for (int count = 0; count < nrOfNodesToMake; count++) {
            if (input.get(index) == 0) {
                // no subnodes
                index++;
                int nrOfMetas = input.get(index);
                Node newNode = new Node();
                index++;
                int countTo = index + nrOfMetas;
                for (int j = index; j < countTo; j++) newNode.meta.add(input.get(j));
                index = countTo;
                currentNode.nodes.add(newNode);
            }
            else {
                // there are subnotes
                Node newNode = new Node();
                int subnodesToMake = input.get(index);
                index++;
                int nrOfMetas = input.get(index);
                index++;
                constructTree(newNode, subnodesToMake);
                int countTo = index + nrOfMetas;
                for (int j = index; j < countTo; j++) newNode.meta.add(input.get(j));
                currentNode.nodes.add(newNode);
                index = countTo;
            }
        }
        level--;
//        System.out.println(level);
    }
    
    private static int determineSumOfMetas(Node node) {
        int sum = node.sumOfMetas();
        int sumOfMetas = node.nodes.stream().mapToInt(Assignment_08::determineSumOfMetas).sum();
        return sum + sumOfMetas;
    }
}

class Node {
    final List<Node> nodes = new ArrayList<>();
    List<Integer> meta = new ArrayList<>();
    
    public int sumOfMetas() {
        return meta.stream().mapToInt(i -> i).sum();
    }
    
    public int getValue() {
        if (nodes.isEmpty()) return meta.stream().mapToInt(i -> i).sum();
        else {
            return meta.stream()
                .filter( i -> i <= nodes.size())
                .mapToInt(i -> nodes.get(i - 1).getValue())
                .sum()
             ;
        }  
    }
    
    @Override
    public String toString() {
        return String.format("Nodes: %s %n metadata: %s", nodes, meta);
    }
}
