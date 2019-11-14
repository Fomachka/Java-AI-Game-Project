import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Man Khi Kim
 * This class contains the movement initialized by the character,
 * how it chooses where to go based on a reward system and
 * calculates the most optimal route for a given task.
 *
 */
public class Character {
    private float profit, random, learn;

    //initializes Hashmap to store the Q-algorithm values
    HashMap<String, Float> hash;

    //array to store x & y points for a character's location
    int[] location = {0,5};

    Grid grid;
    double chanceRandom;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("[hh:MM:ss]: ");

    /**
     *
     * @param profit parameter based on generating the next step based on acquired reward
     * @param random chance of exploring an unplanned grid
     * @param learn the learning curve of the character
     * @param grid the grid of the game
     *
     */
    public Character(float random, float profit, float learn, Grid grid) {
        this.random = random;
        this.profit = profit;
        this.learn = learn;
        this.hash = new HashMap<>();
        this.grid = grid;
        this.chanceRandom = 0.0001;

    }

    /**
     *
     * @param current gives a current location of a character
     * @return all possible moves which character can make
     */
    public ArrayList<int[]> PossibleMoves(int[] current){

        //list to store the moves
        ArrayList <int[]> list = new ArrayList<>();

        //creates possible moves and store them in x and y array separately
        int[] x = {current[0]-1, current[0], current[0]+1};
        int[] y = {current[1]-1, current[1], current[1]+1};

        //loops to check for legal moves combinations and store them in the arraylist of moves
        for(int a = 0; a<x.length; a++) {
            for(int b = 0; b<y.length; b++) {
                int[] p = {x[a],y[b]};
                if(isPossible(p, current)) {
                    list.add(p);
                }
            }
        }

        return list;
    }

    /**
     *
     * @param next a location of the next possible grid that the character steps onto
     * @param loc represents current location of the character
     * @return return true or false based on the boolean conditions
     */
    public boolean isPossible(int[] next,int[] loc) {

        //checks if the points are within bounds and the new move is not same as current move and other rules are satisfied
        boolean legal = next[0]<grid.getLength() && next[0]>= 0 && next[1]<grid.getLength() && next[1]>= 0 &&
            !(next[0] == loc[0] && next[1] == loc[1]) && !(next[0] == (loc[0]+1) && next[1] == (loc[1]+1)) &&
            !(next[0] == (loc[0]-1) && next[1] == (loc[1]-1)) && !(next[0] == (loc[0]+1) && next[1] == (loc[1]-1)) &&
            !(next[0] == (loc[0]-1) && next[1] == (loc[1]+1));

        //returns true or false based on the above conditions
        return legal;
    }

    /**
     *
     * @return the best possible move based on the value of Q or the random move with a specified probability
     */
    public int[] getAction() {

        //list of legal moves
        ArrayList<int[]> list = PossibleMoves(location);

        //return randomly selected ove from the array
        if(new Random().nextFloat() < profit) {
            int r = new Random().nextInt(list.size());
            return list.get(r);
        }else {
            return max(list);
        }
    }

    /**
     *
     * @param list of all possible moves of the character
     * @return the move with the highest value of Q
     */
    public int[] max(ArrayList<int[]> list){

        float max = 0; int num = 0, maxNum = 0;

        String str = "";

        //loops to find the maximum value from the list of moves
        for (int a = 0; a < list.size(); a++) {

            //get a list of points
            int[] arr = list.get(a);

            //create a key string for hashmap
            str += arr[0] + "_" + arr[1];

            //checks if the Hashmap contains this key
            if(hash.containsKey(str)) {

                float v = hash.get(str);

                //update the max variable
                if(v > max) {
                    max = v;
                    maxNum = num;
                }
            }

            //reset the string key
            str = "";
            num++;
        }

        if(max == 0.0) {

            //returns a move selected randomly
            int r = new Random().nextInt(list.size());
            return list.get(r);
        }
        return list.get(maxNum);
    }

    /**
     *
     * @param curLocation current location of the character
     * @return the move with the highest value of Q from all the possible moves
     */
    public float getHighestReward(int[] curLocation) {

        float max = 0;
        String str = "";
        ArrayList<int[]> list = PossibleMoves(curLocation);

        //loops to find the maximum value from the list of moves
        for (int a = 0; a < list.size(); a++) {

            int[] array = list.get(a);
            str += array[0] + "_" + array[1];

            if(hash.containsKey(str)) {
                float v = hash.get(str);

                //updates the max variable
                if(v > max) {
                    max = v;
                }
            }

            //resets the string key
            str = "";
        }

        if(max == 0.0) {
            return 0f;
        }

        //prints out the string for a iteration counter
        if(max < 1){
            System.out.println(dateFormat.format(new Date()) + "Looking for the solution... "+max + String.format("%.4f", max));
        }else {
            System.out.println(dateFormat.format(new Date()) + "Successfully Found. Current score is " + String.format("%.4f", max));
        }

        return max;
    }


    /**
     *
     * @param action that character chooses to do
     * @param r reward based on the action
     */
    private void updateCharacter(int[] action, float r){

        //get the current position of the character
        int[] current = location;
        location[0] = action[0];
        location[1] = action[1];
        improveStep(current, r, location);//call method to get the updated move
    }

    /**
     *
     * @param current action for character to do
     * @param r reward for the action
     * @param next action to perform
     */
    public void improveStep(int[] current, float r, int[] next) {

        //creates a hashmap key using current location
        String str = current[0] + "_" + current[1];

        //calculates the current value
        float curr = hash.containsKey(str) ? hash.get(str): 0f;

        //calculates the next value
        float nextV = curr + learn*(r + random*getHighestReward(next) - curr);
        hash.put(str,nextV);//put it in the hashmap
    }

    /**
     *
     * @param counter counts the number of games
     *
     */
    public void start(int counter) throws InterruptedException {

        int reachFinish = 0;
        boolean thread = false;

        while(counter > 0) {

            float r = 0;

            while(r == 0) {

                int[] arr = getAction();

                //sets location of character of the grid game
                r = grid.setLoc(arr[0], arr[1]);

                //updates the position of character everytime
                updateCharacter(arr, r);

                if(r > 0.99) {

                    reachFinish++;
                    counter--;

                    if(reachFinish > 10) {
                      thread = true;
                    }

                    //re-initialise the position of character
                    location = new int[]{0,5};
                    profit -= chanceRandom;
                    break;
                }

                if(r == -1) {
                    grid.end();
                    counter--;
                    location = new int[]{0,5};
                    profit -= 0.0001;
                    break;
                }

                //after every iteration, make the thread sleep
                if(thread)
                    Thread.sleep(200);
            }
        }   
    }  
}

