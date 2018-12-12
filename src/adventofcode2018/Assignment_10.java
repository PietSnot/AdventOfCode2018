/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adventofcode2018;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import javax.imageio.ImageIO;

/**
 *
 * @author Piet
 */
public class Assignment_10 {
    static List<Data10> input;
    
    public static void main(String... args) {
        processInput();
        List<Data10> previous = input;
        long area = area(input);
        for(int iteration = 1; ; iteration++) {
            List<Data10> next = getNextLocations(previous);
            long areaNew = area(next);
//            System.out.format("area: %8d, ieration: %,6d%n", area, iteration);
            if (areaNew > area) {
                IntSummaryStatistics w = previous.stream().mapToInt(d -> d.location.x).summaryStatistics();
                IntSummaryStatistics h = previous.stream().mapToInt(d -> d.location.y).summaryStatistics();
                int width = w.getMax() - w.getMin();
                int height = h.getMax() - h.getMin();
                System.out.format("min area: width = %d, height = %d%n", width, height);
                createImage(previous);
                break;
            }
            else {
                area = areaNew;
                previous = next;
            }
        }
    }
    
    private static void processInput() {
        var url = Assignment_10.class.getResource("Resources/input_assignment_10.txt");
        try {
            var path = Paths.get(url.toURI());
            input = Files.lines(path)
                .map(Assignment_10::processInputstring)
                .collect(toList());
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("error reading input!!!!!");
        }
    }
    
    private static Data10 processInputstring(String s) {
        // input = position=<-52775,  31912> velocity=< 5, -3>
        int first = s.indexOf("<");
        int comma = s.indexOf(",");
        int second = s.indexOf(">");
        int x = Integer.parseInt(s.substring(first + 1, comma).trim());
        int y = Integer.parseInt(s.substring(comma + 1, second).trim());
        first = s.lastIndexOf("<");
        comma = s.lastIndexOf(",");
        second = s.lastIndexOf(">");
        int z = Integer.parseInt(s.substring(first + 1, comma).trim());
        int w = Integer.parseInt(s.substring(comma + 1, second).trim());
        return Data10.of(x, y, z, w);
    }
    
    private static long area(List<Data10> list) {
        IntSummaryStatistics x = list.stream().mapToInt(d -> d.location.x).summaryStatistics();
        IntSummaryStatistics y = list.stream().mapToInt(d -> d.location.y).summaryStatistics();
        long w = x.getMax() - x.getMin() + 1;
        long h = y.getMax() - y.getMin() + 1;
        long area = w * h;
        return area;
    }
    
    private static List<Data10> getNextLocations(List<Data10> list) {
        var result = list.stream()
            .map(Data10::nextLocation)
            .collect(toList())
        ;
        return result;
    }
    
    private static void createImage(List<Data10> list) {
        IntSummaryStatistics w = list.stream().mapToInt(d -> d.location.x).summaryStatistics();
        IntSummaryStatistics h = list.stream().mapToInt(d -> d.location.y).summaryStatistics();
        int width = w.getMax() - w.getMin() + 1;
        int height = h.getMax() - h.getMin() + 1;
        BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Color c = Color.BLACK;
        int rgb = c.getRGB();
        for (Data10 p: list) {
            int col = p.location.x - w.getMin();
            int row = p.location.y - h.getMin();
            buf.setRGB(col, row, rgb);
        }
        File f = new File("D:\\JavaProgs\\AdventOfCode2018\\src\\adventofcode2018\\Resources\\assignment_10.png");
        try {
            ImageIO.write(buf, "png", f);
        }
        catch (IOException e) {
            throw new RuntimeException("writing failed....");
        }
    }
}

class Data10 {
    Point location;
    Point speed;
    
    private Data10(int col, int row, int vx, int vy) {
        location = new Point(col, row);
        speed = new Point(vx, vy);
    }
    
    public static Data10 of(int x, int y, int vx, int vy) {
        return new Data10(x, y, vx, vy);
    }
    
    public Data10 nextLocation() {
        return new Data10(location.x + speed.x, location.y + speed.y, speed.x, speed.y);
    }
}
