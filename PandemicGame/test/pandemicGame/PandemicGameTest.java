package pandemicGame;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Polygon;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import pandemicGame.PandemicGame.STATE;

class PandemicGameTest {
    PandemicGame game;

    ArrayList<MasklessPeople> masklessPeopleList;

    MasklessPeople person;

    Particles vaccineParticle;

    Particles germ;

    Virus virus;

    @BeforeEach
    void setUp() throws Exception {
        game = new PandemicGame();

        person = new MasklessPeople(game);

        vaccineParticle = new Particles(game, 50, 50, 12);

        germ = new Particles(game, 50, 50, 13);

        virus = new Virus(game);
    }

    @Test
    void testPandemicGame() {
        // PURPOSE: virusList should be initialized with whatever virus number
        // we pass in
        game.emptyVirusList();
        
        game = new PandemicGame();
        
        assertEquals(8, game.getVirusListSize());
    }

    @Test
    void testDrawPolygon() {
        // PURPOSE: makes sure polygon is properly drawn
        Polygon polygon = game.drawPolygon(10);

        int[] ypoints = { 90, 50, 90, 0 };
        int[] xpoints = { 10, 15, 20, 0 };

        assertTrue(Arrays.equals(ypoints, polygon.ypoints));
        assertTrue(Arrays.equals(xpoints, polygon.xpoints));

    }

    @Test
    void testNextFrameNeverRuns() {
        // PURPOSE: makes sure nextframe never runs when game is not in gameplay
        // state
        // (e.g., the time should never be updated)
        game.setState(STATE.MENU);
        game.nextFrame();
        assertEquals(0, game.getTime());

    }

    @Test
    void testNextFrameOneMasklessPerson() {
        // PURPOSE: makes sure one maskless person is created at time = 200
        game.setState(STATE.GAME);

        game.setTime(200);

        game.emptyVirusList();
        game.getDoctor().disappear();
        game.nextFrame();

        assertEquals(1, game.getMasklessPeopleListSize());

    }

    @Test
    void testVirusCollisionCheckerWithDoctor() {
        // PURPOSE: makes sure that there is one virus to delete after the virus
        // and the doctor have collided
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the doctor is so that there will be a
        // collision
        Virus virus2 = new Virus(game, 0, 50, 20, 0, 0, 50, 50, Color.WHITE);

        game.getDoctor().setX(50);
        game.getDoctor().setY(50);

        // virus and doctor have collided
        assertTrue(game.checkCollision(virus2, game.getDoctor()));

        // there will only be this one virus in list
        game.emptyVirusList();
        game.emptyParticleList();

        game.getVirusList().add(virus2);

        // should be one virus in list before nextFrame
        assertEquals(1, game.getVirusListSize());

        assertTrue(game.checkCollision(game.getVirusList().get(0),
                game.getDoctor()));

        game.virusCollisionChecker();

        assertEquals(1, game.getVirusesToDeleteList().size());
    }

