
/**
 * Documentation:
 * 
 * @author Damyan Dimov
 * @ID 1933248
 * @author Pietro Bonaldo Gregori
 * @ID 1964542
 * 
 * 
 * The program manages a virtual zoo park, taking into a consideration all given parameters and wanted validation, 
 * including what animals can live each other, what they must be fed and what not and etc.
 * 
 * Input:
 * - Commands
 *      0 t "name" h: Create an animal of type t with name "name" and assign to enclosure number h
 *      1 "name" h: Move the animal with name "name" to enclosure number h
 *      2 "name": Remove the animal with name "name"
 *      3 f x: Buy x amount of food of type f
 *      4 f x h: Feed x amount of food of type f to enclosure number h
 *      5+: Exit program
 * 
 * Output:
 * - For each command
 *      N: The command with number N was executed succesfully
 *      N!: There was an error with the command with the number N
 */

class AnimalKeeper {

    public static void main(String[] args) {
        MyZoo zoo = new MyZoo();
        zoo.run();
    }
}
