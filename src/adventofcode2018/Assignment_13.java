/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Piet
 */
public class Assignment_13 {
    
    static boolean test = false;
    static List<List<Integer>> terrain;
    static List<Car> cars = new ArrayList<>();
    
    public static void main(String... args) {
        processInput();
        createCars();
        System.out.println("found: " + cars.size() + " cars");
//        showTerrain();
        showCars();
        Car car = solveA();
        System.out.format("solution A = %d,%d%n", car.position.y, car.position.x);
        System.out.println("cars : " + cars.size() + " cars");
        Car lastCar = solveB();
        System.out.format("position of lasrt remaining car: %d,%d%n", lastCar.position.y, lastCar.position.x);
    }
    
    private static void processInput() {
        var filename = "input_assignment_13"  + (test ? "_test" : "" ) + ".txt";
        try {
            var url = Assignment_13.class.getResource("Resources/" + filename);
            var path = Paths.get(url.toURI());
            terrain = Files.lines(path)
                .map(s -> s.chars().boxed().collect(toList()))
                .collect(toList());
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("error reading or processing input!!!!");
        }
    }
    
    private static void createCars() {
        cars.clear();
        Car.nrSoFar = 0;
        for (int row = 0; row < terrain.size(); row++) {
            var currentRow = terrain.get(row);
            for (int col = 0; col < currentRow.size(); col++) {
                switch ((int)currentRow.get(col)) {
                    case '<' : cars.add(new Car(row, col, Direction.LEFT)); 
                               break;
                    case '^' : cars.add(new Car(row, col, Direction.UP));
                               break;
                    case '>' : cars.add(new Car(row, col, Direction.RIGHT));
                               break;
                    case 'v' :
                    case 'V' : cars.add(new Car(row, col, Direction.DOWN));
                               break;
                }
            }
        }
    }
    
    private static void showTerrain() {
        for (List<Integer> list: terrain) {
            for (int i: list) {
                System.out.print((char) i);
            }
            System.out.println();
        }
    }
    
    private static void showCars() {
        cars.sort(Car.comp);
        cars.forEach(System.out::println);
    }
    
    private static Car solveA() {
        while (true) {
            cars.sort(Car.comp);
            for (Car car: cars) {
                car.nextPosition();
                if (isCollision(car)) return car;
            }
//            showCars();
        }
    }
    
    private static Car solveB() {
        createCars();
        while (cars.size() > 1) {
            for (Car car: cars) {
                if (car.isCrashed) continue;
                car.nextPosition();
                Optional<Car> crashCar = findCrash(car);
                crashCar.ifPresent(c -> {c.isCrashed = true; car.isCrashed = true;});
            }
            cars.removeIf(c -> c.isCrashed);
        }
        return cars.get(0);
    }
    
    private static boolean isCollision(Car car) {
        var result = cars.stream().filter(c -> c != car).anyMatch(c -> c.position.equals(car.position));
        return result;
    }
    
    private static Optional<Car> findCrash(Car car) {
        var t = cars.stream()
            .filter(c -> c != car)
            .filter(c -> !c.isCrashed)
            .filter(c -> c.position.equals(car.position))
            .findFirst()
        ;
        return t;
    }
}   // end of class 

//**************************************************************************
// Direction
//**************************************************************************
enum Direction {
    UP(-1, 0), RIGHT(0, 1), DOWN(1, 0), LEFT(0, -1);
    
    final int dx;
    final int dy;
    
    //-------------------------------------------
    Direction(int deltax, int deltay) {
        dx = deltax;
        dy = deltay;
    }
    //-------------------------------------------
    private Direction next() {
        switch (Direction.this) {
            case LEFT: return DOWN;
            case UP: return LEFT;
            case RIGHT: return UP;
            case DOWN: return RIGHT;
            default: throw new RuntimeException("unknown direction received in methed 'next'");
        }
    }
    
    //-------------------------------------------
    public static Direction getDirection(char c, Direction current, int nrOfTurn) {
        switch (c) {
            case '/' : 
                switch (current) {
                    case UP: return RIGHT;
                    case RIGHT: return UP;
                    case DOWN: return LEFT;
                    case LEFT: return DOWN;
                }
            case '\\' : 
                switch (current) {
                    case UP: return LEFT;
                    case RIGHT: return DOWN;
                    case DOWN: return RIGHT;
                    case LEFT: return UP;
                }
            case '+' : {
                switch (nrOfTurn) {
                    case 1: return current.next();
                    case 2: return current;
                    case 3: return current.next().next().next();
                    default: throw new RuntimeException("unknown nr of turn received");
                }
            }
            case '-' :
            case 'v' :
            case 'V' :
            case '<' :
            case '^' :
            case '>' :
            case '|': return current;
                     
            default: throw new RuntimeException("unknown character received: " + c);
        }
    }
}  // end of Direction


//*************************************************************************
// Car
//*************************************************************************
class Car implements Comparable<Car> {
    int id;
    static int nrSoFar = 0;
    static List<List<Integer>> terrain;
    Point position;
    Direction direction;
    int nrOfTurns = 0;
    boolean isCrashed = false;
        
    static final Comparator<Car> comp;
    static {
        Comparator<Car> c1 = Comparator.comparingInt(c -> c.position.x);
        Comparator<Car> c2 = Comparator.comparingInt(c -> c.position.y);
        comp = c1.thenComparing(c2);
        terrain = Assignment_13.terrain;
    }
    
    Car(int row, int column, Direction d) {
        position = new Point(row, column);
        direction = d;
        id = nrSoFar;
        nrSoFar++;
    }
    
    public void nextPosition() {
        position = new Point(position.x + direction.dx, position.y + direction.dy);
        char c = (char) ((int) terrain.get(position.x).get(position.y));
        if (c == '+') nrOfTurns = (nrOfTurns % 3) + 1;
        direction = Direction.getDirection((char) c, direction, nrOfTurns);
    }
    
    public void setDirection(char c) {
        if (c == '+') nrOfTurns++;
        direction = Direction.getDirection(c, direction, nrOfTurns);
    }
    
    public int compareTo(Car c) {
        return comp.compare(this, c);
    }
    
    @Override
    public String toString() {
        return String.format("nr: %d, location: %d, %d, direction: %s", id, position.x, position.y, direction);
    }
}   // end of Car