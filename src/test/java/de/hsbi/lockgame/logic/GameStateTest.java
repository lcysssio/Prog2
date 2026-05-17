package de.hsbi.lockgame.logic;

import static org.junit.jupiter.api.Assertions.*;

import de.hsbi.lockgame.model.CellType;
import de.hsbi.lockgame.model.Direction;
import de.hsbi.lockgame.model.Level;
import de.hsbi.lockgame.model.Pin;
import de.hsbi.lockgame.model.Position;
import de.hsbi.lockgame.model.Snake;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameStateTest {

    private CellType[][] emptyCells(int width, int height) {
        CellType[][] cells = new CellType[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = CellType.EMPTY;
            }
        }
        return cells;
    }

    @Test
    void constructorWorks() {
        CellType[][] cells = emptyCells(5, 5);
        Level level = new Level(5, 5, cells, List.of(), new Position(2, 2));
        Snake snake = new Snake(List.of(new Position(2, 2)));

        GameState gs = new GameState(level, snake, List.of(), GameState.Status.RUNNING, Direction.NONE);

        assertEquals(level, gs.level());
        assertEquals(snake, gs.snake());
        assertEquals(GameState.Status.RUNNING, gs.status());
        assertEquals(Direction.NONE, gs.pendingDirection());
    }

    @Test
    void tickWithNoneDoesNothing() {
        CellType[][] cells = emptyCells(5, 5);
        Level level = new Level(5, 5, cells, List.of(), new Position(2, 2));
        Snake snake = new Snake(List.of(new Position(2, 2)));
        GameState gs = new GameState(level, snake, List.of(), GameState.Status.RUNNING, Direction.NONE);

        GameState result = gs.tick();

        assertSame(gs, result);
    }

  @Test
  void snakeMovesRight() {
      CellType[][] cells = emptyCells(5, 5);
      Pin pin = new Pin(new Position(1, 1), Pin.State.LOW, Direction.RIGHT);
      Level level = new Level(5, 5, cells, List.of(pin), new Position(2, 2));
      Snake snake = new Snake(List.of(new Position(2, 2)));
      GameState gs = new GameState(level, snake, List.of(pin), GameState.Status.RUNNING, Direction.RIGHT);

      GameState result = gs.tick();

      assertEquals(3, result.snake().head().x());
      assertEquals(2, result.snake().head().y());
      assertEquals(GameState.Status.RUNNING, result.status());
  }

  @Test
  void snakeMovesUp() {
      CellType[][] cells = emptyCells(5, 5);
      Pin pin = new Pin(new Position(1, 1), Pin.State.LOW, Direction.RIGHT);
      Level level = new Level(5, 5, cells, List.of(pin), new Position(2, 2));
      Snake snake = new Snake(List.of(new Position(2, 2)));
      GameState gs = new GameState(level, snake, List.of(pin), GameState.Status.RUNNING, Direction.UP);

      GameState result = gs.tick();

      assertEquals(2, result.snake().head().x());
      assertEquals(1, result.snake().head().y());
      assertEquals(GameState.Status.RUNNING, result.status());
  }

    @Test
    void outOfBoundsLoses() {
        CellType[][] cells = emptyCells(3, 3);
        Level level = new Level(3, 3, cells, List.of(), new Position(2, 1));
        Snake snake = new Snake(List.of(new Position(2, 1)));
        GameState gs = new GameState(level, snake, List.of(), GameState.Status.RUNNING, Direction.RIGHT);

        GameState result = gs.tick();

        assertEquals(GameState.Status.LOST_OUT_OF_BOUNDS, result.status());
    }

    @Test
    void wallBlocksMovement() {
        CellType[][] cells = emptyCells(4, 4);
        cells[3][1] = CellType.WALL;

        Level level = new Level(4, 4, cells, List.of(), new Position(1, 1));
        Snake snake = new Snake(List.of(new Position(2, 1)));
        GameState gs = new GameState(level, snake, List.of(), GameState.Status.RUNNING, Direction.RIGHT);

        GameState result = gs.tick();

        assertEquals(Direction.NONE, result.pendingDirection());
        assertEquals(2, result.snake().head().x());
        assertEquals(1, result.snake().head().y());
    }

    @Test
    void wonWhenAllPinsHigh() {
        CellType[][] cells = emptyCells(5, 5);
        Pin pin1 = new Pin(new Position(1, 1), Pin.State.HIGH, Direction.RIGHT);
        Pin pin2 = new Pin(new Position(2, 2), Pin.State.HIGH, Direction.UP);

        Level level = new Level(5, 5, cells, List.of(pin1, pin2), new Position(2, 2));
        Snake snake = new Snake(List.of(new Position(2, 2)));
        GameState gs = new GameState(level, snake, List.of(pin1, pin2), GameState.Status.RUNNING, Direction.RIGHT);

        GameState result = gs.tick();

        assertEquals(GameState.Status.WON, result.status());
    }

    @Test
    void wonStateDoesNothing() {
        CellType[][] cells = emptyCells(5, 5);
        Level level = new Level(5, 5, cells, List.of(), new Position(2, 2));
        Snake snake = new Snake(List.of(new Position(2, 2)));
        GameState gs = new GameState(level, snake, List.of(), GameState.Status.WON, Direction.RIGHT);

        GameState result = gs.tick();

        assertSame(gs, result);
    }
}

