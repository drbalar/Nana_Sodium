import java.lang.Math;
import java.util.Random;
import java.io.*;

public class Game2048 {
  // === *** Provided class variables (Don't delete this section) *** ===//
  final public static int LEFT_INPUT = 0;
  final public static int DOWN_INPUT = 1;
  final public static int RIGHT_INPUT = 2;
  final public static int UP_INPUT = 3;
  
  final public static int VALUE_GRID_SETTING = 0;
  final public static int INDEX_GRID_SETTING = 1;
  
  private String GAME_CONFIG_FILE = "game_config.txt";
  
  private Game2048GUI gui;
  
  /*
   * position [0][0] represents the Top-Left corner and position [max][max]
   * represents the Bottom-Right corner
   */
  private int grid[][];
  
  // === *** Your class variables can be added starting here *** ===//
  private final int EMPTY_SLOT = -1;
  
  private int winningLevel;
  private long currentScore;
  private int currentLevel;
  private long highScore;
  
  /**
   * Constructs Game2048 object.
   *
   * @param gameGUI
   *            The GUI object that will be used by this class.
   */
  public Game2048(Game2048GUI gameGUI) {
    gui = gameGUI;
    
    // TO DO:
    // - create and initialize the grid array
    // - initialize the variables
    // - winningLevel (value read from text file)
    // - currentLevel
    // - currentScore
    // - insert the first number tile
    grid = new int[gui.NUM_ROW][gui.NUM_COLUMN];
    try {
      BufferedReader readGame = new BufferedReader(new FileReader("game_config.txt"));
      readGame.readLine();
      winningLevel = Integer.parseInt(readGame.readLine());
      readGame.close();
      BufferedReader readScore = new BufferedReader(new FileReader("high_score.txt"));
      highScore = Long.parseLong(readScore.readLine());
      readScore.close();
      currentLevel = 0;
      currentScore = 0;
      Random rn = new Random();
      int slot = rn.nextInt((gui.NUM_COLUMN * gui.NUM_ROW));
      int x = slot % gui.NUM_COLUMN;
      int y = slot / gui.NUM_ROW;
      grid[y][x] = rn.nextInt(2) + 1;
      gui.setNewSlotBySlotIndex(y, x, grid[y][x]);
    } catch (IOException e) {
      System.out.println("File could not be read!");
    }
  }
  
  /**
   * Place a new number tile on a random slot on the grid. This method is
   * called every time a key is released.
   */
  public void newSlot(boolean moved) {
    // TO DO: insert a new number tile on the grid
    // Make sure to check if a new tile should be inserted first
    // (a slide or a tile combination has occurred previously)
    if (moved) {
      Random rn = new Random();
      int slot, x, y;
      slot = rn.nextInt((gui.NUM_COLUMN * gui.NUM_ROW));
      x = slot % gui.NUM_COLUMN;
      y = slot / gui.NUM_ROW;
      for (int i = 0; i < 16; i++) {
        if (grid[y][x] == 0) {
          int number = rn.nextInt(5) + 1;
          if(number < 5) grid[y][x] = 1;
          else grid[y][x] = 2;
          gui.setNewSlotBySlotIndex(y, x, grid[y][x]);
          break;
        } else if (x == 3) {
          x = 0;
          y++;
        } else
          x++;
        if (y == 4)
          y = 0;
        if (i == 15)
          gui.showGameOver(highScore,currentScore);
      }
    }
  }
  
