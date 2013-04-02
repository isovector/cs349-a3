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
public interface AnimSerializable {
    public void serialize(AnimSerializer json) throws IOException;
}