    @Test
    void testVirusCollisionCheckerWithParticle() {
        // PURPOSE: makes sure there is one virus and one particle to delete
        // after they have both collided
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the particle is so that there will be a
        // collision
        Virus virus2 = new Virus(game, 0, 50, 20, 0, 0, 50, 50, Color.WHITE);

        // virus and particle have collided
        assertTrue(game.checkCollision(virus2, vaccineParticle));

        // there will only be that one virus in list
        game.emptyVirusList();
        game.getVirusList().add(virus2);
        game.getParticleList().add(vaccineParticle);

        // should be one virus in list before nextFrame
        assertEquals(1, game.getVirusListSize());

        game.virusCollisionChecker();

        // there should be one of each to delete
        assertEquals(1, game.getVirusesToDeleteList().size());
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testVirusCollisionCheckerWithParticlePer10000() {
        // PURPOSE: checks that player gets a new life every 10000 points
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the particle is so that there will be a
        // collision
        Virus virus2 = new Virus(game, 0, 50, 20, 0, 0, 50, 50, Color.WHITE);

        // virus and particle have collided
        assertTrue(game.checkCollision(virus2, vaccineParticle));

        // there will only be that one virus in list
        game.emptyVirusList();
        game.getVirusList().add(virus2);
        game.getParticleList().add(vaccineParticle);

        game.setPer10000Score(10001);

        game.setLives(5);

        assertEquals(5, game.getLives());

        game.virusCollisionChecker();

        // there were 5 lives initially, now there should be 6
        assertEquals(6, game.getLives());
    }

    @Test
    void testVirusCollisionCheckerWithParticleHighestScore() {
        // PURPOSE: checks that game ends when score is greater than 99991
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the particle is so that there will be a
        // collision
        Virus virus2 = new Virus(game, 0, 50, 20, 0, 0, 50, 50, Color.WHITE);

        // virus and particle have collided
        assertTrue(game.checkCollision(virus2, vaccineParticle));

        // there will only be that one virus in list
        game.emptyVirusList();
        game.getVirusList().add(virus2);
        game.getParticleList().add(vaccineParticle);

        // should be one virus in list before nextFrame
        assertEquals(1, game.getVirusListSize());

        game.setScore(99991);

        game.virusCollisionChecker();

        // game state should switch
        assertEquals(STATE.GAMEOVER, game.getState());
    }

    @Test
    void testVirusCollisionCheckerParticleGetsRemoved1() {
        // PURPOSE: particle should get removed from list when it x >
        // windowwidth
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the particle is so that there will be a
        // collision
        Virus virus2 = new Virus(game, 0, 50, 20, 0, 0, 50, 50, Color.WHITE);

        // creates offscreen particle
        Particles newParticle = new Particles(game, 7000, 50, 13);

        // there will only be that one virus in list, and the new particle in
        // the particleList
        game.emptyVirusList();
        game.getVirusList().add(virus2);
        game.getParticleList().add(newParticle);

        game.virusCollisionChecker();

        // particle is offscreen, so there should now be one particle to delete
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testVirusCollisionCheckerParticleGetsRemoved2() {
        // PURPOSE: particle should get removed from list when x < 0
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the particle is so that there will be a
        // collision
        Virus virus2 = new Virus(game, 0, 50, 20, 0, 0, 50, 50, Color.WHITE);

        // creates offscreen particle
        Particles newParticle = new Particles(game, -50, 50, 13);

        // there will only be that one virus in list, and the new particle in
        // the particleList
        game.emptyVirusList();
        game.getVirusList().add(virus2);
        game.getParticleList().add(newParticle);

        game.virusCollisionChecker();

        // particle is offscreen, so there should now be one particle to delete
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testVirusCollisionCheckerParticleGetsRemoved3() {
        // PURPOSE: particle should get removed from list when y > windowheight
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the particle is so that there will be a
        // collision
        Virus virus2 = new Virus(game, 0, 50, 20, 0, 0, 50, 50, Color.WHITE);

        // creates offscreen particle
        Particles newParticle = new Particles(game, 50, 5000, 13);

        // there will only be that one virus in list, and the new particle in
        // the particleList
        game.emptyVirusList();
        game.getVirusList().add(virus2);
        game.getParticleList().add(newParticle);

        game.virusCollisionChecker();

        // particle is offscreen, so there should now be one particle to delete
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testVirusCollisionCheckerParticleGetsRemoved4() {
        // PURPOSE: particle should get removed from list when y < 0
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the particle is so that there will be a
        // collision
        Virus virus2 = new Virus(game, 0, 50, 20, 0, 0, 50, 50, Color.WHITE);

        // creates offscreen particle
        Particles newParticle = new Particles(game, 50, -90, 13);

        // there will only be that one virus in list, and the new particle in
        // the particleList
        game.emptyVirusList();
        game.getVirusList().add(virus2);
        game.getParticleList().add(newParticle);

        game.virusCollisionChecker();

        // particle is offscreen, so there should now be one particle to delete
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testMasklessPeopleCollisionCheckerWithDoctor() {
        // PURPOSE: makes sure that a masklessperson properly collides with the
        // doctor
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new virus in where the doctor is so that there will be a
        // collision
        person.setX(50);
        person.setY(50);

        game.getMasklessPeopleList().add(person);

        game.getDoctor().setX(50);
        game.getDoctor().setY(50);

        // person and doctor have collided
        assertTrue(game.checkCollision(person, game.getDoctor()));

        // there will be no particles in the particle list
        game.emptyParticleList();

        game.masklessPeopleCollisionChecker();

        // there should be one person to delete in the respective list
        assertEquals(1, game.getPeopleToDeleteListSize());
    }

    @Test
    void testMasklessPeopleCollisionCheckerWithParticle() {
        // PURPOSE: makes sure that a masklessperson properly collides with a
        // particle
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new person in where the particle is so that there will be a
        // collision
        person.setX(50);
        person.setY(50);

        game.getMasklessPeopleList().add(person);

        // person and particle have collided
        assertTrue(game.checkCollision(person, vaccineParticle));

        // empties any particles from earlier and adds the new one
        game.emptyParticleList();
        game.getParticleList().add(vaccineParticle);

        game.masklessPeopleCollisionChecker();

        // there should be one person to delete in the respective lists
        assertEquals(1, game.getPeopleToDeleteListSize());
        assertEquals(1, game.getPeopleToDeleteListSize());
    }

    @Test
    void testMasklessPeopleCollisionCheckerWithParticlePer10000() {
        // PURPOSE: checks that player gets a new life every 10000 points
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new person in where the particle is so that there will be a
        // collision
        person.setX(50);
        person.setY(50);

        game.getMasklessPeopleList().add(person);

        // empties any particles from earlier and adds the new one
        game.emptyParticleList();
        game.getParticleList().add(vaccineParticle);

        game.setPer10000Score(10001);

        game.setLives(5);

        assertEquals(5, game.getLives());

        game.masklessPeopleCollisionChecker();

        // there were 5 lives initially, now there should be 6
        assertEquals(6, game.getLives());
    }

    @Test
    void testMasklessPeopleCollisionCheckerWithParticleHighestScore() {
        // PURPOSE: checks that game ends when score is greater than 99991
        game.setState(STATE.GAME);

        game.setTime(0);

        // puts a new person in where the particle is so that there will be a
        // collision
        person.setX(50);
        person.setY(50);

        game.getMasklessPeopleList().add(person);

        // person and particle have collided
        assertTrue(game.checkCollision(person, vaccineParticle));

        // empties any particles from earlier and adds the new one
        game.emptyParticleList();
        game.getParticleList().add(vaccineParticle);

        game.setScore(99991);

        game.masklessPeopleCollisionChecker();

        // game state should switch
        assertEquals(STATE.GAMEOVER, game.getState());
    }

    @Test
    void testMasklessPeopleCollisionCheckerParticleGetsRemoved1() {
        // PURPOSE: particle should get removed from list when it x >
        // windowwidth
        game.setState(STATE.GAME);

        game.setTime(0);

        game.getMasklessPeopleList().add(person);

        // creates offscreen particle
        Particles newParticle = new Particles(game, 5000, 50, 13);

        // puts new particle in list
        game.getParticleList().add(newParticle);

        game.masklessPeopleCollisionChecker();

        // particle is offscreen, so there should now be one particle to delete
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testMasklessPeopleCollisionCheckerParticleGetsRemoved2() {
        // PURPOSE: particle should get removed from list when it x < 0
        game.setState(STATE.GAME);

        game.setTime(0);

        game.getMasklessPeopleList().add(person);

        // creates offscreen particle
        Particles newParticle = new Particles(game, -5000, 50, 13);

        // puts new particle in list
        game.getParticleList().add(newParticle);

        game.masklessPeopleCollisionChecker();

        // particle is offscreen, so there should now be one particle to delete
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testMasklessPeopleCollisionCheckerParticleGetsRemoved3() {
        // PURPOSE: particle should get removed from list when it y >
        // windowheight
        game.setState(STATE.GAME);

        game.setTime(0);

        game.getMasklessPeopleList().add(person);

        // creates offscreen particle
        Particles newParticle = new Particles(game, 50, 5000, 13);

        // puts new particle in list
        game.getParticleList().add(newParticle);

        game.masklessPeopleCollisionChecker();

        // particle is offscreen, so there should now be one particle to delete
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testMasklessPeopleCollisionCheckerParticleGetsRemoved4() {
        // PURPOSE: particle should get removed from list when it y < 0
        game.setState(STATE.GAME);

        game.setTime(0);

        game.getMasklessPeopleList().add(person);

        // creates offscreen particle
        Particles newParticle = new Particles(game, 50, -5000, 13);

        // puts new particle in list
        game.getParticleList().add(newParticle);

        game.masklessPeopleCollisionChecker();

        // particle is offscreen, so there should now be one particle to delete
        assertEquals(1, game.getParticlesToDeleteList().size());
    }

    @Test
    void testGermCollisionChecker() {
        // PURPOSE: checks to make sure that there is one germ to delete if the
        // germ has collided with the doctor
        game.setState(STATE.GAME);

        game.getGermList().add(germ);

        game.getDoctor().setX(50);
        game.getDoctor().setY(50);

        // germ and doctor have collided
        assertTrue(game.checkCollision(germ, game.getDoctor()));

        game.germCollisionChecker();

        // should be one germ to delete
        assertEquals(1, game.getGermListSize());

    }

    @Test
    void testIndividualNextFrames() {
        // PURPOSE: tests that this method actually updates nextFrame for each
        // object

        // sets up test for doctor
        int originalX = game.getDoctor().getX();
        // doctor should initially be at x = 450; then gets set to x = 460
        assertEquals(450, originalX);
        game.getDoctor().setX(460);

        // sets up test for virus
        game.getVirusList().add(virus);
        // virus should initially be at x = 0; sets move amount for x to 5
        assertEquals(0, virus.getX());
        virus.setMoveAmountX(5);

        // sets up test for particle
        game.getParticleList().add(vaccineParticle);
        vaccineParticle.setX(300);

        // sets up test for germ
        game.getGermList().add(germ);
        germ.setX(300);

        game.individualNextFrames();

        // doctor's original x value plus the move amount should equal its new x
        // value
        assertEquals(game.getDoctor().getMoveAmount() + originalX,
                game.getDoctor().getX());
        // makes sure virus is where it is supposed to be
        assertEquals(virus.getMoveAmountX(), 5);
        // makes sure particle is where it is supposed to be
        assertEquals(
                (int) (300 + vaccineParticle.getMoveAmount() * Math.sin(12)),
                vaccineParticle.getX());
        // makes sure germ is where it's supposed to be
        assertEquals((int) (300 + germ.getMoveAmount() * Math.sin(13)),
                germ.getX());

    }

    @Test
    void testMasklessPeopleCreate() {
        // PURPOSE: checks to make sure the game will only have one
        // masklessperson saved
        // after one has been created

        game.masklessPeopleCreate();

        assertEquals(1, game.getMasklessPeopleListSize());
    }

    @Test
    void testMasklessPersonNextFrameShoot() {
        // PURPSOE: tests that shooting happens at the right time and actually
        // spawns germs

        masklessPeopleList = game.getMasklessPeopleList();

        masklessPeopleList.add(person);

        game.setShootTime(360);

        game.masklessPeopleNextFrame(masklessPeopleList);

        assertTrue(game.getGermListSize() > 0);
    }

    @Test
    void testMasklessPersonNextFrameOffscreen() {
        // PURPOSE: tests that maskless people off screen get added to the list
        // of people to delete
        masklessPeopleList = game.getMasklessPeopleList();

        masklessPeopleList.add(person);

        // person should be offscreen now
        person.setX(2000);
        person.setY(2000);

        game.masklessPeopleNextFrame(masklessPeopleList);

        assertTrue(game.getPeopleToDeleteListSize() == 1);
    }

    @Test
    void testDoctorRespawn5Lives() {
        // PURPOSE: makes sure state is still 'game' if the doctor has > 1 life

        game.setState(STATE.GAME);

        game.doctorRespawn();

        assertEquals(STATE.GAME, game.getState());
    }

    @Test
    void testDoctorRespawn1Life() {
        // PURPOSE: makes sure state is set to over if doctor has 1 life
        game.setLives(1);

        game.doctorRespawn();

        assertEquals(STATE.GAMEOVER, game.getState());
    }

    @Test
    void testlevelSwitcher() {
        // PURPOSE: level should not switch if there are > 0 viruses
        game.setLevel(0);
        
        game.levelSwitcher();

        assertEquals(0, game.getLevel());
    }

    @Test
    void testLevelSwitcherMoreViruses() {
        // PURPOSE: if there are 0 viruses, level should update
        game.setLevel(1);
        
        game.emptyVirusList();

        game.levelSwitcher();

        assertEquals(2, game.getLevel());
    }

    @Test
    void testVirusAdder() {
        // PURPOSE: makes sure virusAdder() works correctly (there should be 7)

        game = new PandemicGame();

        // empties game of whatever came before
        game.emptyVirusList();

        assertEquals(7, game.virusAdder(7));
    }

    @Test
    void testLoseLife() {
        // PURPOSE: makes sure that loseLife correctly subtracts one life
        game = new PandemicGame();

        game.setLives(5);

        game.loseLife();

        assertEquals(4, game.getLives());
    }
    
    
    @Test
    void testTopTenNoScores() throws Exception {
        //PURPOSE: when topTen is empty, the score should get added to the list
        //no matter what, topTen will always be true
        setUp();
        game.setFile("TopTenCaseOne.txt");
        assertTrue(game.topTen());
    }
    
    @Test
    void testTopTenSomeScores() throws Exception {
        //Purpose: Tests the case for top ten where there are some high scores
        // in the file
        setUp();
        game.setFile("TopTenCaseTwo.txt");
        assertTrue(game.topTen());
    }
    
    @Test
    void testTopTeScoresHighThanLast() throws Exception {
        setUp();
        game.setScore(200);
        game.setFile("TopTenCaseThree.txt");
        assertTrue(game.topTen());
    }
    
    @Test
    void testTopTeScoresLowerThanLast() throws Exception {
        setUp();
        game.setScore(10);
        game.setFile("TopTenCaseThree.txt");
        assertEquals(false, game.topTen());
    }
    
    @Test
    void testAddTopTenWellFormed() throws Exception {
        //Purpose: To test that the scores in the highscore
        //file are added in order and that there are never more
        //than ten high scores in the file
        setUp();
        game.setFile("AddTopTenTest.txt");
        //clears the file
        FileWriter fileWriter = new FileWriter("AddTopTenTest.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("");
        printWriter.close();
        
        //adds eleven scores to the file, only ten should be saved in the file
        
        //when the scores are added largest to smallest, it tests the if statement
        //that adds scores to the list if the score is less than ten
        for( int i = 6; i >= 0; i--) {
            game.setScore(10 * i);
            game.addTopTen("AJ");
        }
        //when the scores are added from smallest to largest, it tests the if
        //statement that adds scores that are larger than other scores in the file
        for(int i = 0; i <6; i++) {
            game.setScore(60+ 10 * i);
            game.addTopTen("Anna");
        }
        
        boolean test = true;
        
        //set up to test that the high scores were added to the file in order
        File f = new File("AddTopTenTest.txt");
        Scanner highScore = new Scanner(f);
        List<Integer> scores = new ArrayList<Integer>();
        while (highScore.hasNextInt()) {
                 scores.add(highScore.nextInt());
                 highScore.next();
        }
        highScore.close();
        
        // test
        for (int j = 0;j <scores.size() -1;j++) {
            if (scores.get(j) < scores.get(j + 1)) {
                test = false;
                break;
            }
        }
        
        assertTrue(test);
        
        
    }
    
}
    

   