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
public class Animation implements AnimSerializable {
    public enum Mode {
        DRAW, SELECT, LASSO, ANIMATE, ERASE
    }
    public Mode mode = Mode.DRAW;
    
    public LinkedList<Actor> actors = new LinkedList<Actor>();
    public LinkedList<Doodle> doodles = new LinkedList<Doodle>();
    
    public Actor currentActor;
    public Doodle activeDoodle;
    public int currentFrame = 0;
    
    @Override
    public void serialize(AnimSerializer json) throws IOException {
        if (json instanceof AnimWriter) {
            json.serialize(actors.size());
            for (Actor actor : actors) {
                json.serialize(actor);
            }
            
            json.serialize(doodles.size());
            for (Doodle doodle : doodles) {
                json.serialize(doodle);
            }
        } else {
            int size = json.serialize(0);
            for (int i = 0; i < size; ++i) {
                Actor actor = new Actor();
                json.serialize(actor);
                actors.add(actor);
            }
            
            size = json.serialize(0);
            for (int i = 0; i < size; ++i) {
                Doodle doodle = new Doodle();
                json.serialize(doodle);
                doodles.add(doodle);
            }
        }
    }
    
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
}
