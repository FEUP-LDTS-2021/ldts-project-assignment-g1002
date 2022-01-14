package game;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFrame;
import game.enemies.Dreg;
import game.enemies.Enemy;
import game.enemies.Vandal;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Game
{
    private Screen screen;
    private Level level;
    private Terminal terminal;
    private final int frameRateInMillis = 10;
    public Game() throws IOException, FontFormatException, URISyntaxException {
        loadLevel1();
        URL resource = getClass().getClassLoader().getResource("fate.ttf");
        File fontFile = new File(resource.toURI());
        Font font =  Font.createFont(Font.TRUETYPE_FONT, fontFile);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        Font loadedFont = font.deriveFont(Font.PLAIN,45);
        AWTTerminalFontConfiguration fontConfig = AWTTerminalFontConfiguration.newInstance(loadedFont);
        TerminalSize terminalSize = new TerminalSize(level.getNumColumns(), level.getNumRows());
        terminal = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize).setForceAWTOverSwing(true)
                .setTerminalEmulatorFontConfiguration(fontConfig).createTerminal();
        ((AWTTerminalFrame)terminal).addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        });
        screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
        screen.startScreen();
        screen.doResizeIfNecessary();
    }

    private void draw() throws IOException {
        screen.clear();
        level.draw(screen.newTextGraphics());
        screen.refresh();
    }
    /*public void drawMainMenu() throws IOException, InterruptedException {
        MainMenu a = new MainMenu(this);
        while(true) {
            screen.clear();
            a.showMenu(screen.newTextGraphics());
            screen.refresh();
            Thread.sleep(300);
            a.previousOption();
        }
    }*/

    public void run() throws IOException, InterruptedException {
        int rateOfEntitiesAction = frameRateInMillis * 2;
        while(!level.gameOver())
        {
            Thread.sleep(frameRateInMillis);
            draw();
            KeyStroke key = terminal.pollInput(); //pollInput is non-blocking
            if(key != null) {
                if (key.getKeyType() == KeyType.EOF) {
                    break;
                }
                processKey(key);
            }
            if(rateOfEntitiesAction == frameRateInMillis) {
                level.moveEnemies();
                level.moveBullets();
                level.checkCollisions();
                rateOfEntitiesAction = frameRateInMillis * 2;
            }
            else rateOfEntitiesAction--;
        }
        draw();
        if(level.getPlayer().getHealth() > 0) System.out.println("You won!");
        else System.out.println("You lose!");
    }

    private void processKey(KeyStroke key)
    {
        level.processKey(key);
    }

    private void loadLevel1()
    {
        level = new Level(25,50);
        Player player = new Player(new Position(1,1));
        Dreg dreg = new Dreg(new Position(8,8));
        Dreg dreg2 = new Dreg(new Position(8,5));
        Vandal vandal = new Vandal(new Position (8,2));
        List<Enemy> enemyList = new ArrayList<Enemy>();
        enemyList.add(vandal);
        enemyList.add(dreg);
        enemyList.add(dreg2);
        List<Wall> wallList = new ArrayList<Wall>();
        for(int i = 0; i < level.getNumRows();i++)
        {
            wallList.add(new Wall(new Position(level.getNumColumns()-1,i)));
            wallList.add (new Wall(new Position(0, i)));
        }
        for(int i = 0; i < level.getNumColumns(); i++)
        {
            wallList.add(new Wall(new Position(i,0)));
            wallList.add(new Wall(new Position(i,level.getNumRows()-1)));
        }
        for(int i = 0; i < level.getNumRows()/2;i++)
        {
            wallList.add(new Wall(new Position(5,i)));
        }
        level.generateEntities(player,enemyList,wallList);
    }

    public Screen getScreen() {
        return screen;
    }
}
