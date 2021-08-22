package pandemicGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import animation.AbstractAnimation;
import animation.AnimatedObject;

/**
 * 
 * This class creates the virus class with methods to allow it to move, split,
 * change point values and wrap around the screen properly
 *
 */
public class Virus extends OnscreenObject implements AnimatedObject {
    // The color of the largest virus when it is first created
    private Color c = Color.white;

    // The diameter of the ball, in pixels
    private int ballSize = 50;

    // The starting left edge of the ball
    private int x = 0;

    // The starting top edge of the ball
    private int y = 100;

    // The number of pixels to move on each frame of the animation.
    private int moveAmountX;

    private int moveAmountY;

    // The animation that this object is part of.
    private AbstractAnimation animation;

    // The ball shape type
    private Ellipse2D virus;

    // size of the virus in terms of big, medium and small
    // represented by 0, 1, 2
    int size;

    // point value of the virus, this is how many points
    // the players gets when the virus is hit
    int ptValue;

    // amount to offset the virus when it wraps around the screen
    int offset = 100;

    /**
     * Constructs a virus with the instance variables set for a large virus
     * (these values will be updated as the virus is hit with bullets)
     * 
     * @param animation the animation this object is part of
     */
    public Virus(AbstractAnimation animation) {
        this.animation = animation;
        virus = new Ellipse2D.Double(x, y, ballSize, ballSize);

        // zero to represent large asteroid
        size = 0;

        // represents point value of a large asteroid
        ptValue = 20;

        // sets the inital virus to a random point alongside the animation frame
        Random rand = new Random();
        y = rand.nextInt(400);

        generateMovement();

    }

    /**
     * Constructs a virus with the instance variables set for a specified size.
     * This constructor is used when an exiting virus on the screen is hit and
     * splits
     * 
     * @param animation the animation this object is part of
     * @param size the size (large, medium, small) of the virus
     * @param ballSize the size in pixels of the virus on the screen
     * @param ptValue the pt value the virus adds to the score when it is hit
     * @param moveAmountX the amount the virus moves horizontally each time
     * next frame is called
     * @param moveAmountY the amount the virus moves vertically each time
     * next frame is called
     * @param x the x position of the virus's left side
     * @param y the y position of the virus's top
     * @param c the color of the virus on the screen
     */
    public Virus(AbstractAnimation animation, int size, int ballSize,
            int ptValue, int moveAmountX, int moveAmountY, int x, int y,
            Color c) {
        this.animation = animation;

        this.c = c;

        virus = new Ellipse2D.Double(x, y, ballSize, ballSize);

        this.size = size;

        this.ptValue = ptValue;

        // checks that moveAmount will have a nonzero value
        if (moveAmountX == 0) {
            moveAmountX++;
        }
        this.moveAmountX = moveAmountX;

        // checks that moveAmount will have a nonzero value
        if (moveAmountY == 0) {
            moveAmountY++;
        }
        this.moveAmountY = moveAmountY;

        this.ballSize = ballSize;

        this.x = x;

        this.y = y;

    }

    /**
     * Draws a virus of the right color for its size.
     * 
     * @param g the graphics context to draw on.
     */
    public void paint(Graphics2D g) {

        g.setColor(c);

        g.fill(virus);
    }

    /**
     * Moves the virus a small amount in the direction is was already heading.
     * If the virus reaches the edge of the screen, the virus wraps around to
     * the other size.
     */
    public void nextFrame() {
        // Update the x and y value to move in the current direction
        x = x + moveAmountX;
        y = y + moveAmountY;

        wrapAround();

        virus.setFrame(x, y, ballSize, ballSize);
    }

    /**
     * Returns the virus that is the graphics shape
     * 
     * @return virus the virus being drawn
     */
    public Shape getShape() {
        return virus;
    }

    /**
     * Sets up the virus to move in a random direction at the beginning of the
     * game
     */
    protected void generateMovement() {
        Random rand = new Random();
        moveAmountX = rand.nextInt(4);
        moveAmountY = rand.nextInt(4);
        // checks to make sure moveAmount will
        // not be 0, adds 1 if it is
        if (moveAmountX == 0) {
            moveAmountX += 1;
        }
        if (moveAmountY == 0) {
            moveAmountY += 1;
        }

        // Randomly assigns a sign to each moveAmount variable
        if (rand.nextInt(10) > 5) {
            moveAmountX = -1 * moveAmountX;
        }
        if (rand.nextInt(10) > 5) {
            moveAmountY = -1 * moveAmountY;
        }

    }

