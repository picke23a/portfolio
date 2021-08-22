package pandemicGame;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VirusTest {
    PandemicGame game;

    Virus virus;

    @BeforeEach
    void setUp() throws Exception {
        game = new PandemicGame();

        virus = new Virus(game);
    }

    @Test
    void testGenerateMovement() {
        // Purpose: Tests that moveAmounts generated in
        // generateMovement() are within the correct ranges
        PandemicGame game = new PandemicGame();
        Virus virus = new Virus(game);
        virus.generateMovement();
        int moveX = virus.getMoveAmountX();
        int moveY = virus.getMoveAmountY();
        assertTrue(moveX != 0 && moveX < 5 && moveX > -5);
        assertTrue(moveY != 0 && moveY < 5 && moveY > -5);
    }

    @Test
    void testwrapAroundTop() {
        // Purpose: To test that the virus wraps around
        // correctly when it hits the top of the window
        game = new PandemicGame();
        virus = new Virus(game, 0, 50, 20, 1, 1, 100, -1, Color.GREEN);
        virus.wrapAround();
        assertEquals(550, virus.getY());
        assertEquals(200, virus.getX());
    }

    @Test
    void testwrapAroundTopOffsets() {
        // Purpose: To test that the virus wraps around
        // correctly when it hits the top of the window
        // and that the offset is working correctly
        game = new PandemicGame();
        virus = new Virus(game, 0, 50, 20, -1, -1, 20, -1, Color.GREEN);
        virus.wrapAround();
        assertEquals(550, virus.getY());
        assertEquals(770, virus.getX());
    }

    @Test
    void testwrapAroundBottom() {
        // Purpose: To test that the virus wraps around
        // correctly when it hits the buttom of the window
        game = new PandemicGame();
        // int size, int ballSize, int ptValue, int moveAmountX, int
        // moveAmountY, int x,
        // int y, Color c
        virus = new Virus(game, 0, 50, 20, 1, 1, 100, 650, Color.GREEN);
        virus.wrapAround();
        assertEquals(0, virus.getY());
        assertEquals(200, virus.getX());
    }

    @Test
    void testwrapAroundBottomOffsets() {
        // Purpose: To test that the virus wraps around
        // correctly when it hits the buttom of the window
        // and that the offset is working correctly
        game = new PandemicGame();
        virus = new Virus(game, 0, 50, 20, -1, 1, 20, 650, Color.GREEN);
        virus.wrapAround();
        assertEquals(0, virus.getY());
        assertEquals(770, virus.getX());
    }

    @Test
    void testwrapAroundLeft() {
        // Purpose: To test that the virus wraps around
        // correctly when it hits the left side of the window
        game = new PandemicGame();
        virus = new Virus(game, 0, 50, 20, 1, 1, -1, 100, Color.GREEN);
        virus.wrapAround();
        assertEquals(200, virus.getY());
        assertEquals(900 - 50, virus.getX());

    }

    @Test
    void testwrapAroundLeftOffsets() {
        // Purpose: To test that the virus wraps around
        // correctly when it hits the left side of the window
        // and that the offset is working correctly
        game = new PandemicGame();
        // int size, int ballSize, int ptValue, int moveAmountX, int
        // moveAmountY, int x,
        // int y, Color c
        virus = new Virus(game, 0, 50, 20, 1, -1, -1, 20, Color.GREEN);
        virus.wrapAround();
        assertEquals(470, virus.getY());
        assertEquals(900 - 50, virus.getX());

    }

    @Test
    void testwrapAroundRight() {
        // Purpose: To test that the virus wraps around
        // correctly when it hits the right side of the window
        game = new PandemicGame();
        virus = new Virus(game, 0, 50, 20, 1, 1, 901, 100, Color.GREEN);
        virus.wrapAround();
        assertEquals(200, virus.getY());
        assertEquals(0, virus.getX());
    }

    @Test
    void testwrapAroundRightOffsets() {
        // Purpose: To test that the virus wraps around
        // correctly when it hits the right side of the window
        // and that the offset is working correctly
        game = new PandemicGame();
        virus = new Virus(game, 0, 50, 20, 1, -1, 901, 20, Color.GREEN);
        virus.wrapAround();
        assertEquals(470, virus.getY());
        assertEquals(0, virus.getX());
    }

    @Test
    void testSplitBig() {
        // Purpose: To test that big viruses split correctly
        ArrayList<Virus> virusList = new ArrayList<>();
        virusList.add(virus);
        assertEquals(1, virusList.size());
        virusList.addAll(virusList.get(0).split());
        // The virus list should be three because the virus splits into
        // 2 and the previous virus is not deleted from the list without
        // further code that's used in Pandemic Game
        assertEquals(3, virusList.size());

    }

    @Test
    void testSplitMedium() {
        // Purpose: To test that medium viruses split correctly
        ArrayList<Virus> virusList = new ArrayList<>();
        virusList.add(new Virus(game, 1, 20, 20, 20, 20, 20, 20, Color.GREEN));
        virusList.addAll(virusList.get(0).split());
        assertEquals(3, virusList.size());

    }

    @Test
    void testSplitSmall() {
        // Purpose: To test that small viruses split correctly
        ArrayList<Virus> virusList = new ArrayList<>();
        virusList.add(new Virus(game, 2, 20, 20, 20, 20, 20, 20, Color.GREEN));
        virusList.addAll(virusList.get(0).split());
        assertEquals(1, virusList.size());

    }

}
