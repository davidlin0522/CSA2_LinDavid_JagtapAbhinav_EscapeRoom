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
    Input \'trap\' or \'t\', optionally followed by r/l/u/d, to spring a trap \n
    Input \'check\' or \'c\' to look for a trap in all four directions \n
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
    int invalidVal = 5; // penalty for typing an unrecognized command
    int noTrapVal = 5; // penalty for checking and finding nothing nearby

    Scanner in = new Scanner(System.in);
    String[] validCommands = { "right", "left", "up", "down", "r", "l", "u", "d",
    "jump", "jr", "jumpleft", "jl", "jumpup", "ju", "jumpdown", "jd",
    "pickup", "p", "trap", "t", "check", "c", "quit", "q", "replay", "help", "?"};

    // set up game
    boolean play = true;
    while (play)
    {
      System.out.print("Enter a command (help for a list)\n>");
      String line = in.nextLine().trim().toLowerCase();
      // "trap" can take a second word for direction, e.g. "trap d" checks the space below
      String[] words = line.split("\\s+");
      String command = words[0];
      String dir = words.length > 1 ? words[1] : "";

      if (!isValidCommand(command, validCommands))
      {
        System.out.println("Invalid input. Please try again");
        score -= invalidVal;
        System.out.println("score=" + score + " steps=" + game.getSteps());
        continue;
      }

      // px/py reset each turn since movePlayer() moves relative to where the player already is
      px = 0;
      py = 0;

      // y grows going DOWN the screen, so up is negative
      if (command.equals("right") || command.equals("r"))
      {
        px = m;
      }
      else if (command.equals("left") || command.equals("l"))
      {
        px = -m;
      }
      else if (command.equals("up") || command.equals("u"))
      {
        py = -m;
      }
      else if (command.equals("down") || command.equals("d"))
      {
        py = m;
      }
      // a jump clears one space, so it moves two spaces at once
      else if (command.equals("jump") || command.equals("jr"))
      {
        px = 2 * m;
      }
      else if (command.equals("jumpleft") || command.equals("jl"))
      {
        px = -2 * m;
      }
      else if (command.equals("jumpup") || command.equals("ju"))
      {
        py = -2 * m;
      }
      else if (command.equals("jumpdown") || command.equals("jd"))
      {
        py = 2 * m;
      }
      // pick up the prize on the space the player is standing on
      else if (command.equals("pickup") || command.equals("p"))
      {
        score += game.pickupPrize();
      }
      // spring a trap: with no direction, checks your own space; with a direction (trap d, trap u, ...)
      // it checks the adjacent space instead, so you can clear a trap before ever stepping on it.
      // trapped=false here since this is a deliberate, informed spring, so a hit pays out normally
      else if (command.equals("trap") || command.equals("t"))
      {
        int tx = 0;
        int ty = 0;
        if (dir.equals("right") || dir.equals("r"))
        {
          tx = m;
        }
        else if (dir.equals("left") || dir.equals("l"))
        {
          tx = -m;
        }
        else if (dir.equals("up") || dir.equals("u"))
        {
          ty = -m;
        }
        else if (dir.equals("down") || dir.equals("d"))
        {
          ty = m;
        }
        score += game.springTrap(tx, ty, false);
      }
      // look at all four adjacent spaces for traps; a wasted check with nothing nearby costs points
      else if (command.equals("check") || command.equals("c"))
      {
        boolean found = false;
        if (game.isTrap(m, 0, false))
        {
          System.out.println("trap to your right");
          found = true;
        }
        if (game.isTrap(-m, 0, false))
        {
          System.out.println("trap to your left");
          found = true;
        }
        if (game.isTrap(0, -m, false))
        {
          System.out.println("trap above you");
          found = true;
        }
        if (game.isTrap(0, m, false))
        {
          System.out.println("trap below you");
          found = true;
        }
        if (!found)
        {
          System.out.println("no traps in any direction");
          score -= noTrapVal;
        }
      }
      // reset the board; replay() itself returns the win/loss score for the run just finished
      else if (command.equals("replay"))
      {
        System.out.println("steps=" + game.getSteps());
        score += game.replay();
      }
      else if (command.equals("help") || command.equals("?"))
      {
        System.out.println(helpMessage);
      }
      else if (command.equals("quit") || command.equals("q"))
      {
        play = false;
      }

      // only movement/jump commands set px or py, so this skips movePlayer for everything else
      if (px != 0 || py != 0)
      {
        // movePlayer returns a penalty for hitting a wall or going off the grid, -1 for a normal move
        int moveResult = game.movePlayer(px, py);
        score += moveResult;

        // walking onto an unsprung trap springs it automatically; trapped=true keeps it silent on the
        // "ahead" warning and makes springTrap pay out a penalty instead of a reward for the surprise
        if (moveResult == -1 && game.isTrap(0, 0, true))
        {
          System.out.println("YOU STEPPED ON A TRAP!");
          score += game.springTrap(0, 0, true);
        }
      }

      // show the player where they stand after every command
      System.out.println("score=" + score + " steps=" + game.getSteps());
    }

    // the game is over: check if the player reached the far right wall
    score += game.endGame();

    System.out.println("score=" + score);
    System.out.println("steps=" + game.getSteps());
  }

  // returns true if command is one of the strings in validCommands
  public static boolean isValidCommand(String command, String[] validCommands)
  {
    for (String valid : validCommands)
    {
      if (command.equals(valid))
      {
        return true;
      }
    }
    return false;
  }
}
