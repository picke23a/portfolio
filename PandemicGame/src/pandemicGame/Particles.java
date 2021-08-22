package pandemicGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import animation.AbstractAnimation;
import animation.AnimatedObject;

/**
 * 
 * This class implements Particles class and defines methods to allow particles
 * to move in the direction of the doctor object and disappear when they get in
 * contact with viruses or maskless people. Doctor objects and maskless people
 * shoot particles.
 *
 */
public class Particles extends OnscreenObject implements AnimatedObject {

    // The diameter of the ball, in pixels
    private static final int BALL_SIZE = 5;

    // The current x-coordinate of the ball from the left/right side of the
    // window
    private int x;

    // The current y-coordinate of the ball from the top/bottom of the window
    private int y;

    // The number of pixels to move on each frame of the animation.
    private final int moveAmount = 10;

    // The animation that this object is part of.
    private AbstractAnimation animation;

    // The shape of a particle
    private Ellipse2D particle;

    // To keep track of whether the particle has intersected with a target
    private boolean moving = true;

    // The angle that the ship is facing
    private double degree;

    /**
     * This method draws the shape of the particles
     * 
     * @param animation an initialized abstract animation
     * @param x         the x-coordinate of the current location where the
     *                  particle should be drawn
     * @param y         the y-coordinate of the current location where the
     *                  particle should be drawn
     * @param degree    the current direction of the Doctor object
     */
    public Particles(AbstractAnimation animation, int x, int y, double degree) {
        this.animation = animation;
        particle = new Ellipse2D.Double(x, y, BALL_SIZE, BALL_SIZE);
        this.degree = degree;
        this.x = x;
        this.y = y;
        // System.out.println("x = " + x);
        // System.out.println("y = " + y);
    }

    /**
     * This method fills in the color of the particle
     * 
     * @param g the graphics to use/work on
     */
    public void paint(Graphics2D g) {
        // paints the particle yellow
        g.setColor(Color.YELLOW);
        g.fill(particle);
    }

    /**
     * This method sets the next frame of the particles so that they are in
     * constant motion on the screen
     */
    public void nextFrame() {
        // If the particle is in motion(not collided with a virus), then paint
        // the next frame
        if (moving) {
            move();
            particle.setFrame(x, y, BALL_SIZE, BALL_SIZE);

        }
    }

    /**
     * This method makes the particles disappear when they get in contact with
     * viruses
     */
    public void disappear() {
        // Particles disappear from screen when they hit a virus or maskless
        // person
        // if collision, set moving to false
        moving = false;
        // change the x and y coordinates to points off screen
        x = animation.getWidth() + 1;
        y = animation.getHeight() + 1;
    }

    /**
     * This method moves the particles in the direction that doctor currently
     * faces
     */
    public void move() {
        moving = true;
        // Move particles towards the target in the direction that the user
        // shoots
        x += moveAmount * Math.sin(degree);
        y -= moveAmount * Math.cos(degree);

    }

    /*
     * Returns the particle
     * 
     * @return particle the particle
     */
    public Shape getShape() {
        return particle;
    }

    /*
     * returns the current x coordinate of the particle
     * 
     * @return x the x coordinate of the point where the particle is
     */
    public int getX() {
        return x;
    }

    /**
     * sets x. FOR TESTING PURPOSES ONLY
     * 
     * @param newX the new value for the x coordinate of the point
     * 
     */
    public void setX(int newX) {
        x = newX;
    }

    /*
     * returns the current y position of the particle
     * 
     * @return y the current y position of the particle
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the moveAmount. FOR TESTING PURPOSES ONLY.
     * 
     * @return moveAmount the shift value
     */
    public int getMoveAmount() {
        return moveAmount;
    }

    /**
     * Gets the animation. FOR TESTING PURPOSES ONLY
     * 
     * @return animation the animation
     */
    public AbstractAnimation getAnimation() {
        return animation;
    }

    /**
     * Gets the animation. FOR TESTING PURPOSES ONLY
     * 
     * @return degree the angle of the doctor
     */
    public double getDegree() {
        return degree;
    }

}
