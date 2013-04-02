/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.io.IOException;

/**
 *
 * @author sandy
 */
public class SpaceTime {
    public Vector2D position;
    public int delta;
    
    public SpaceTime(int delt, Vector2D pos) {
        position = pos;
        delta = delt;
    }
    
    public void serialize(JsonWriter file) throws IOException {
        file.writeObject();
        file.write(delta);
        position.serialize(file);
        file.closeObject();
    }
}
