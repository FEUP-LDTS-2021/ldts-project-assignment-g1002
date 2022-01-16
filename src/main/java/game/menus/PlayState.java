package game.menus;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import game.Level;

public class PlayState implements State
{
    private final Level level;

    public PlayState(Level level)
    {
        this.level = level;
    }

    @Override
    public void show(TextGraphics graphics) {

    }

    @Override
    public void processInput(KeyStroke keystroke) {

    }

    public Level getLevel() {
        return level;
    }
}
