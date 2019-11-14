
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Man Khi Kim
 * This class contains the main info that is output on a screen.
 *
 */
public class Grid extends Frame {

    //2D array to store the game board
    int[][] grid = null;
    int numGames = 0, x = 20, y = 20, rectSize = 50;
    int[] character = {0, 0};

    BufferedImage image_character;
    BufferedImage image_grass;
    BufferedImage image_cheese;
    BufferedImage image_box;

    //images for elements within the grid
    final String IMAGE_CHARACTER_PATH = "C:\\Project V4\\src\\miki.png";
    final String IMAGE_GRASS_PATH = "C:\\Project V4\\src\\grass.png";
    final String IMAGE_CHEESE_PATH = "C:\\Project V4\\src\\cheese1.png";
    final String IMAGE_BOX_PATH = "C:\\Project V4\\src\\box.png";


    /**
     * Constructor method to initialize the board and other frame components
     */
    Grid() {
        super("Game");

        //initializes a grid 11 by 11
        grid = new int[11][11];

        //initialise all the values
        for (int[] gridGame : grid) {
            for (int b = 0; b < grid[0].length; b++) {
                gridGame[b] = 0;
            }
        }

        //initializes/reads the image
        try {
            image_character = ImageIO.read(new File(IMAGE_CHARACTER_PATH));
            image_grass = ImageIO.read(new File(IMAGE_GRASS_PATH));
            image_cheese = ImageIO.read(new File(IMAGE_CHEESE_PATH));
            image_box = ImageIO.read(new File(IMAGE_BOX_PATH));
        } catch (IOException e) {
            System.err.println(e);
        }

        //set frame components
        setSize(580, 600);
        setVisible(true);
        setResizable(false);

        //call setup method to fill the grid randomly
        setup();
    }

    /**
     * This method populates/creates the board with values
     */
    public void setup() {

        //call method to randomly generate the final destination of character
        int[] dest = getFinalDest();
        int[] field = {0, 0, -1, 0};

        //loops to fill the array with random values
        for (int a = 0; a < grid.length; a++) {
            for (int b = 0; b < grid[0].length; b++) {
                grid[a][b] = field[new Random().nextInt(4)];
            }
        }

        //set the final destination randomly
        grid[dest[0]][dest[1]] = 1;
    }

    /**
     * This method returns the x and y axis for final destination on the grid
     * @return returns the array with the final grid point
     */
    public int[] getFinalDest() {

        //create and store x an y values into an array
        int[] dest = new int[2];
        dest[0] = new Random().nextInt(grid.length - 2);
        dest[1] = new Random().nextInt(grid[0].length - 2);

        //return the array
        return dest;
    }

    /**
     * This method sets the location of the character on the grid
     * @return returns x and y axis values for the character
     */
    public int setLoc(int x, int y) {
        character[0] = x;
        character[1] = y;

        //return the x and y axis value for the character
        return grid[y][x];
    }

    /**
     * This method returns the length of the game grid
     * @returns the length of the grid
     */
    public int getLength() {
        return grid.length;
    }

    /**
     * When the game is over and final destination is found, this method resets the values to initials
     */
    public void end() {
        character[0] = 0;
        character[1] = 0;

        //increments the number of games played
        numGames++;
    }

    /**
     * This method creates the visual representation of the game
     * For all the grids on the board we generate the image depending on a case.
     */
    @Override
    public void paint(Graphics graphics) {

        graphics.setColor(Color.orange);

        x = 20;
        y = 30;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                switch (grid[row][col]) {
                    case 0:
                        graphics.drawImage(image_grass, col * rectSize + x, row * rectSize + y, 50, 50, null);
                        break;

                    case -1:
                        graphics.drawImage(image_box, col * rectSize + x, row * rectSize + y, 50, 50, null);
                        break;

                    case 1:
                        graphics.drawImage(image_cheese, col * rectSize + x, row * rectSize + y, 50, 50, null);
                        break;

                    default:
                        graphics.drawImage(image_grass, col * rectSize + x, row * rectSize + y, 50, 50, null);
                        break;
                }
            }
        }


        //sets up the main character (mouse)
        graphics.drawImage(image_character, character[0] * rectSize + x, character[1] * rectSize + y, 50, 50, null);

        //sets up the text showing iterations count
        graphics.setColor(Color.RED);
        graphics.drawString(numGames + " ITERATIONS", 229, 590);

        try {
            //refresh rate of the picture
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

        //repaint method to repaint the board
        repaint();
    }
}
