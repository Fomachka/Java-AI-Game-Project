/**
 *
 * @author Man Khi Kim
 * This class contains the initializes the UI that is output on a screen.
 *
 */
public class Main {

    /**
     *
     * @param args outputs the values for the parameters within the Grid class
     * @throws InterruptedException to stop the thread accordingly
     */
    public static void main(String[] args) throws InterruptedException {

        Grid grid = new Grid();

        float profit = (float) 0.55;
        float learn = (float) 0.15;
        float random = (float) 0.95;

        //creates a new instance of a character
        Character character = new Character(random, profit, learn, grid);

        //sets the maximum amount of games which can be played
        character.start(9999999);
    }
}

