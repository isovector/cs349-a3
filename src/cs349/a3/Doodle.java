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
    
    public void toRelative(Vector2D origin) {
        for (Line l : lines) {
            l.source = l.source.minus(origin);
            l.dest = l.dest.minus(origin);
        }
    }
    
    public Rectangle getBoundingBox() {
        Rectangle bb = new Rectangle(999999, 999999, -99999, -99999);
        
        for (Line l : lines) {
            bb.x = Math.min(bb.x, (int)Math.min(l.source.x, l.dest.x));
            bb.width = Math.max(bb.width, (int)Math.max(l.source.x, l.dest.x));
            
            bb.y = Math.min(bb.y, (int)Math.min(l.source.y, l.dest.y));
            bb.height = Math.max(bb.height, (int)Math.max(l.source.y, l.dest.y));
        }
        
        bb.width -= bb.x;
        bb.height -= bb.y;
        
        return bb;
    }
}