    /**
     * Keeps the virus from disappearing off the screen. When the virus hits the
     * edge of the screen if appears on the opposite edge, somewhat offset from
     * where it went off the screen on the other end.
     */
    public void wrapAround() {
        int windowHeight = animation.getHeight();
        int windowWidth = animation.getWidth();

        // FOR TESTING PURPOSES:
        if (windowHeight == 0) {
            windowHeight = PandemicGame.getWindowHeight();
        }

        if (windowWidth == 0) {
            windowWidth = PandemicGame.getWindowWidth();
        }
        // Check if the right edge of the ball is beyond the right
        // edge of the window. If it is, move it to the opposite edge
        // and down 100 pixels.
        if (x + ballSize > windowWidth) {
            x = 0;
            // if the virus is moving in the positive direction,
            // the virus will reappear on the opposite side with
            // a higher y value
            if (moveAmountY > 0) {
                y += offset;
            }
            // if the virus is moving in the negative direction,
            // the virus will reappear on the opposite side with
            // a lower y value
            else {
                y -= offset;
            }

            // Makes sure the virus stays on screen
            if (y + ballSize > windowHeight) {
                y -= windowHeight;
            } else if (y < 0) {
                y += windowHeight - ballSize;
            }
        }

        // Check if the left edge of the ball is beyond the left
        // edge of the window. If it is, move it to the opposite edge
        // and move it down 100 pixels.
        else if (x < 0) {
            x = windowWidth - ballSize;
            // if the virus is moving in the positive direction,
            // the virus will reappear on the opposite side with
            // a higher y value
            if (moveAmountY > 0) {
                y += offset;
            }
            // if the virus is moving in the negative direction,
            // the virus will reappear on the opposite side with
            // a lower y value
            else {
                y -= offset;
            }

            // Makes sure the virus stays on screen
            if (y + ballSize > windowHeight) {
                y -= windowHeight;
            } else if (y < 0) {
                y += windowHeight - ballSize;
            }
        }
        // Check if the bottom edge of the ball if over the bottom
        // edge of the window.
        if (y + ballSize > windowHeight) {
            y = 0;
            if (moveAmountX > 0) {
                x += offset;
            } else {
                x -= offset;
            }

            // Makes sure the virus stays on screen
            if (x + ballSize > windowWidth) {
                x -= windowWidth;
            } else if (x < 0) {
                x += windowWidth - ballSize;
            }

        }

        // check if the top edge of the ball if over the
        // top of the window
        else if (y < 0) {
            y = windowHeight - ballSize;
            if (moveAmountX > 0) {
                x += offset;
            } else {
                x -= offset;
            }
            // Makes sure the virus stays on screen
            if (x + ballSize > windowWidth) {
                x -= windowWidth;
            } else if (x < 0) {
                x += windowWidth - ballSize;
            }
        }

    }

    /**
     * Makes the virus disappear when it is hit by the doctor
     */
    public void disappear() {
        moveAmountX = 0;
        moveAmountY = 0;

    }

    /**
     * The method creates two smaller viruses when the virus is hit by the
     * doctor. If the current virus is already at the smallest size, the virus
     * will disappear
     * 
     * @return ret an arrayList containing the new viruses that the old one splits into
     * returns an empty list if the virus is already at its smallest size 
     */
    public ArrayList<Virus> split() {
        ArrayList<Virus> ret = new ArrayList<Virus>();
        switch (this.size) {
        // case 0 is when the virus is at its largest size
        case 0:
            ret.add(new Virus(animation, 1, 40, 50, -moveAmountY, -moveAmountX,
                    x, y, Color.GREEN));
            ret.add(new Virus(animation, 1, 40, 50, moveAmountY, -moveAmountX,
                    x, y, Color.GREEN));
            this.disappear();
            return ret;
        // case 1 is when the virus is a medium size
        case 1:
            ret.add(new Virus(animation, 2, 25, 100, -moveAmountY, -moveAmountX,
                    x, y, Color.MAGENTA));
            ret.add(new Virus(animation, 2, 25, 100, moveAmountY, moveAmountX,
                    x, y, Color.MAGENTA));
            this.disappear();
            return ret;

        // case 2 is when the virus is smallest.
        // In this case, it should just disappear
        case 2:
            this.disappear();
            return ret;
        // default method should not be used.
        default:
            return ret;
        }
    }

    /**
     * The method returns the point value the virus will add to the overall
     * score when it is hit.
     * 
     * @return ptValue the pt value for hitting the asteroid
     * 
     */
    public int getPtValue() {
        return ptValue;
    }

    /**
     * Gets x move amount. FOR TESTING PURPOSES ONLY.
     * 
     * @return moveAmountX
     */
    public int getMoveAmountX() {
        return moveAmountX;
    }

    /**
     * Gets y move amount. FOR TESTING PURPOSES ONLY.
     * 
     * @return moveAmountY
     */
    public int getMoveAmountY() {
        return moveAmountY;
    }

    /**
     * Sets x move amount. FOR TESTING PURPOSES ONLY.
     * 
     * @param newAmount - an amount to update by
     */
    public void setMoveAmountX(int newAmount) {
        moveAmountX = newAmount;
    }

    /**
     * Gets x. FOR TESTING PURPOSES ONLY.
     * 
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y. FOR TESTING PURPOSES ONLY.
     * 
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Sets x. FOR TESTING PURPOSES ONLY.
     * 
     * @param newX - a new x value
     */
    public void setX(int newX) {
        x = newX;
    }

    /**
     * Sets y. FOR TESTING PURPOSES ONLY.
     * 
     * @param newY - a new y value
     */
    public void setY(int newY) {
        y = newY;
    }

}
