package pandemicGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

public abstract class OnscreenObject {
    public abstract void paint(Graphics2D g);
    protected abstract void nextFrame();
    public abstract Shape getShape();
}