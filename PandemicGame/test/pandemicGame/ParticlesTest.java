package pandemicGame;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for methods in Particles.java class
 *
 */
class ParticlesTest {
    // A new particle object
    private Particles particle;

    // The shape of a particle
    private Ellipse2D particleShape;

    // the size of a particle
    private final int ballSize = 5;

    private final double degree = 30.00;

    private final int initialXAndY = 30;

    private PandemicGame game;

    @BeforeEach
    void setUp() throws Exception {
        game = new PandemicGame();
        particle = new Particles(game, initialXAndY, initialXAndY, degree);
        particleShape = new Ellipse2D.Double(initialXAndY, initialXAndY,
                ballSize, ballSize);
    }

    @Test
    /**
     * Method: disappear()
     * 
     * Parameters: None
     * 
     * Purpose: Check that the x and y coordinates change to points offscreen
     */
    void testDisappear() {
        // initialize the size of the ball to 5
        final int newBallSize = 5;

        // Call the disappear method
        particle.disappear();

        // Get the x and y coordinates after the disappear method is called
        int initialX = particle.getX();
        int initialY = particle.getY();

        assertEquals(newBallSize, ballSize);
        // check if the new x and y coordinates are successfully moved outside
        // the game window
        assertEquals(game.getWidth() + 1, initialX);
        assertEquals(game.getHeight() + 1, initialY);
    }

    @Test
    /**
     * Method: move()
     * 
     * Parameters: None
     * 
     * Purpose: To check if the x and y coordinates change as it moves from one
     * frame to another
     */
    void testMove() {
        // Get the original x and y values of the particle
        int initialX = particle.getX();
        int initialY = particle.getY();

        // call the move method
        particle.move();

        // Change the current point of the particle
        initialX += (particle.getMoveAmount() * Math.sin(degree));
        initialY -= (particle.getMoveAmount() * Math.cos(degree));

        // Get the new x and y coordinates
        int newX = particle.getX();
        int newY = particle.getY();

        // Check that the x and y coordinates changed as expected
        assertEquals(newX, initialX);
        assertEquals(newY, initialY);
    }

    @Test
    /*
     * Method: setX()
     * 
     * Parameters: newX
     * 
     * Purpose: Check that the x-coordinate is changed to the parameter
     * successfully
     */
    void testSetX() {
        // initialize the new x variable
        final int newX = 40;

        // change the x coordinate to the new variable
        particle.setX(newX);

        // Get the new x value
        int updatedX = particle.getX();

        // check that the x value is changed to the new value
        assertEquals(newX, updatedX);
    }

    @Test
    /*
     * Method: getShape()
     * 
     * Parameters: None
     * 
     * Purpose: Check to make sure that the shape of the particle is returned
     */
    void testGetShape() {
        Shape expectedParticle = particle.getShape();
        assertEquals(particleShape, expectedParticle);
    }

    @Test
    /*
     * Method: nextFrame()
     * 
     * Parameters: None
     * 
     * Purpose: Check that the x and y coordinates change in each frame and move
     * in the direction that the doctor object is facing
     */
    void testNextFrame() {
        // Get the original x and y values of the particle
        int initialX = particle.getX();
        int initialY = particle.getY();

        // call the next frame method
        particle.nextFrame();

        // Change the current point of the particle
        initialX += (particle.getMoveAmount() * Math.sin(degree));
        initialY -= (particle.getMoveAmount() * Math.cos(degree));

        // Get the new x and y coordinates
        int newX = particle.getX();
        int newY = particle.getY();

        // Check that the x and y coordinates changed as expected
        assertEquals(newX, initialX);
        assertEquals(newY, initialY);
    }
}
