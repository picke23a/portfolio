package pandemicGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import animation.AbstractAnimation;

/**
 * 
 * Creates a PandemicGame. This game contains all of the game logic. It creates
 * objects on the screen, sets up various game screens, and allows gameplay.
 * 
 * @author Anisha Johnson with Amy Wang, Anna Pickett, and Gloria Kodua
 *
 */
public class PandemicGame extends AbstractAnimation implements KeyListener {
    // an instance of the game
    static PandemicGame game;

    // The width of the window, in pixels.
    private static int WINDOW_WIDTH = 900;

    // The height of the window, in pixels.
    private static int WINDOW_HEIGHT = 600;

    // the player's score
    private static int score = 0;

    // will be used to give the player an extra life every 10,000 points
    private static int per10000score = 0;

    // how many lives the player has
    private static int numLives = 4;

    // how many large viruses show up at the beginning of the screen
    private static int numViruses = 7;

    // represents the level that the user is currently at
    private static int level = 1;

    // a timer that will help determine when to create
    // masklessPeople
    private static int time = 0;

    // timer that will determine when masklessPeople shoot
    private static int shootTime = 0;

    // The players initials that may be added at the end of the game
    private static String inits = "_";

    /**
     * Keeps track of the state of the game
     *
     */
    public static enum STATE {
        MENU, GAME, ABOUT, LEVEL, GAMEOVER, ENTER_INIT, EXIT_SCREEN
    }

    public static STATE State = STATE.MENU;

    private static MouseInput mouseInput = new MouseInput();

    // file where high scores are kept
    private String highScoreFile = "HighScore.txt";

    // doctor variable, since there is only one doctor at a time
    private Doctor doctor = new Doctor(this);

    // stores all viruses
    private ArrayList<Virus> virusList = new ArrayList<>();

    // stores all vaccine particels
    private ArrayList<Particles> particleList = new ArrayList<>();

    // stores all masklessPeople
    private ArrayList<MasklessPeople> masklessPeopleList = new ArrayList<>();

    // stores all germs shot out by masklessPeople
    private ArrayList<Particles> germList = new ArrayList<>();

    // will be used to draw the shape that represents a life that the
    // player has left
    private Point p1, p2, p3;

    // all of these variables are used for different screens
    private Rectangle quitButton = new Rectangle(WINDOW_WIDTH / 2 + 200, 500,
            150, 50);

    private Rectangle aboutButton = new Rectangle(WINDOW_WIDTH / 2 - 70, 500,
            150, 50);

    private Rectangle playButton = new Rectangle(WINDOW_WIDTH / 2 - 330, 500,
            150, 50);

    private Rectangle playAgainButton = new Rectangle(WINDOW_WIDTH / 2 - 330,
            500, 150, 50);

    // represents collections of objects that need to be deleted from the game
    ArrayList<MasklessPeople> peopleToDelete = new ArrayList<>();
    ArrayList<Virus> virusesToDelete = new ArrayList<>();
    ArrayList<Particles> particlesToDelete = new ArrayList<>();
    ArrayList<Particles> germsToDelete = new ArrayList<>();

    // stores new viruses that get created when the old one splits
    ArrayList<Virus> virusesToAdd = new ArrayList<>();

    // sound effects
    File fire = new File("fire.wav");
    File thrust = new File("thrust.wav");
    File saucerBig = new File("saucerBig.wav");
    File bangLarge = new File("bangLarge.wav");
    File extraShip = new File("extraShip.wav");
    File end = new File("end.wav");

    /**
     * Constructs a pandemicGame and initializes it to be able to accept key
     * input.
     */
    public PandemicGame() {
        // Allow the game to receive key input
        setFocusable(true);

        addKeyListener(this);

        // initializes the game with viruses (7 for the first level)

        virusAdder(numViruses);

    }

