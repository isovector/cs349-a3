/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

/**
 *
 * @author sandy
 */
public class JsonWriter extends BufferedWriter {
    Stack<Boolean> started = new Stack<Boolean>();
    
    public JsonWriter(Writer writer) {
        super(writer);
        started.push(false);
    }
    
    public void writeHead(String name) throws IOException {
        if (started.peek()) {
            write(",");
            newLine();
        }
        
        write(name);
        write(": ");
        
        setWritten(false);
    }
    
    private void setWritten(boolean set) {
        if (started.peek() != set) {
            started.pop();
            started.push(set);
        }
    }
    
    public void write(String name, String value) throws IOException {
        writeHead(name);
        write(value);
        setWritten(true);
    }
    
    public void write(int value) throws IOException {
        if (started.peek()) {
            write(",");
            newLine();
        }
        
        write(Integer.toString(value));
        setWritten(true);
    }
    
    public void write(String name, int value) throws IOException {
        write(name, Integer.toString(value));
    }
    
    public void write(String name, boolean value) throws IOException {
        write(name, value ? "true" : "false");
    }
    
    public void writeObject() throws IOException {
        if (started.peek()) {
            write(",");
            newLine();
        }
        
        write("{");
        newLine();
        
        started.push(false);
    }
    
    public void writeObject(String name) throws IOException {
        writeHead(name);
        writeObject();
    }
    
    public void closeObject() throws IOException {
        newLine();
        write("}");
        started.pop();
        setWritten(true);
    }
}
