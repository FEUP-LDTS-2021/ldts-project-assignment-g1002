package game.menus;

import game.Game;

public class PlayCommand extends Command
{

    private State oldState;

    public PlayCommand(Game game) {
        super(game);
    }

    @Override
    String getText() {

    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {
        ;
    }
}
