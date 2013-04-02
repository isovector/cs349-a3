/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author sandy
 */
public class Animation {
    public enum Mode {
        DRAW, SELECT, LASSO, ANIMATE, ERASE
    }
    public Mode mode = Mode.DRAW;
    
    public LinkedList<Actor> actors = new LinkedList<Actor>();
    public LinkedList<Doodle> doodles = new LinkedList<Doodle>();
    
    public Actor currentActor;
    public Doodle activeDoodle;
    public int currentFrame = 0;
    
    public void finishSelect(Rectangle r) { 
        currentActor = new Actor();
        
        actors.add(currentActor);
        
        Iterator<Doodle> it = doodles.iterator();
        doodleloop:
        while (it.hasNext()) {
            Doodle doodle = it.next();
            for (Line line : doodle.lines) {
                if (!line.containedBy(r)) {
                    continue doodleloop;
                }
            }
            it.remove();
            currentActor.doodles.add(doodle);
        }
        currentActor.finalize();
    }
    
    public void finishSelect(Polygon p) { 
        currentActor = new Actor();
        
        actors.add(currentActor);
        
        Iterator<Doodle> it = doodles.iterator();
        doodleloop:
        while (it.hasNext()) {
            Doodle doodle = it.next();
            for (Line line : doodle.lines) {
                if (!line.containedBy(p)) {
                    continue doodleloop;
                }
            }
            it.remove();
            currentActor.doodles.add(doodle);
        }
        currentActor.finalize();
    }
    
    public void clearNewActor() {
        if (currentActor != null) {
            if (!currentActor.committed) {
                for (Doodle doodle : currentActor.doodles) {
                    doodles.add(doodle);
                }

                actors.remove(currentActor);
            }
            
            currentActor = null;
        }
    }
    
    public void serialize(JsonWriter file) throws IOException {
        file.writeObject();
        
        file.writeObject("actors");
        for (Actor a : actors) {
            a.serialize(file);
        }
        file.closeObject();
        
        file.writeObject("doodles");
        for (Doodle doodle : doodles) {
            doodle.serialize(file);
        }
        file.closeObject();
        
        file.closeObject();
    }
}
