package pandemicGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import animation.AbstractAnimation;
import animation.AnimatedObject;

/**
 * MasklessPeoeple would be able to create a MasklessPerson and make it randomly
 * enter the screen and shoot five germs to the player.
 * 
 * @author amywang and AJ
 *
 */
public class MasklessPeople extends OnscreenObject implements AnimatedObject {

    // The diameter of the person, in pixels
    private static int personSize = 25;

    // The diameter of an eye, in pixels
    private static int eyeSize = 5;

    // The starting left edge of the person
    private int x = 0;

    // The starting top the person, measured in pixels from the
    // top of the window.
    private int y = 100;

    // The number of pixels to move on each frame of the animation.
    private int moveAmountX = 5;

    private int moveAmountY = 5;

    // The animation that this object is part of.
    private AbstractAnimation animation;

    // The shape of the MasklessPerson
    private Ellipse2D person;

    private Ellipse2D eye1;

    private Ellipse2D eye2;

    private Rectangle mouth;

    // Initialize the timer to 0
    private int timer = 0;

    /**
     * Constructs a person with the instance variables set to for a large person
     * (these values will be updated as the person is hit with bullets)
     *
     * @param animation - the animation this object is part of
     */
    public MasklessPeople(AbstractAnimation animation) {
        this.animation = animation;

        person = new Ellipse2D.Double(x, y, personSize, personSize);

        eye1 = new Ellipse2D.Double(x + 5, y + 5, eyeSize, eyeSize);
        eye2 = new Ellipse2D.Double(x + 15, y + 5, eyeSize, eyeSize);
        mouth = new Rectangle(x + 5, y + 15, 15, 2);

    }

    /**
     * Returns the person that is the graphics shape
     * 
     * @return the person - the graphics shape being drawn
     */
    public Shape getShape() {
        return person;
    }

    /**
     * Display the MasklessPerson on screen
     * 
     * @param g - Graphics2D g 
     */
    public void paint(Graphics2D g) {
        g.setColor(Color.RED);
        g.fill(person);
        g.setColor(Color.WHITE);
        g.fill(eye1);
        g.fill(eye2);
        g.fill(mouth);
    }

    /**
     * Sets up the person to move in a random direction at the beginning of the
     * game.
     */
    void generateMovement() {
        Random rand = new Random();
        int moveAmount = 5;
        double degree = rand.nextDouble() * Math.PI * 2;
        moveAmountY = (int) (moveAmount * Math.cos(degree));
        timer = 0;
    }

    /**
     * Moves the ball a small amount. If it reaches the left or right edge, it
     * bounces.
     */
    public void nextFrame() {
        if (timer == 20) {
            generateMovement();
        } else {
            x += moveAmountX;
            y -= moveAmountY;
            person.setFrame(x, y, personSize, personSize);
            eye1.setFrame(x + 5, y + 5, eyeSize, eyeSize);
            eye2.setFrame(x + 15, y + 5, eyeSize, eyeSize);
            mouth.setFrame(x + 5, y + 15, 15, 2);
        }

        timer += 1;

        wrapAround();
    }

    /**
     * Makes MasklessPeople disappear form the screen
     */
    public void disappear() {
        x = animation.getWidth() + 200;
        y = animation.getHeight() + 200;
    }

    /**
     * Shoots six particles/germs at a time
     * 
     * @return particleList - a list of Particles that are shot
     */
    public ArrayList<Particles> shoot() {
        ArrayList<Particles> particleList = new ArrayList<Particles>();

        int count = 0;
        // MasklessPerson would shoot out six germs at a time
        while (count <= 5) {
            Random rand = new Random();
            double degree = rand.nextDouble() * Math.PI * 2;
            Particles particle = new Particles(animation, x, y, degree);
            particleList.add(particle);
            count += 1;
        }

        return particleList;
    }

    /**
     * Checks if the MasklessPerson is off screen
     * 
     * @return true - if MasklessPerson is off screen; false - if MasklessPerson
     *         is within the width
     */
    public boolean ifLeft() {
        return (x > animation.getWidth());
    }

    /**
     * Makes sure the MasklessPerson is on the screen the entire time during the
     * game
     */
    public void wrapAround() {
        int WINDOW_HEIGHT = animation.getHeight();

        int offset = 1;

        if (WINDOW_HEIGHT <= 0) {
            WINDOW_HEIGHT = PandemicGame.getWindowHeight();
        }

        if (y > WINDOW_HEIGHT && WINDOW_HEIGHT > 0) {
            y = y % WINDOW_HEIGHT + offset;
        } else if (y < 0) {
            y = WINDOW_HEIGHT + y + offset;
        }
    }

    /**
     * Sets x position. FOR TESTING PURPOSES ONLY.
     * 
     * @param newX - new x position
     */
    public void setX(int newX) {
        x = newX;
        person.setFrame(x, y, personSize, personSize);
    }

    /**
     * Sets y position. FOR TESTING PURPOSES ONLY.
     * 
     * @param newY - new y position
     */
    public void setY(int newY) {
        y = newY;
        person.setFrame(x, y, personSize, personSize);
    }

    /**
     * Gets x move amount. FOR TESTING PURPOSES ONLY.
     * 
     * @return moveAmountX - the value of moveAmountX
     */
    public int getMoveAmountX() {
        return moveAmountX;
    }

    /**
     * Gets y move amount. FOR TESTING PURPOSES ONLY.
     * 
     * @return moveAmountY - the value of moveAmountY
     */
    public int getMoveAmountY() {
        return moveAmountY;
    }

    /**
     * Gets the value of x. FOR TESTING PURPOSES ONLY.
     * 
     * @return x - the value of x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the value of y. FOR TESTING PURPOSES ONLY.
     * 
     * @return y - the value of y 
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the value of timer. FOR TESTING PURPOSES ONLY.
     * 
     * @return timer - the value of timer 
     */
    public int getTimer() {
        return timer;
    }

}
