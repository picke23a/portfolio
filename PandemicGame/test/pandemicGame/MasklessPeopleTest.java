package pandemicGame;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author AmyWang and GloriaKodua
 *
 */
class MasklessPeopleTest {
    
    private PandemicGame game;
    
    private MasklessPeople person;
    
    
    @BeforeEach
    void setUp() throws Exception {
        game = new PandemicGame();
        person = new MasklessPeople(game);
    }

    @Test
    void testDisappear() {
        // PURPOSE: tests if disappear() would move the object off screen 
        
        person.disappear();
        int x = person.getX();
        int y = person.getY();
        assertEquals(game.getWidth() + 200, x);
        assertEquals(game.getHeight() + 200, y);
    }

    @Test
    void testWrapAround() {
        // PURPOSE: tests if wrapAround() would work in an expected way 
     
        person.setY(PandemicGame.getWindowHeight() + 1);
        int y = person.getY();
       
        assertEquals(601,y);
        
        person.wrapAround();
        
        assertEquals(2,person.getY());
        
        person.setY(-1);
        
        person.wrapAround();
        
        assertEquals(600, person.getY());
        
    }
    

    @Test
    void shootTest() {
        // PURPOSE: tests if shoot() would generate a certain number of particles
        
        ArrayList<Particles> drops = person.shoot();

        for (int j = 0; j < drops.size(); j++) {
            Particles expected = new Particles(game, person.getX(), person.getY(), drops.get(j).getDegree());
            assertEquals(drops.get(j).getAnimation(), expected.getAnimation());
            assertEquals(drops.get(j).getX(), expected.getX());
            assertEquals(drops.get(j).getY(), expected.getY());
            assertEquals(drops.get(j).getDegree(), expected.getDegree());
        }
    }

    @Test
    void generateMovementTest() {
        // PURPOSE: tests if generateMovement() would generate a new moveAmountY.
        
        int oldY = person.getMoveAmountY();
        
        person.nextFrame();
        person.nextFrame();

        person.generateMovement();
        
        int newY = person.getMoveAmountY();
        int timer = person.getTimer();
        
        assertFalse(oldY == newY);
        assertTrue(timer == 0);
    }
    
    @Test 
    void testNextframe() {
        // PURPOSE: tests if nextFrame() would call generateMovement() when the timer = 20 
        int i = 0;
        while (i <= 20) {
            person.nextFrame();
            i++;
        }

        int timer = person.getTimer();
        assertEquals(1, timer);

    }
    

}
