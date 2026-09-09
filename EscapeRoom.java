/*
* Problem 1: Escape Room
* 
* V1.0
* 10/10/2019
* Copyright(c) 2019 PLTW to present. All rights reserved
*/
import java.util.Scanner;

/**
 * Create an escape room game where the player must navigate
 * to the other side of the screen in the fewest steps, while
 * avoiding obstacles and collecting prizes.
 */
public class EscapeRoom
{

      // describe the game with brief welcome message
      // determine the size (length and width) a player must move to stay within the grid markings
      // Allow game commands:
      //    right, left, up, down: if you try to go off grid or bump into wall, score decreases
      //    jump over 1 space: you cannot jump over walls
      //    if you land on a trap, spring a trap to increase score: you must first check if there is a trap, if none exists, penalty
      //    pick up prize: score increases, if there is no prize, penalty
      //    help: display all possible commands
      //    end: reach the far right wall, score increase, game ends, if game ended without reaching far right wall, penalty
      //    replay: shows number of player steps and resets the board, you or another player can play the same board
      // Note that you must adjust the score with any method that returns a score
      // Optional: create a custom image for your player use the file player.png on disk
    
      /**** provided code:
      // set up the game
      boolean play = true;
      while (play)
      {
        // get user input and call game methods to play 
        play = false;
      }
      */

  public static void main(String[] args) 
  {      
    // welcome message
    System.out.println("Welcome to EscapeRoom!");
    System.out.println("Get to the other side of the room, avoiding walls and invisible traps,");
    System.out.println("pick up all the prizes.\n");
    String helpMessage = """
    --------------------------------------------------------------------- \n
    Welcome to the escape room.\n
    Input \'right\' or \'r\', \'left\' or \'l\', \'up\' or \'u\', \'down\' or \'d\'
    to move right, left, up or down respectively \n 
    Input \'replay\' to reset your player position \n
    Input \'jump\' or \'jr\' to jump to the right \n
    Input \'jumpleft\' or \'jl\' to jump to the left\n
    Input \'jumpup\' or \'ju\' to jump upwards \n
    Input \'jumpdown\' or \'jd\' to jump downwards \n
    Input \'pickup\' or \'p\' to pickup items \n
    Input \'quit\' or \'q\' to quit the game \n
    Input \'help\' or \'?\' to print this message again! \n
    --------------------------------------------------------------------- \n
     """;


    GameGUI game = new GameGUI();
    game.createBoard();

    // size of move
    int m = 60;
    // individual player moves
    int px = 0;
    int py = 0;

    int score = 0;

    Scanner in = new Scanner(System.in);
    String[] validCommands = { "right", "left", "up", "down", "r", "l", "u", "d",
    "jump", "jr", "jumpleft", "jl", "jumpup", "ju", "jumpdown", "jd",
    "pickup", "p", "quit", "q", "replay", "help", "?"};

    // set up game
    boolean play = true;
    while (play)
    { 
      /* TODO: get all the commands working */
	  /* Your code here */
      Scanner scanner = new Scanner(System.in); 
      String next_command = scanner.nextLine();
      if (next_command.equals("quit") || next_command.equals("q")) {
        play = false;
      } else if (next_command.equalsIgnoreCase("right") || next_command.equalsIgnoreCase("r")) {
        score += game.movePlayer(m, 0);
      } else if (next_command.equalsIgnoreCase("left") || next_command.equalsIgnoreCase("l")) {
        score += game.movePlayer(-m, py);
      } else if (next_command.equalsIgnoreCase("up") || next_command.equalsIgnoreCase("u")) {
        score += game.movePlayer(0, -m);
      }else if (next_command.equalsIgnoreCase("down") || next_command.equalsIgnoreCase("d")) {
        score += game.movePlayer(0, m);
      }else if (next_command.equalsIgnoreCase("pickup") || next_command.equalsIgnoreCase("p")) {
       score +=  game.pickupPrize();
      }else if (next_command.equalsIgnoreCase("replay")) {
        //create a new game instance
       game.replay();
      }else if (next_command.equals("help") || next_command.equals("?")) {
        //create a new game instance
        System.out.println(helpMessage);
      }
      else{
        score -= 3;
      }

      System.out.println("current score:" + score);
      System.out.println("current steps:" + game.getSteps());
    }
  score += game.endGame();

  System.out.println("score=" + score);
  System.out.println("steps=" + game.getSteps());
  }}

        