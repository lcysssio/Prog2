package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.model.*;
import java.util.List;

public final class GameState {
    private  Level level;
    private  Snake snake;
    private  List<Pin> pins;
    private  Status status;
    private  Direction pendingDirection;

  public GameState(
      Level level, Snake snake, List<Pin> pins, Status status, Direction pendingDirection) {
    // TODO: lege einen neuen GameState mit den übergebenen Informationen an
      this.level = level;
      this.snake = snake;
      this.pins = pins;
      this.status = status;
      this.pendingDirection = pendingDirection;
    ;
  }

  public Level level() {
    // TODO: Getter
      return level;

  }

  public Snake snake() {
    // TODO: Getter
      return snake;

  }

  public List<Pin> pins() {
    // TODO: Getter
        return pins;

  }

  public Status status() {
    // TODO: Getter
      return status;

  }

  public Direction pendingDirection() {
    // TODO: Getter
      return pendingDirection;

  }

    public GameState tick() {
        if (!status.isRunning() || pendingDirection == Direction.NONE) {
            return this;
        }
        var nextHead = snake.nextHead(pendingDirection);
        //  Spielfeld verlassen
        if (!level.isInside(nextHead)) {
            return new GameState(level, snake, pins, Status.LOST_OUT_OF_BOUNDS, pendingDirection);
        }
        // Wand
        if (level.cellAt(nextHead) == CellType.WALL) {
            return new GameState(level, snake, pins, status, Direction.NONE);
        }
        //  Selbstkollision
        if (snake.body().indexOf(nextHead) > 0) {
            return new GameState(level, snake, pins, Status.LOST_SELF_COLLISION, pendingDirection);
        }
        // Pins erstmal ignoriert
        var newSnake = snake.grow(pendingDirection);
        Pin pinAtNextHead = null;
        for (var pin : pins) {
            if (pin.position().x() == nextHead.x() && pin.position().y() == nextHead.y()) {
                pinAtNextHead = pin;
                break;
            }
        }

        if (pinAtNextHead != null) {
            boolean correctDirection = pendingDirection == pinAtNextHead.activationDirection();

            if (!pinAtNextHead.state().isSet() && correctDirection) {
                // aktiv
            } else {
                // blockieren
                return new GameState(level, snake, pins, status, Direction.NONE);
            }
        }

 //GEWONNEN WENN ALLE PINS HIGH SIND :D
         boolean allHigh = true;
        for (var pin : pins) {
            if (!pin.state().isSet()) {
                allHigh = false;
                break;
            }
        }

        if (allHigh) {
            return new GameState(level, snake, pins, Status.WON, pendingDirection);
        }

        return new GameState(level, newSnake, pins, status, pendingDirection);
    }

  public enum Status {
    RUNNING,
    WON,
    LOST_SELF_COLLISION,
    LOST_OUT_OF_BOUNDS;

    public boolean isRunning() {
      return this == RUNNING;
    }
  }
}