    @Override
    /**
     * This is called when the user presses a key. It notifies the doctor object
     * about presses of up arrow, right arrow, left arrow, space bar, and the h
     * key. All other keys are ignored.
     * 
     * @param event - information about the key pressed
     */
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
        // if the game is currently being played, the user can interact with
        // these keys
        if (State == STATE.GAME) {
            switch (key) {
            case KeyEvent.VK_SPACE:
                particleList.add(doctor.shoot());
                playMusic(fire);
                break;
            case KeyEvent.VK_UP:
                doctor.accelerate();
                playMusic(thrust);
                break;
            case KeyEvent.VK_RIGHT:
                doctor.rotateClockwise();
                break;
            case KeyEvent.VK_LEFT:
                doctor.rotateAntiClockwise();
                break;
            case KeyEvent.VK_H:
                doctor.hyperspace();
                break;
            default:
            }
        }
        // if space is pressed on gameover screen, two possible screens come
        // next
        if (State == STATE.GAMEOVER) {
            if (key == KeyEvent.VK_SPACE) {
                try {
                    if (topTen()) {
                        State = STATE.ENTER_INIT;
                    } else {
                        State = STATE.EXIT_SCREEN;
                    }
                } catch (IOException e) {
                    State = STATE.EXIT_SCREEN;
                    e.printStackTrace();
                }

            }
        }
        // the level screen lets the player hit the spacebar to move to the next
        // level
        if (State == STATE.LEVEL) {
            if (key == KeyEvent.VK_SPACE) {
                State = STATE.GAME;
            }
        }
        // lets the player enter their name
        if (State == STATE.ENTER_INIT) {
            switch (key) {
            case KeyEvent.VK_A:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "A";
                break;
            case KeyEvent.VK_B:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "B";
                break;
            case KeyEvent.VK_C:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "C";
                break;
            case KeyEvent.VK_D:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "D";
                break;
            case KeyEvent.VK_E:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "E";
                break;
            case KeyEvent.VK_F:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "F";
                break;
            case KeyEvent.VK_G:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "G";
                break;
            case KeyEvent.VK_H:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "H";
                break;
            case KeyEvent.VK_I:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "I";
                break;
            case KeyEvent.VK_J:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "J";
                break;
            case KeyEvent.VK_K:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "K";
                break;
            case KeyEvent.VK_L:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "L";
                break;
            case KeyEvent.VK_M:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "M";
                break;
            case KeyEvent.VK_N:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "N";
                break;
            case KeyEvent.VK_O:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "O";
                break;
            case KeyEvent.VK_P:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "P";
                break;
            case KeyEvent.VK_Q:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "Q";
                break;
            case KeyEvent.VK_R:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "R";
                break;
            case KeyEvent.VK_S:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "S";
                break;
            case KeyEvent.VK_T:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "T";
                break;
            case KeyEvent.VK_U:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "U";
                break;
            case KeyEvent.VK_V:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "V";
                break;
            case KeyEvent.VK_W:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "W";
                break;
            case KeyEvent.VK_X:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "X";
                break;
            case KeyEvent.VK_Y:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "Y";
                break;
            case KeyEvent.VK_Z:
                if (inits.compareTo("_") == 0) {
                    inits = "";
                }
                inits += "Z";
                break;
            case KeyEvent.VK_ENTER:
                if (inits.compareTo("_") != 0) {

                    try {
                        addTopTen(inits);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    State = STATE.EXIT_SCREEN;
                }
                break;
            default:
            }

        }

    }

    @Override
    /**
     * This is called when the user releases the key after pressing it. It does
     * nothing.
     * 
     * @param e information about the key released
     */
    public void keyReleased(KeyEvent e) {
        // Nothing to do
    }

    @Override
    /**
     * This is called when the user presses and releases a key without moving
     * the mouse in between. Does nothing.
     * 
     * @param e information about the key typed.
     */
    public void keyTyped(KeyEvent e) {
        // Nothing to do
    }

