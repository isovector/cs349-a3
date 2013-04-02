/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author sandy
 */
public class AnimReader implements AnimSerializer {
    BufferedReader file;
    
    public AnimReader(BufferedReader reader) {
        file = reader;
    }
    
    @Override
    public void close() throws IOException {
        file.close();
    }
    
    @Override
    public int serialize(int value) throws IOException {
        return Integer.parseInt(file.readLine());
    }

    @Override
    public boolean serialize(boolean value) throws IOException {
        return Boolean.parseBoolean(file.readLine());
    }

    @Override
    public double serialize(double value) throws IOException {
        return Double.parseDouble(file.readLine());
    }

    @Override
    public void serialize(Object value) throws IOException {
        if (value instanceof AnimSerializable) {
            ((AnimSerializable)value).serialize(this);
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
}