  /**
   * Plays the game by the direction specified by the user. This method is
   * called every time a button is pressed
   */
  public void play(int direction) {
    // TO DO: implement the action to be taken after an arrow key of the
    // specified direction is pressed
    boolean moved = false;
    int tempValue;
    if (direction == LEFT_INPUT) {
      for (int q = 0; q < gui.NUM_COLUMN; q++) {
        for (int i = 0; i < grid.length; i++) {
          if (grid[i][q] > 0) {
            for (int merge = q - 1; merge >= 0; merge--) {
              if (merge >= 0 && grid[i][merge] == grid[i][q]) {
                gui.clearSlot(i, q);
                currentScore = (long) Math.pow(2, grid[i][q]) + currentScore;
                gui.setScore(currentScore);
                moved = true;
                gui.setNewSlotBySlotIndex(i, merge, ++grid[i][q]);
                if (grid[i][q] == winningLevel)
                  gui.showGameWon(highScore,currentScore);
                tempValue = grid[i][q];
                grid[i][q] = 0;
                grid[i][merge] = tempValue;
                break;
              } else if (merge >= 0 && grid[i][merge] != 0)
                break;
            }
            for (int check = 0; grid[i][q] > 0 && check < q; check++)
              if (grid[i][check] == 0) {
              gui.clearSlot(i, q);
              gui.setNewSlotBySlotIndex(i, check, grid[i][q]);
              tempValue = grid[i][q];
              moved = true;
              grid[i][q] = 0;
              grid[i][check] = tempValue;
              break;
            }
          }
        }
      }
    }
    if (direction == RIGHT_INPUT) {
      for (int q = gui.NUM_COLUMN - 1; q >= 0; q--) {
        for (int i = 0; i < grid.length; i++) {
          if (grid[i][q] > 0) {
            for (int merge = q + 1; merge <= 3; merge++) {
              if (merge >= 0 && grid[i][merge] == grid[i][q]) {
                gui.clearSlot(i, q);
                currentScore = (long) Math.pow(2, grid[i][q]) + currentScore;
                gui.setScore(currentScore);
                moved = true;
                gui.setNewSlotBySlotIndex(i, merge, ++grid[i][q]);
                if (grid[i][q] == 11)
                  gui.showGameWon(highScore,currentScore);
                tempValue = grid[i][q];
                grid[i][q] = 0;
                grid[i][merge] = tempValue;
                break;
              } else if (merge >= 0 && grid[i][merge] != 0)
                break;
            }
            for (int check = 3; grid[i][q] > 0 && check > q; check--)
              if (grid[i][check] == 0) {
              gui.clearSlot(i, q);
              gui.setNewSlotBySlotIndex(i, check, grid[i][q]);
              tempValue = grid[i][q];
              moved = true;
              grid[i][q] = 0;
              grid[i][check] = tempValue;
              break;
            }
          }
        }
      }
    }
    if (direction == UP_INPUT) {
      for (int i = 0; i < grid.length; i++) {
        for (int q = 0; q < grid[i].length; q++) {
          if (grid[i][q] > 0) {
            for (int merge = i - 1; merge >= 0; merge--) {
              if (merge >= 0 && grid[merge][q] == grid[i][q]) {
                gui.clearSlot(i, q);
                currentScore = (long) Math.pow(2, grid[i][q]) + currentScore;
                gui.setScore(currentScore);
                moved = true;
                gui.setNewSlotBySlotIndex(merge, q, ++grid[i][q]);
                if (grid[i][q] == 11)
                  gui.showGameWon(highScore,currentScore);
                tempValue = grid[i][q];
                grid[i][q] = 0;
                grid[merge][q] = tempValue;
                break;
              } else if (merge >= 0 && grid[merge][q] != 0)
                break;
            }
            for (int check = 0; grid[i][q] > 0 && check < i; check++)
              if (grid[check][q] == 0) {
              gui.clearSlot(i, q);
              gui.setNewSlotBySlotIndex(check, q, grid[i][q]);
              tempValue = grid[i][q];
              moved = true;
              grid[i][q] = 0;
              grid[check][q] = tempValue;
              break;
            }
          }
        }
      }
    }
    if (direction == DOWN_INPUT) {
      for (int i = grid.length - 1; i >= 0; i--) {
        for (int q = 0; q < grid[i].length; q++) {
          if (grid[i][q] > 0) {
            for (int merge = i + 1; merge <= 3; merge++) {
              if (merge >= 0 && grid[merge][q] == grid[i][q]) {
                gui.clearSlot(i, q);
                currentScore = (long) Math.pow(2, grid[i][q]) + currentScore;
                gui.setScore(currentScore);
                moved = true;
                gui.setNewSlotBySlotIndex(merge, q, ++grid[i][q]);
                if (grid[i][q] == 11)
                  gui.showGameWon(highScore,currentScore);
                tempValue = grid[i][q];
                grid[i][q] = 0;
                grid[merge][q] = tempValue;
                break;
              } else if (merge >= 0 && grid[merge][q] != 0)
                break;
            }
            for (int check = 3; grid[i][q] > 0 && check > i; check--)
              if (grid[check][q] == 0) {
              gui.clearSlot(i, q);
              gui.setNewSlotBySlotIndex(check, q, grid[i][q]);
              tempValue = grid[i][q];
              moved = true;
              grid[i][q] = 0;
              grid[check][q] = tempValue;
              break;
            }
          }
        }
      }
    }
    int counter = 0;
    if (moved == false) {
      for (int y = 0; y < grid.length; y++) {
        for (int x = 0; x < grid[y].length; x++) {
          if (y != 0)
            if (grid[y][x] == grid[y - 1][x]) {
            moved = false;
            break;
          }
          if (x != 3)
            if (grid[y][x] == grid[y][x + 1]) {
            moved = false;
            break;
          }
          if (grid[y][x] > 0)
            counter = counter + 1;
        }
      }
      for (int y = 3; y >= 0; y--) {
        for (int x = 3; x >= 0; x--) {
          if (y != 0)
            if (grid[y][x] == grid[y - 1][x]) {
            moved = false;
            break;
          }
          if (x != 3)
            if (grid[y][x] == grid[y][x + 1]) {
            moved = false;
            break;
          }
          if (grid[y][x] > 0)
            counter = counter + 1;
        }
      }
    }
    if (counter == 32)
      gui.showGameOver(highScore,currentScore);
    newSlot(moved);
  }
}