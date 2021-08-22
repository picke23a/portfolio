package pandemicGame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author amywang
 *
 */
class DoctorTest {

    PandemicGame game;

    Doctor doctor;

    @BeforeEach
    void setUp() throws Exception {
        game = new PandemicGame();
        doctor = new Doctor(game);
    }

    @Test
    void hyperspaceTest() {
        // PURPOSE: tests if hyperspace() would put the doctor in a new location

        int oldX = doctor.getX();
        int oldY = doctor.getY();
        doctor.hyperspace();
        int newX = doctor.getX();
        int newY = doctor.getY();
        assertFalse(oldX == newX);
        assertFalse(oldY == newY);
    }

    @Test
    void rotateClockwiseTest() {
        // PURPOSE: tests if rotateClockwise() would change rotateClockwise to
        // true
        doctor.rotateClockwise();
        assertTrue(doctor.getRotateClockwise());
    }

    @Test
    void rotateAntiClockwiseTest() {
        // PURPOSE: tests if rotateAntiClockwise() would change
        // rotateAntiClockwise to true
        doctor.rotateAntiClockwise();
        assertTrue(doctor.getRotateAntiClockwise());
    }

    @Test
    void getShootDirectionTest0Iteration() {
        // PURPOSE: tests if getShootDirection() would return the correct degree
        // value when there is no iteration
        doctor.setDegree(Math.PI / 2);
        doctor.getShootDirection();
        // Expected value should be Math.PI/2
        assertEquals(Math.PI / 2, doctor.getDegree());
    }

    @Test
    void getShootDirectionTest1Iteration() {
        // PURPOSE: tests if getShootDirection() would return the correct degree
        // value when there is one iteration
        doctor.setDegree(Math.PI);
        doctor.getShootDirection();
        // Expected value should be 0
        assertEquals(0, doctor.getDegree());
    }

    @Test
    void getShootDirectionTest3Iterations() {
        // PURPOSE: tests if getShootDirection() would return the correct degree
        // value when there is three iterations
        doctor.setDegree(Math.PI * 3);
        doctor.getShootDirection();
        // Expected value should be 0
        assertEquals(0, doctor.getDegree());
    }

    @Test
    void disappearTest() {
        // PURPOSE: tests if disappear() would move the doctor off screen
        doctor.disappear();
        int newX = doctor.getX();
        int newY = doctor.getY();
        System.out.println(newX);
        // The expected value for newX and newY should be larger the animation's
        // width and height
        assertTrue(newX > game.getWidth());
        assertTrue(newY > game.getHeight());
    }

    @Test
    void wrapAroundTest() {
        // PURPOSE: tests wrapAround() would modify x and y coordinates in the
        // expected way

        // Sets x larger than the animation window width
        doctor.setX(PandemicGame.getWindowWidth() + 1);
        // Sets y larger than the animation window height
        doctor.setY(PandemicGame.getWindowHeight() + 2);

        doctor.wrapAround();

        // Expected value for x should be 2
        assertEquals(2, doctor.getX());
        // Expected value for y should be 2
        assertEquals(3, doctor.getY());

        doctor.setX(-1);
        doctor.setY(-1);

        doctor.wrapAround();

        // Expected value for x should be 900
        assertEquals(900, doctor.getX());
        // Expected value for x should be 600
        assertEquals(600, doctor.getY());
    }

    @Test
    void accelerateTest() {
        // PURPOSE: tests if accelerate() would change the x and y coordinates
        // in the expected way
        int moveAmount = Doctor.getMoveAmount();
        double degree = Math.PI / 3;
        doctor.setDegree(degree);
        int oldX = doctor.getX();
        int oldY = doctor.getY();

        doctor.accelerate();

        int newX = doctor.getX();
        int newY = doctor.getY();

        // Expected value for x and y
        int expectedX = oldX + (int) (moveAmount * Math.sin(degree));
        int expectedY = oldY - (int) (moveAmount * Math.cos(degree));

        assertEquals(expectedX, newX);
        assertEquals(expectedY, newY);
    }

    @Test
    void shootTest() {
        // PURPOSE: tests if shoot() would return the Particles in the expected
        // way

        Particles drop = doctor.shoot();

        Particles expected = new Particles(game, doctor.getX(), doctor.getY(),
                doctor.getDegree());

        assertEquals(drop.getAnimation(), expected.getAnimation());
        assertEquals(drop.getX(), expected.getX());
        assertEquals(drop.getY(), expected.getY());
        assertEquals(drop.getDegree(), expected.getDegree());
    }

    @Test
    void getShapeTest() {
        // PURPOSE: tests if getShape() would reset rotateClockwise and
        // rotateAntiClockwise to false and the degree is as expected

        double degree = Math.PI / 18;
        doctor.setDegree(degree);

        doctor.rotateClockwise();
        doctor.getShape();
        assertFalse(doctor.getRotateClockwise());
        assertEquals(Math.PI / 9, doctor.getDegree());

        doctor.rotateAntiClockwise();
        assertTrue(doctor.getRotateAntiClockwise());
        doctor.getShape();
        assertFalse(doctor.getRotateAntiClockwise());
        assertEquals(Math.PI / 18, doctor.getDegree());

        doctor.getShape();
        assertFalse(doctor.getRotateClockwise());
        assertFalse(doctor.getRotateAntiClockwise());
        assertEquals(Math.PI / 18, doctor.getDegree());
    }

    @Test
    void nextFrameTest() {
        // PURPOSE: tests if nextFrame would keep the doctor object within the
        // animation window
        
        // Sets x larger than the animation window width
        doctor.setX(PandemicGame.getWindowWidth() + 1);
        // Sets y larger than the animation window height
        doctor.setY(PandemicGame.getWindowHeight() + 2);

        doctor.nextFrame();

        assertTrue(doctor.getX() < PandemicGame.getWindowWidth());
        assertTrue(doctor.getY() < PandemicGame.getWindowHeight());
    }

}
