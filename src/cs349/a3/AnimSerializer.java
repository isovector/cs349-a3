/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author sandy
 */
public interface AnimSerializer {
    void close() throws IOException;
    
    int serialize(int value) throws IOException;
    boolean serialize(boolean value) throws IOException;
    double serialize(double value) throws IOException;
    void serialize(Object value) throws IOException;
}
