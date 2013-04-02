/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author sandy
 */
public class Actor {
    public Path path;
    
    public LinkedList<Doodle> doodles = new LinkedList<Doodle>();
    public Vector2D position = null;
    public Vector2D origin = new Vector2D();
    public Vector2D size;
    public Rectangle boundingBox;
    public boolean committed = false;
    
    public void finalize() {
        boundingBox = new Rectangle(9999999, 9999999, -9999999, -9999999);
        
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
        origin = size.scalarMult(0.5);
    }
    
    public void commit(int frame) {
        if (committed) {
            return;
        }

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
    
    public void record(boolean on, int frame) {
        if (on) {
            position = path.at(frame);
            path.eraseAfter(frame);
        } else {
            moveTo(frame, position);
            position = null;
        }
    }
    
    public void moveTo(int frame, Vector2D pos) {
        path.add(new SpaceTime(frame, pos));
    }
    
    public void paint(Graphics g, int frame) {
        if (committed) {
            Vector2D pos = (position != null) ? position : path.at(frame);
            g.translate((int)pos.x, (int)pos.y);
        }
        
        for (Doodle doodle : doodles) {
            doodle.paint(g, frame);
        }
    }
    
    public void paintRect(Graphics g, int frame) {
        g.setColor(Color.RED);
        
        int x,y;
        int w = boundingBox.width; 
        int h = boundingBox.height;
        
        if (!committed) {
            x = boundingBox.x;
            y = boundingBox.y; 
        } else {
            Vector2D pos = (position != null) ? position : path.at(frame);
            x = (int)pos.x;
            y = (int)pos.y;
        }
        
        g.drawRect(x, y, w, h);
        g.fillOval(x + w / 2 - 5, y + h / 2 - 5, 10, 10);
        g.drawOval(x + w / 2 - 10, y + h / 2 - 10, 20, 20);
    }

    void erase(int frame, Line line) {
        line.toRelative(path.at(frame));
        for (Doodle doodle : doodles) {
            doodle.erase(frame, line);
        }
    }
    
    public void serialize(JsonWriter file) throws IOException {
        file.writeObject();

        file.writeObject("doodles");
        for (Doodle doodle : doodles) {
            doodle.serialize(file);
        }
        file.closeObject();
        
        file.writeObject("path");
        for (SpaceTime st : path) {
            st.serialize(file);
        }
        file.closeObject();
        
        file.closeObject();
    }
}