    /**
     * Allows the game to play sounds.
     * 
     * @param Sound a sound file to play
     */
    public static void playMusic(File Sound) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Sound));
            clip.start();
        } catch (Exception e) {
            System.out.print("error");
        }
    }

    /**
     * Draws a representation of one life
     * 
     * This method creates a drawing that represents a life that the player has
     * left. The life will look exactly like the doctor object.
     * 
     * @param shiftInX - the x coordinate at which the leftmost point will be
     *                 drawn
     * @return a Polygon object
     */
    public Polygon drawPolygon(int shiftInX) {

        // creates three points
        int height = 30;
        p1 = new Point(shiftInX, height + 60);
        p2 = new Point(shiftInX + 5, -height + 80);
        p3 = new Point(shiftInX + 10, height + 60);

        Polygon doctorShape = new Polygon();

        // uses those three points to draw a polygon
        doctorShape.addPoint((int) p1.getX(), (int) p1.getY());
        doctorShape.addPoint((int) p2.getX(), (int) p2.getY());
        doctorShape.addPoint((int) p3.getX(), (int) p3.getY());

        return doctorShape;
    }

    /**
     * Draws all of the objects in the game.
     * 
     * @param g the graphics context to draw on
     */
    public void paintComponent(Graphics g) {
        // if in gameplay mode
        if (State == STATE.GAME) {
            // shows score and our copyright info on the screen
            g.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 20));
            g.setColor(Color.WHITE);
            g.drawString(Integer.toString(score), 50, 40);
            g.drawString("Produced by the one and only SpaceWonders",
                    this.getWidth() / 4, 30);

            // represents a gap so that the number of lives left are not all
            // drawn
            // on top of each other
            int shiftInX = 60;

            // draws polygons to represent the number of lives
            for (int i = 0; i < numLives; i++) {
                g.drawPolygon(drawPolygon(shiftInX));
                shiftInX += 15;
            }

            // paints doctor
            doctor.paint((Graphics2D) g);

            // paints every virus in the virusList
            for (int i = 0; i < virusList.size(); i++) {
                virusList.get(i).paint((Graphics2D) g);
            }

            // paints every particle in the particleList
            for (int i = 0; i < particleList.size(); i++) {
                particleList.get(i).paint((Graphics2D) g);
            }

            // paints every maskless person
            for (int i = 0; i < masklessPeopleList.size(); i++) {
                masklessPeopleList.get(i).paint((Graphics2D) g);
            }

            // paints every germ
            for (int i = 0; i < germList.size(); i++) {
                germList.get(i).paint((Graphics2D) g);

            }
        }

        // handles menu screen
        else if (State == STATE.MENU) {
            menuRender(g);
        }

        // handles about screen
        else if (State == STATE.ABOUT) {
            aboutRender(g);
        }

        // handles new level screen
        else if (State == STATE.LEVEL) {
            levelRender(g);
        }

        // handles gameover screen
        else if (State == STATE.GAMEOVER) {
            gameoverRender(g);
        }

        // handles highscore screen
        else if (State == STATE.ENTER_INIT) {
            try {
                enterInitRender(g);
            } catch (IOException e) {
                System.out.println("IOException in paintComponent thrown");
                e.printStackTrace();
            }
        }

        // handles screen for when user ends the game with no new highscore
        else if (State == STATE.EXIT_SCREEN) {
            // shows score and our copyright info on the screen
            g.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 20));
            g.setColor(Color.WHITE);
            g.drawString(Integer.toString(score), 50, 50);
            g.drawString("Produced by the one and only SpaceWonders", 300, 550);
            try {
                exitScreenRender(g);
            } catch (FileNotFoundException e) {
                System.out.println("IOException in paintComponent thrown");
                e.printStackTrace();
            }
        }
    }

    @Override
    /**
     * Updates the animated object for the next frame of the animation and
     * repaints the window.wcc
     */
    protected void nextFrame() {
        this.addMouseListener(mouseInput);

        // will be used to tell MasklessPeople how often to shoot
        int shootTimer = 0;

        // if the game is currently being played
        if (State == STATE.GAME) {
            // calls nextFrame for all objects except masklessPeople
            individualNextFrames();

            // create a maskless person every 200 frames
            if (time == 200) {
                masklessPeopleCreate();
            }

            // handles nextFrame for masklessPeople
            masklessPeopleNextFrame(masklessPeopleList);

            repaint();

            // all three of these check for various collisions
            virusCollisionChecker();

            masklessPeopleCollisionChecker();

            germCollisionChecker();

            // all deleted particles get removed from their respective lists
            virusList.removeAll(virusesToDelete);
            particleList.removeAll(particlesToDelete);
            germList.removeAll(germsToDelete);
            masklessPeopleList.removeAll(peopleToDelete);

            // new viruses get added to the virus list
            virusList.addAll(virusesToAdd);

            particlesToDelete.clear();
            virusesToDelete.clear();
            germsToDelete.clear();
            peopleToDelete.clear();
            virusesToAdd.clear();

            // switches to next level
            levelSwitcher();

            // timers are updated
            time += 1;
            shootTime += 1;
        }
    }

    /**
     * Handles collisions with viruses.
     */
    void virusCollisionChecker() {
        // goes through every virus in the game
        for (Virus virus : virusList) {

            if (checkCollision(virus, doctor)) {
                // if virus collides with doctor, both disappear
                virus.disappear();
                virusesToDelete.add(virus);

                playMusic(bangLarge);

                doctorRespawn();

            }
            // goes through every vaccine particle to see if it has intersected
            // with one of the viruses
            for (int i = 0; i < particleList.size(); i++) {
                if (checkCollision(virus, particleList.get(i))) {
                    // if a collision has happened, both disappear
                    particleList.get(i).disappear();
                    particlesToDelete.add(particleList.get(i));

                    // the virus splits into two, both of which are added to
                    // virus list
                    virusesToAdd.addAll(virus.split());
                    virusesToDelete.add(virus);

                    // score is updated with the virus's point value
                    per10000score += virus.getPtValue();
                    score += virus.getPtValue();

                    playMusic(bangLarge);

                    // every 10000 points, a new life will be given
                    if (per10000score >= 10000) {
                        numLives += 1;
                        playMusic(extraShip);

                        // resets score so a new life is only given every
                        // 10,000 points
                        per10000score = 0;
                    }
                    // if the maximum score has been reached, the game is now
                    // over
                    if (score >= 99990) {
                        playMusic(end);
                        State = STATE.GAMEOVER;
                    }
                }
                // if the particle made it off screen without colliding, it gets
                // removed
                // from the list of particles
                else if ((particleList.get(i).getX() > this.getWidth()
                        || particleList.get(i).getX() < 0)
                        || (particleList.get(i).getY() < 0 || particleList
                                .get(i).getY() > this.getHeight())) {
                    particlesToDelete.add(particleList.get(i));
                }
            }
        }
    }

    /**
     * Handles collision checking with masklessPeople
     */
    void masklessPeopleCollisionChecker() {
        // if a maskless person undergoes a collision
        for (int i = 0; i < masklessPeopleList.size(); i++) {
            // if the doctor hits a maskless person
            if (checkCollision(masklessPeopleList.get(i), doctor)) {
                // the masklessPerson and the doctor both disappear
                masklessPeopleList.get(i).disappear();
                peopleToDelete.add(masklessPeopleList.get(i));

                playMusic(bangLarge);

                doctorRespawn();

            }

            for (int j = 0; j < particleList.size(); j++) {
                // if a vaccine particle has collided with a maskless person
                if (checkCollision(particleList.get(j),
                        masklessPeopleList.get(i))) {
                    // the vaccine particle disappears
                    particleList.get(j).disappear();
                    particlesToDelete.add(particleList.get(j));

                    // the masklessPerson disappears
                    masklessPeopleList.get(i).disappear();
                    peopleToDelete.add(masklessPeopleList.get(i));

                    // score is updated accordingly
                    per10000score += 750;
                    score += 750;

                    playMusic(bangLarge);

                    // every 10000 points, a new life will be given
                    if (per10000score >= 10000) {
                        numLives += 1;
                        playMusic(extraShip);

                        // resets score so a new life is only given every
                        // 10,000 points
                        per10000score = 0;
                    }
                    // if the maximum score has been reached, the game is now
                    // over
                    if (score >= 99990) {
                        playMusic(end);
                        State = STATE.GAMEOVER;
                    }
                }
                // if the particle made it off screen without colliding, it gets
                // removed
                // from the list of particles
                else if ((particleList.get(j).getX() > this.getWidth()
                        || particleList.get(j).getX() < 0)
                        || (particleList.get(j).getY() < 0 || particleList
                                .get(j).getY() > this.getHeight())) {
                    particlesToDelete.add(particleList.get(j));
                }
            }
        }
    }

    /**
     * Handles collision checking with germs.
     */
    void germCollisionChecker() {
        // checks for collisions between germs and doctor
        for (int i = 0; i < germList.size(); i++) {
            if (checkCollision(doctor, germList.get(i))) {
                // if a collision has happened, both disappear
                germList.get(i).disappear();
                germsToDelete.add(germList.get(i));

                playMusic(bangLarge);

                doctorRespawn();
            }
        }
    }

    /**
     * Calls multiple objects' nextFrame methods.
     */
    void individualNextFrames() {
        // runs doctor's nextFrame method
        doctor.nextFrame();

        // calls nextFrame for every virus
        for (int i = 0; i < virusList.size(); i++) {
            virusList.get(i).nextFrame();
        }

        // calls nextFrame for every particle in the game
        for (int i = 0; i < particleList.size(); i++) {
            particleList.get(i).nextFrame();

        }

        // calls nextFrame for every germ
        for (int i = 0; i < germList.size(); i++) {
            germList.get(i).nextFrame();
        }
    }

    /**
     * Creates a masklessPerson.
     */
    void masklessPeopleCreate() {
        // create masklessperson
        MasklessPeople masklessPerson = new MasklessPeople(this);
        // add it to the list of maskless people
        masklessPeopleList.add(masklessPerson);

        germList.addAll(masklessPerson.shoot());

        // resets time to 0 so that a maskless person will appear once every 200
        // frames
        time = 0;
    }

    /**
     * Calls nextFrame method for masklessPerson.
     * 
     * @param list - represents the list of masklessPeople.
     */
    void masklessPeopleNextFrame(ArrayList<MasklessPeople> list) {
        for (int i = 0; i < list.size(); i++) {
            // gets rid of people that are off screen
            if (list.get(i).ifLeft()) {
                peopleToDelete.add(list.get(i));
            }

            list.get(i).nextFrame();

            // decides when masklessPerson should shoot
            if (shootTime == 360) {
                // adds all germs to the list
                germList.addAll(list.get(i).shoot());
                shootTime = 280;
            }
        }
    }

    /**
     * Lets the doctor respawn on screen.
     * 
     * Allows the doctor to reappear on screen if the player still has lives
     * left, or ends the game if the player is out of lives.
     */
    void doctorRespawn() {
        // as long as the user has 2 or more lives, the doctor disappears
        // and reappears elsewhere on screen
        if (numLives > 1) {
            loseLife();
            doctor.disappear();
            doctor.hyperspace();
        }

        // otherwise, the game is over
        else {
            loseLife();
            playMusic(end);
            State = STATE.GAMEOVER;
        }
    }

    /**
     * Switches game to the next level.
     */
    void levelSwitcher() {
        // level switching only happens if all viruses have disappeared
        if (virusList.size() == 0) {
            numViruses += 1;
            // indicates that we are switching to next level
            virusAdder(numViruses);
            State = STATE.LEVEL;
            level++;
        }
    }

    /**
     * Sets gameplay with a certain number of viruses.
     * 
     * This method takes in a number of viruses that will change depending on
     * what level the player is playing. All of the viruses will be added to the
     * list of viruses that is used in each level.
     * 
     * @param numberOfViruses - a number of viruses
     */
    int virusAdder(int numberOfViruses) {
        for (int i = 0; i < numberOfViruses; i++) {
            // creates a new virus and adds it to the list
            Virus virus = new Virus(this);
            virusList.add(virus);

        }
        // for testing purposes only
        return virusList.size();
    }

    /**
     * Check whether two object collide. This tests whether their shapes
     * intersect.
     * 
     * @param shape1 the first shape to test
     * @param shape2 the second shape to test
     * @return true if the shapes intersect
     */
    boolean checkCollision(OnscreenObject shape1, OnscreenObject shape2) {
        return shape2.getShape().intersects(shape1.getShape().getBounds2D());
    }

    /**
     * This method takes one life away from the player.
     */
    void loseLife() {
        numLives -= 1;
    }

    /**
     * Creates the game's intro screen.
     * 
     * @param g - the graphics context to draw on
     */
    public void menuRender(Graphics g) {
        Font fnt = new Font("arial", Font.BOLD, 60);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
        g.setFont(fnt);
        g.setColor(Color.white);
        g.drawString("Welcome To PANDEMIC!", 100, 100);
        g.setFont(buttonFont);

        // may need to delete this line
        g.drawString("Produced by the one and only Space Wonders",
                this.getWidth() / 4, 20);
        g.drawString(
                "The year is 2020, and there is a deadly pandemic sweeping the globe.",
                40, 150);
        g.drawString(
                "Your job is to eliminate it with the power of vaccines! Viruses will appear in random places ",
                40, 180);
        g.drawString(
                "around the screen, going at random speeds, and you have to try to inject them with vaccine  ",
                40, 210);
        g.drawString(
                "particles. You get 5 vaccine needles in total, and you can keep going until you have eliminated ",
                40, 240);
        g.drawString(
                "all of the viruses or until they have eliminated you, at which point you will respawn in a random, ",
                40, 270);
        g.drawString(
                "location. Be careful, though! Each virus will split into  ",
                40, 300);

        g.drawString(
                "two after it has been injected, until you reach the smallest size. ",
                40, 330);
        g.drawString(
                "When you have successfully eliminated one round of viruses, the game will switch to the next",
                40, 360);
        g.drawString(
                "level. Each level will contain one more virus than the previous one.",
                40, 390);
        g.drawString(
                "You will also have to face stupid people who have forgotten to wear their masks.",
                40, 420);
        g.drawString(
                "They will kill you if you touch them and will also cough deadly germs out in ",
                40, 450);
        g.drawString(
                "all directions at once, so try to inject or avoid them before you get hit!",
                40, 480);

        // Creates and draws buttons
        g.drawString("PLAY", playButton.x + 43, playButton.y + 34);
        g.drawString("ABOUT", aboutButton.x + 40, aboutButton.y + 34);
        g.drawString("QUIT", quitButton.x + 41, quitButton.y + 34);
        Graphics2D g2D = (Graphics2D) g;
        g2D.draw(playButton);
        g2D.draw(aboutButton);
        g2D.draw(quitButton);

    }

    /**
     * Creates the game's about page.
     * 
     * @param g - the graphics context to draw on
     */
    public void aboutRender(Graphics g) {
        Font fnt = new Font("Times New Romans", Font.PLAIN, 20);
        g.setFont(fnt);
        g.setColor(Color.yellow);

        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
        g.setFont(buttonFont);

        g.drawString("Point values: ", 400, 100);

        g.setColor(Color.white);

        // displays point values
        g.drawString("Big virus: 20 points", 400, 130);
        g.drawString("Medium virus: 50 points", 400, 160);
        g.drawString("Small virus: 100 points", 400, 190);
        g.drawString("Maskless person: 750 points", 400, 220);

        g.setColor(Color.yellow);
        g.drawString("KEYS ", 400, 250);

        // displays which keys the player can use
        g.setColor(Color.white);
        g.drawString("Spacebar = shoot ", 350, 280);
        g.drawString("Left arrow key = rotate left", 300, 310);
        g.drawString("Right arrow key = rotate right ", 300, 340);
        g.drawString("Up arrow key = accelerate ", 300, 370);
        g.drawString("H key = Move to Another Location on screen ", 300, 400);

        // displays buttons
        g.drawString("PLAY NOW", playButton.x + 24, playButton.y + 32);
        g.drawString("QUIT GAME", quitButton.x + 20, quitButton.y + 32);
        Graphics2D g2D = (Graphics2D) g;
        g2D.draw(playButton);
        g2D.draw(quitButton);

    }

    /**
     * Creates the game's gameOver page
     * 
     * @param g - the graphics context to draw on
     */
    public void gameoverRender(Graphics g) {
        Font fnt = new Font("Times New Romans", Font.PLAIN, 40);
        Font spaceFont = new Font("Times New Romans", Font.PLAIN, 20);
        g.setFont(fnt);
        g.setColor(Color.WHITE);

        g.drawString("GAME OVER!", this.getWidth() / 2 - 100,
                this.getHeight() / 2);

        g.setFont(spaceFont);
        g.drawString("Press space bar to continue", this.getWidth() / 2 - 100,
                this.getHeight() - 20);

    }

    /**
     * Creates the game's highscore page.
     * 
     * @param g - the graphics context to draw on
     * @throws IOException if file not found
     */
    public void enterInitRender(Graphics g) throws IOException {
        Font fnt = new Font("Times New Romans", Font.PLAIN, 40);
        g.setFont(fnt);
        g.setColor(Color.WHITE);
        g.drawString("Your score is one of the ten best", 150,
                this.getHeight() / 2 - 60);
        g.drawString("Please enter your initials", 150,
                this.getHeight() / 2 - 20);
        g.drawString(inits, 150, this.getHeight() / 2 + 40);

    }

    /**
     * Creates the game's level screen
     * 
     * @param g the graphics context to draw on
     */
    public void levelRender(Graphics g) {
        Font fnt = new Font("Times New Romans", Font.PLAIN, 40);
        Font spaceFont = new Font("Times New Romans", Font.PLAIN, 20);
        g.setFont(fnt);
        g.setColor(Color.WHITE);
        String levelString = "LEVEL " + level;

        g.drawString(levelString, this.getWidth() / 2 - 100,
                this.getHeight() / 2);

        g.setFont(spaceFont);
        g.drawString("Press space bar to continue", this.getWidth() / 2 - 100,
                this.getHeight() - 20);
    }

    /**
     * Creates the page that shows if the game is over but the user does not
     * have a highscore
     * 
     * @param g - the graphics context to draw on
     * @throws FileNotFoundException - if file not found
     */
    public void exitScreenRender(Graphics g) throws FileNotFoundException {
        Font fnt = new Font("Times New Romans", Font.PLAIN, 40);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
        g.setFont(fnt);
        g.setColor(Color.WHITE);

        File f = new File("HighScore.txt");
        Scanner highScore = new Scanner(f);
        int place = 1;

        g.drawString("High Scores:", 300, 300 - 180);
        while (highScore.hasNextLine()) {
            g.drawString(place + " " + highScore.nextLine(), 300,
                    300 - 180 + (40 * place));
            place++;
        }
        highScore.close();

        g.setFont(buttonFont);
        g.drawString("PLAY AGAIN", playAgainButton.x + 15,
                playAgainButton.y + 32);
        g.drawString("QUIT GAME", quitButton.x + 20, quitButton.y + 32);
        Graphics2D g2D = (Graphics2D) g;
        g2D.draw(playAgainButton);
        g2D.draw(quitButton);
    }

    /**
     * The method sets the game up to be played again
     */
    static void playAgain() {
        score = 0;
        
        game.doctor = new Doctor(game);

        numLives = 4;

        numViruses = 7;

        inits = "_";

        time = 0;

        game.level = 1;

        game.virusList.clear();

        game.particleList.clear();

        game.masklessPeopleList.clear();

        game.germList.clear();

        game.virusAdder(numViruses);
    }

    /**
     * Checks if player score is in top ten stored in a file on the computer.
     * 
     * @return boolean true if the player's score is in the top ten, false if
     *         not
     * @throws IOException
     */
    boolean topTen() throws IOException {
        File f = new File(highScoreFile);
        Scanner highScore = new Scanner(f);
        List<Integer> scores = new ArrayList<Integer>();
        while (highScore.hasNextInt()) {
            scores.add(highScore.nextInt());
            highScore.next();
        }
        highScore.close();
        // Test if player's score is higher than any
        // of the previous high scores
        for (int i = 0; i < scores.size(); i++) {
            if (score > scores.get(i)) {
                return true;
            }
        }
        // If there are not yet ten high scores,
        // then any score should be added to the list
        if (scores.size() < 10) {
            return true;
        }

        return false;
    }

    /**
     * Adds highscores to a highscore file.
     * 
     * @param name - the name of the player
     * @throws IOException
     */
    void addTopTen(String name) throws IOException {
        String file = "HighScore.txt";
        File f = new File(file);
        Scanner highScore = new Scanner(f);
        List<Integer> scores = new ArrayList<Integer>();
        List<String> names = new ArrayList<String>();

        while (highScore.hasNext()) {

            scores.add(highScore.nextInt());
            names.add(highScore.next());
        }

        highScore.close();

        boolean added = false; 
        for (int i = 0; i < scores.size(); i++) {
            // if the player's score is in the top ten
            // the program adds them into the ordered list
            // and deletes the last person on the list to
            // keep the number of people at 10.
            if (score > scores.get(i)) {
                scores.add(i, score);
                names.add(i, name);
                added = true;
                if (scores.size() > 10) {
                    scores.remove(scores.size() - 1);
                }
                break;
            }

        }
        
        // if there are fewer than 10 scores, the current
        // score gets added to the file no matter what
        if (scores.size() < 10 && !added) {
            scores.add(score);
            names.add(name);
        }

        // if there are no scores, the current score gets added
        // no matter what
        if (scores.size() == 0 && !added) {
            scores.add(score);
            names.add(name);
        }

        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (int j = 0; j < scores.size(); j++) {
            printWriter.println(scores.get(j) + " " + names.get(j));
        }
        printWriter.close();
    }

    /**
     * a getter for the width of the window
     * 
     * @return window_width 0 the width of the window
     */
    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    /**
     * a getter for the height of the window
     * 
     * @return window_height - the height of the window
     */
    public static int getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    /**
     * Gets the doctor. FOR TESTING PURPOSES ONLY.
     * 
     * @return doctor - the doctor
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Gets time. FOR TESTING PURPOSES ONLY.
     * 
     * @return time - the timer in the game
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets time. FOR TESTING PURPOSES ONLY.
     * 
     * @param newTime - a new time
     */
    public static void setTime(int newTime) {
        time = newTime;
    }

    /**
     * Gets size of VirusList. FOR TESTING PURPOSES ONLY.
     * 
     * @return virusList.size() - the size of the virus list
     */
    public int getVirusListSize() {
        return virusList.size();
    }

    /**
     * Gets size of GermList. FOR TESTING PURPOSES ONLY.
     * 
     * @return germlist.size() - the size of the germList
     */
    public int getGermListSize() {
        return germList.size();
    }

    /**
     * Gets actual masklessPeopleList. FOR TESTING PURPOSES ONLY.
     * 
     * @return masklessPeopleList - a list of people
     */
    public ArrayList<MasklessPeople> getMasklessPeopleList() {
        return masklessPeopleList;
    }

    /**
     * Gets actual VirusList. FOR TESTING PURPOSES ONLY.
     * 
     * @return virusList - a list of viruses
     */
    public ArrayList<Virus> getVirusList() {
        return virusList;
    }

    /**
     * gets virus to delete list. FOR TESTING PURPOSES ONLY.
     * 
     * @return virusesToDelete - a list of viruses to delete
     */
    public ArrayList<Virus> getVirusesToDeleteList() {
        return virusesToDelete;
    }

    /**
     * gets particles to delete list. FOR TESTING PURPOSES ONLY.
     * 
     * @return particlesToDelete - a list of particles to delete
     */
    public ArrayList<Particles> getParticlesToDeleteList() {
        return particlesToDelete;
    }

    /**
     * Gets actual germList. FOR TESTING PURPOSES ONLY.
     * 
     * @return germList - a list of germs
     */
    public ArrayList<Particles> getGermList() {
        return germList;
    }

    /**
     * Gets actual vaccine particle list. FOR TESTING PURPOSES ONLY.
     * 
     * @return particleList - a list of particles
     */
    public ArrayList<Particles> getParticleList() {
        return particleList;
    }

    /**
     * Empties the whole virus list. FOR TESTING PURPOSES ONLY.
     */
    public void emptyVirusList() {
        virusList.clear();
    }

    /**
     * Empties the whole list of people to delete. FOR TESTING PURPOSES ONLY.
     */
    public void emptyPeopleToDeleteList() {
        peopleToDelete.clear();
    }

    /**
     * Empties the whole list of particles. FOR TESTING PURPOSES ONLY.
     */
    public void emptyParticleList() {
        particleList.clear();
    }

    /**
     * Gets size of MasklessPeopleList. FOR TESTING PURPOSES ONLY.
     * 
     * @return masklessPeopleList.size() - size of masklessPeopleList
     */
    public int getMasklessPeopleListSize() {
        return masklessPeopleList.size();
    }

    /**
     * Gets size of peopleToDelete. FOR TESTING PURPOSES ONLY.
     * 
     * @return peopletoDelete.size() - size of masklessPeopleList
     */
    public int getPeopleToDeleteListSize() {
        return peopleToDelete.size();
    }

    /**
     * This method returns the total score.
     * 
     * @return score - the game's score
     */
    public int getScore() {
        return score;
    }

    /**
     * This method returns the level. FOR TESTING PURPOSES ONLY.
     * 
     * @return level - the game's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * This method returns the state. FOR TESTING PURPOSES ONLY.
     * 
     * @return state - the game's state
     */
    public STATE getState() {
        return State;
    }

    /**
     * Sets game's state. FOR TESTING PURPOSES ONLY.
     * 
     * @param state - the game's state
     */
    public static void setState(STATE state) {
        State = state;
    }

    /**
     * Sets game's shoot time. FOR TESTING PURPOSES ONLY.
     * 
     * @param time - the game's timer
     */
    public static void setShootTime(int time) {
        shootTime = time;
    }

    /**
     * Sets the score. FOR TESTING PURPOSES ONLY.
     * 
     * @param num - a new score
     */
    public static void setScore(int num) {
        score = num;
    }

    /**
     * Sets the per10000score. FOR TESTING PURPOSES ONLY.
     * 
     * @param num - a new score
     */
    public static void setPer10000Score(int num) {
        per10000score = num;
    }

    /**
     * This method returns the number of lives left.
     * 
     * @return numLives - the number of lives
     */
    public int getLives() {
        return numLives;
    }

    /**
     * Sets the number of lives. FOR TESTING PURPOSES ONLY.
     * 
     * @param lives - some number of lives
     */
    public static void setLives(int lives) {
        numLives = lives;
    }

    /**
     * Sets the level. FOR TESTING PURPOSES ONLY.
     * 
     * @param newLevel - a new level to switch to
     */
    public static void setLevel(int newLevel) {
        level = newLevel;
    }

    /**
     * Sets doctor. FOR TESTING PURPOSES ONLY.
     * 
     * @param doctor1 - a new doctor
     */
    public void setDoctor(Doctor doctor1) {
        doctor = doctor1;
    }

    /**
     * 
     * Sets the high score file. FOR TESTING PURPOSES ONLY.
     * 
     * @param fileName name of file to use to store high scores
     */
    public void setFile(String fileName) {
        highScoreFile = fileName;
    }

    /**
     * Main method.
     * 
     * @param args - some arguments
     */
    public static void main(String[] args) {

        // JFrame is the class for a window. Create the window,
        // set the window's title and its size.
        JFrame f = new JFrame();
        f.setTitle("Pandemic");
        f.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        f.getContentPane().setBackground(Color.BLACK);

        // Create the animation.
        game = new PandemicGame();

        game.addMouseListener(mouseInput);

        // Add the animation to the window
        Container contentPane = f.getContentPane();
        contentPane.add(game, BorderLayout.CENTER);

        // Display the window.
        f.setVisible(true);

        // This says that when the user closes the window, the
        // entire program should exit.
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.start();
    }

}
