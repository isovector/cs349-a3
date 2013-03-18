/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.Timer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author sandy
 */
public class Canvas extends javax.swing.JPanel {
    public enum Mode {
        DRAW, SELECT, LASSO, ANIMATE, ERASE
    }
    
    public LinkedList<Actor> actors = new LinkedList<Actor>();
    public Actor currentActor;
    
    public LinkedList<Doodle> doodles = new LinkedList<Doodle>();
    public Doodle activeDoodle;
    public Mode mode = Mode.DRAW;
    public int currentFrame = 0;
    
    int startX, startY, lastX, lastY;
    
    Color white = new Color(0xFFFFFF);
    Color black = new Color(0);
    BasicStroke selectStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 8.0f, 3.0f, 2.0f, 3.0f }, 0.0f);
    AffineTransform identityTransform = new AffineTransform();
    
    
    Timer recordTimer = new Timer(1000 / 24, new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            currentActor.moveTo(currentFrame, currentActor.position);
            currentFrame++;
        }
    });
            
    /**
     * Creates new form Canvas
     */
    public Canvas() {
        initComponents();
    }
    
    public void setMode(Mode m) {
        mode = m;
    }
    
    public void finishSelect() {
        Rectangle r = null;
        Polygon p = null;
        if (mode == Mode.SELECT) {
            int x0 = Math.min(startX, lastX), x1 = Math.max(startX, lastX);
            int y0 = Math.min(startY, lastY), y1 = Math.max(startY, lastY);
            r = new Rectangle(x0, y0, x1 - x0, y1 - y0);
        } else if (mode == Mode.LASSO) {
            int xs[] = new int[activeDoodle.lines.size()];
            int ys[] = new int[activeDoodle.lines.size()];
            
            int i = 0;
            for (Line l : activeDoodle.lines) {
                xs[i] = (int)l.source.x;
                ys[i] = (int)l.source.y;
                ++i;
            }
            
            p = new Polygon(xs, ys, xs.length);
            activeDoodle = new Doodle();
            activeDoodle.firstFrame = currentFrame;
        }
        
        currentActor = new Actor();
        
        actors.add(currentActor);
        
        Iterator<Doodle> it = doodles.iterator();
        doodleloop:
        while (it.hasNext()) {
            Doodle doodle = it.next();
            for (Line line : doodle.lines) {
                if (mode == Mode.SELECT) {
                    if (!line.containedBy(r)) {
                        continue doodleloop;
                    }
                } else {
                    if (!line.containedBy(p)) {
                        continue doodleloop;
                    }
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
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(white);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        for (Doodle doodle : doodles) {
            doodle.paint(g, currentFrame);
        }
        
        for (Actor actor : actors) {
            actor.paint(g, currentFrame);
            ((Graphics2D)g).setTransform(identityTransform);
        }
        
        if (currentActor != null) {
            currentActor.paintRect(g, currentFrame);
        }
        
        if (mode == Mode.SELECT || mode == Mode.LASSO) {
            g.setColor(black);
            ((Graphics2D)g).setStroke(selectStroke);
            
            if (mode == Mode.SELECT) {
                int x0 = Math.min(startX, lastX), x1 = Math.max(startX, lastX);
                int y0 = Math.min(startY, lastY), y1 = Math.max(startY, lastY);

                g.drawRect(x0, y0, x1 - x0, y1 - y0);
            } else {
                activeDoodle.paint(g, currentFrame);
                if (activeDoodle.lines.size() > 2) {
                    new Line(0, activeDoodle.lines.peekFirst().source, activeDoodle.lines.peekLast().dest).paint(g);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (mode == Mode.SELECT) {
            for (Actor actor : actors) {
                if (actor.hitTest(currentFrame, new Vector2D(evt.getX(), evt.getY()))) {
                    currentActor = actor;
                    break;
                }
            }
        }
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (mode == Mode.DRAW || mode == Mode.LASSO) {
            activeDoodle.append(new Line(0, startX, startY, evt.getX(), evt.getY()));
            startX = evt.getX();
            startY = evt.getY();
        } else if (mode == Mode.SELECT) {
            lastX = evt.getX();
            lastY = evt.getY();
        } else if (mode == Mode.ANIMATE && currentActor != null) {
            currentActor.position = new Vector2D(evt.getX(), evt.getY()).minus(currentActor.origin);
        } else if (mode == Mode.ERASE) {
            Line line = new Line(0, startX, startY, evt.getX(), evt.getY());
            
            for (Doodle doodle : doodles) {
                doodle.erase(currentFrame, line);
            }
            
            for (Actor actor : actors) {
                actor.erase(currentFrame, line);
            }
            
            startX = evt.getX();
            startY = evt.getY();
        }
        
        repaint();
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        startX = evt.getX();
        startY = evt.getY();
        
        if (mode != Mode.ANIMATE) {
            clearNewActor();
        } else {
            if (currentActor != null) {
                if (!currentActor.committed) {
                    currentActor.commit(currentFrame);
                }
                
                currentActor.record(true, currentFrame);
                recordTimer.start();
            }
        }
        
        if (mode == Mode.DRAW || mode == Mode.LASSO) {
            activeDoodle = new Doodle();
            activeDoodle.firstFrame = currentFrame;
            
            if (mode == Mode.DRAW) {
                doodles.add(activeDoodle);
            }
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if (mode == Mode.SELECT || mode == Mode.LASSO) {
           
            lastX = evt.getX();
            lastY = evt.getY();
            
            if (currentActor == null)
            {
                finishSelect();
            }
            
            startX = startY = lastX = lastY = 0;
            
            repaint();
        }
        
        if (mode == Mode.ANIMATE) {
            recordTimer.stop();
            currentActor.record(false, currentFrame);
        }
    }//GEN-LAST:event_formMouseReleased
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
