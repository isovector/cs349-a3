package cs349.a3;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

public class Doodle {
    public LinkedList<Line> lines = new LinkedList<Line>();
    
    public void append(Line line) {
        lines.add(line);
    }
    
    public void paint(Graphics g) {
        for (Line l : lines) {
            l.paint(g);
        }
    }
    
    public Actor select(Rectangle r) {
        return new Actor();
    }
}
