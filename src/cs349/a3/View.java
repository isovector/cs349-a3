/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs349.a3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author sandy
 */
public class View {
    Color white = new Color(0xFFFFFF);
    Color black = new Color(0);
    BasicStroke selectStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 8.0f, 3.0f, 2.0f, 3.0f }, 0.0f);
    AffineTransform identityTransform = new AffineTransform();
    
    Animation animation;
    
    public View(Animation m) {
        animation = m;
    }
    
    void paint(Graphics g, int width, int height) {
        g.setColor(white);
        g.fillRect(0, 0, width, height);
        
        for (Doodle doodle : animation.doodles) {
            doodle.paint(g, animation.currentFrame);
        }
        
        for (Actor actor : animation.actors) {
            actor.paint(g, animation.currentFrame);
            ((Graphics2D)g).setTransform(identityTransform);
        }
        
        if (animation.currentActor != null) {
            animation.currentActor.paintRect(g, animation.currentFrame);
        }
        
        if (animation.mode == Animation.Mode.SELECT || animation.mode == Animation.Mode.LASSO) {
            g.setColor(black);
            ((Graphics2D)g).setStroke(selectStroke);
            
            if (animation.mode == Animation.Mode.SELECT) {
                //int x0 = Math.min(startX, lastX), x1 = Math.max(startX, lastX);
                //int y0 = Math.min(startY, lastY), y1 = Math.max(startY, lastY);

                //g.drawRect(x0, y0, x1 - x0, y1 - y0);
            } else {
                animation.activeDoodle.paint(g, animation.currentFrame);
                if (animation.activeDoodle.lines.size() > 2) {
                    new Line(0, animation.activeDoodle.lines.peekFirst().source, animation.activeDoodle.lines.peekLast().dest).paint(g);
                }
            }
        }
    }
}
