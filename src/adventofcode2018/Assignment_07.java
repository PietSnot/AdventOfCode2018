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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author Piet
 */
public class Assignment_07 {
    
    static Map<Character, List<Character>> input;
    static List<Worker> workers = new ArrayList<>();
    static long nrOfTasksToPerform;
    static int nrOfWorkers = 5;
    static boolean test = false;
    static boolean print = false;
    
    
    public static void main(String... args) {
        processInput();
        solveA();
        solveB();
    }
    
    private static void processInput() {
        var url = test ? Assignment_07.class.getResource("Resources/input_assignment_07_test.txt") :
                         Assignment_07.class.getResource("Resources/input_assignment_07.txt")
        ;
        try {
            var path = Paths.get(url.toURI());
            input = Files.lines(path)
                .collect(groupingBy(s -> s.charAt(36), mapping(s -> s.charAt(5), toList())))
            ;
            
            // determine number of tasks to perform
            Set<Character> set = new HashSet<>();
            input.forEach((k, v) -> {set.add(k); set.addAll(v);});
            nrOfTasksToPerform = set.size();
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("input error!!");
        }
    }
    
    private static void solveA() {
        List<Character> processed = new ArrayList<>();
        TreeSet<Character> toBeProcessed = new TreeSet<>();
        var left = new HashSet<Character>(input.keySet());
        var right = input.values().stream().flatMap(List::stream).collect(toSet());
        right.removeAll(left);
        toBeProcessed.addAll(right);
        
        while (processed.size() < nrOfTasksToPerform) {
            char c = toBeProcessed.first();
            toBeProcessed.remove(c);
            processed.add(c);
            var temp = findAvailableTasks(processed);
            if (temp != null) {
                temp.removeAll(processed);
                toBeProcessed.addAll(temp);
            }
        }
        StringBuilder sb = new StringBuilder();
        processed.forEach(sb::append);
        var resultA = sb.toString();
        System.out.println(resultA);
    }
    
    private static void solveB() {
        List<Character> processed = new ArrayList<>();
        TreeSet<Character> toBeProcessed = new TreeSet<>();
        var left = new HashSet<>(input.keySet());
        var right = input.values().stream().flatMap(List::stream).collect(toSet());
        right.removeAll(left);
        toBeProcessed.addAll(right);
        for (int i = 0; i < nrOfWorkers; i++) workers.add(new Worker(test ? 0 : 60));
        
        int time = 0;
        
        for (time = 0; processed.size() < nrOfTasksToPerform; time++) {
            var finishedTasks = finishedTasks(time);
            processed.addAll(finishedTasks);
            var tasksAvailable = findAvailableTasks(processed);
            tasksAvailable.removeAll(beingProcessedByTheWorkers());
            tasksAvailable.removeAll(processed);
            toBeProcessed.addAll(tasksAvailable);
            for (Worker w: workers) {
                if (toBeProcessed.isEmpty()) break;
                if (w.isAvailable()) {
                    char c = toBeProcessed.first();
                    if (w.newTask(c, time)) toBeProcessed.remove(c);
                }
            }
            if (print) makePrintOut(time, processed, tasksAvailable);
        }
        var solutionB = time - 1;
        System.out.println("solution B: " + solutionB);
    }    
    
    private static List<Character> finishedTasks(int time) {
        var tasks = workers.stream()
            .map(w -> w.deliver(time))
            .filter(task -> task != null)
            .collect(toList())
        ;
        return tasks;
    }
    
    private static List<Character> findAvailableTasks(List<Character> processed) {
        var result = input.entrySet().stream()
            .filter(e -> processed.containsAll(e.getValue()))
            .map(e -> e.getKey())
            .collect(toList())
        ;
        return result;
    }
    
    private static List<Character> beingProcessedByTheWorkers() {
        var list = workers.stream()
            .map(w -> w.busyWithTask)
            .filter(t -> t != null)
            .collect(toList())
        ;
        return list;
    }
    
    private static void makePrintOut(int time, List<Character> processed, List<Character> available) {
        System.out.format("%,5d   ", time);
        workers.forEach(w -> System.out.format("%4s   ", w.busyWithTask));
        System.out.format("%15s", available);
        System.out.format("   %s %n", processed);
    }
}

class Worker {
    Character busyWithTask;
    int starttime, endtime;
    int basicDuration;
    
    public Worker(int basicDuration) {
        this.basicDuration = basicDuration;
    }
    
    public boolean newTask(char task, int time) {
        if (busyWithTask != null) return false;
        busyWithTask = task;
        starttime = time;
        endtime = time + busyWithTask - 'A' +  + basicDuration + 1;
        return true;
    }
    
    public boolean isAvailable() {
        return busyWithTask == null;
    }
    
    public Character deliver(int time) {
        if (busyWithTask == null || time != endtime) return null;
        Character result = busyWithTask;
        busyWithTask = null;
        return result;
    }
}