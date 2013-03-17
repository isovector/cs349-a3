/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author sandy
 */
public class Actor {
    public Path path;
    
    public LinkedList<Doodle> doodles = new LinkedList<Doodle>();
    public Vector2D size;
    public Rectangle boundingBox;
    public boolean committed = false;
    
    public int firstFrame = 0;
    public int lastFrame = 999999;
    
    public void finalize() {
        boundingBox = new Rectangle(99999, 99999, -99999, -99999);
        
        for (Doodle doodle : doodles) {
            Rectangle bb = doodle.getBoundingBox();
            bb.width += bb.x;
            bb.height += bb.y;
            
            boundingBox.x = Math.min(bb.x, boundingBox.x);
            boundingBox.y = Math.min(bb.y, boundingBox.y);
            
            boundingBox.width = Math.max(bb.width, boundingBox.width);
            boundingBox.height = Math.max(bb.height, boundingBox.height);
        }
        
        boundingBox.width -= boundingBox.x;
        boundingBox.height -= boundingBox.y;
        
        size = new Vector2D(boundingBox.width, boundingBox.height);
    }
    
    public void commit(int frame) {
        if (committed) {
            return;
        }
        
        firstFrame = frame;
        committed = true;
        
        path = new Path();
        
        Vector2D origin = new Vector2D(boundingBox.x, boundingBox.y);
        moveTo(frame, origin);
        
        for (Doodle doodle : doodles) {
            doodle.toRelative(origin);
        }
    }
    
    public boolean hitTest(int frame, Vector2D point) {
        return false;
    }
    
    public void moveTo(int frame, Vector2D pos) {
        path.add(new SpaceTime(frame, pos));
    }
    
    public void paint(Graphics g, int frame) {
        if (frame < firstFrame || frame > lastFrame) {
            return;
        }
        
        if (committed) {
            Vector2D pos = path.at(frame);
            g.translate((int)pos.x, (int)pos.y);
        }
        
        for (Doodle doodle : doodles) {
            doodle.paint(g);
        }
    }
    
    public void paintRect(Graphics g, int frame) {
        g.setColor(Color.RED);
        
        if (!committed) {
            g.drawRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        } else {
            Vector2D pos = path.at(frame);
            g.drawRect((int)pos.x, (int)pos.y, boundingBox.width, boundingBox.height);
        }
    }
}
