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
public class SpaceTime implements AnimSerializable {
    public Vector2D position;
    public int delta;
    
    public SpaceTime(int delt, Vector2D pos) {
        position = pos;
        delta = delt;
    }
    
    @Override
    public void serialize(AnimSerializer json) throws IOException {
        json.serialize(position);
        json.serialize(delta);
    }
}
