/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.io.BufferedWriter;
import java.io.IOException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author sandy
 */
public class AnimWriter implements AnimSerializer {
    private BufferedWriter file;
    
    public AnimWriter(BufferedWriter writer) {
        file = writer;
    }
    
        @Override
    public void close() throws IOException {
        file.close();
    }
    
    private void write(String s) throws IOException {
        file.write(s);
        file.newLine();
    }
    
    @Override
    public int serialize(int value) throws IOException {
        write(Integer.toString(value));
        return value;
    }

    @Override
    public boolean serialize(boolean value) throws IOException {
        write(Boolean.toString(value));
        return value;
    }

    @Override
    public double serialize(double value) throws IOException {
        write(Double.toString(value));
        return value;
    }

    @Override
    public void serialize(Object value) throws IOException {
        if (value instanceof Integer) {
            serialize((int)value);
        } else if (value instanceof Double) {
            serialize((double)value);
        } else if (value instanceof Boolean) {
            serialize((boolean)value);
        } else if (value instanceof AnimSerializable) {
            ((AnimSerializable)value).serialize(this);
        } else {
            throw new NotImplementedException();
        }
    }
}
