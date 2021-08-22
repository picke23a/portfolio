package pandemicGame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
 * Class to check input from the mouse
 * implements MouseListener
 */
public class MouseInput implements MouseListener {

    private static final int WINDOW_WIDTH = 900;

    private final int yamt = 500;

    private final int xamt = 200;

    private final int amt = 100;

    private final int amt2 = 50;

    private final int amt3 = 150;

    private final int amt4 = 130;

    private final int amt5 = 70;

    private final int amt6 = 80;

    @Override
    public void mouseClicked(MouseEvent e) {
        // empty method

    }

    /*
     * Handles when the quit button is clicked on the screen
     * 
     * @param e the point on the screen that is clicked
     */
    public void quitButtonPressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // If any point in the rectangle containing the quit button is pressed
        // and the state
        // of the game is the EXIT_SCREEN, then quit the game
        if ((mouseX >= WINDOW_WIDTH / 2 + amt
                && mouseX <= WINDOW_WIDTH / 2 + xamt + amt3)
                && (PandemicGame.State == PandemicGame.STATE.EXIT_SCREEN
                        || PandemicGame.State == PandemicGame.STATE.ABOUT)) {
            // TO QUIT!!
            if (mouseY >= yamt && mouseY <= yamt + amt2) {
                System.exit(1);
            }
        }

        // If any point in the rectangle containing the quit button is pressed
        // and the state of the game is either ABOUT or MENU, exit the game
        if ((mouseX >= WINDOW_WIDTH / 2 + amt
                && mouseX <= WINDOW_WIDTH / 2 + xamt + amt3)
                && (PandemicGame.State == PandemicGame.STATE.MENU
                        || PandemicGame.State == PandemicGame.STATE.ABOUT)) {
            // TO QUIT!!
            if (mouseY >= yamt && mouseY <= yamt + amt2) {
                System.exit(1);
            }
        }

    }

    /*
     * Handles when the play button is clicked on the screen
     * 
     * @param e the point on the screen that is clicked
     */
    public void playButtonPressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // If any point in the rectangle containing the quit button is pressed
        // and the state of the game is either ABOUT or MENU, start the game
        if (mouseX >= WINDOW_WIDTH / 2 - (xamt + amt4)
                && mouseX <= WINDOW_WIDTH / 2 - (xamt + amt4) + amt3
                && mouseY >= yamt && mouseY <= yamt + amt2) {
            if (PandemicGame.State == PandemicGame.STATE.MENU
                    || PandemicGame.State == PandemicGame.STATE.ABOUT) {
                PandemicGame.State = PandemicGame.STATE.GAME;
            } else if (PandemicGame.State == PandemicGame.STATE.EXIT_SCREEN) {
                PandemicGame.playAgain();
                PandemicGame.State = PandemicGame.STATE.GAME;
            }
        }

    }

    @Override
    /*
     * Handles when a button is clicked on the screen
     * 
     * @param e the point on the screen that is clicked
     */
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        playButtonPressed(e);

        quitButtonPressed(e);

        // If any point in the rectangle containing the about button is pressed,
        // then open the about page
        if (mouseX >= WINDOW_WIDTH / 2 - amt5
                && mouseX <= WINDOW_WIDTH / 2 - amt5 + amt3
                && (PandemicGame.State == PandemicGame.STATE.MENU)) {
            // TO About!!
            if (mouseY >= yamt && mouseY <= yamt + amt2) {
                System.out.println("I hit About");
                PandemicGame.State = PandemicGame.STATE.ABOUT;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // empty method

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // empty method

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // empty method

    }

}
