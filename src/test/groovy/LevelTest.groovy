import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import game.Bullet
import game.Level
import game.Player
import game.Position
import game.Wall
import game.enemies.Dreg
import game.enemies.Enemy
import game.enemies.Vandal
import game.weapons.HandCannon
import spock.lang.Specification



class LevelTest extends Specification
{

    private Level level
    private Player player
    private Vandal vandal
    private Vandal vandal2
    private List<Enemy> enemyList
    private List<Wall> wallList

    void 'setup'()
    {
        level = new Level(10,10)
        player = new Player(new Position(1,1))
        vandal = new Vandal(new Position(8,8))
        vandal2 = new Vandal(new Position(8,5))
        enemyList = new ArrayList<Enemy>()
        enemyList.add(vandal)
        enemyList.add(vandal2)
        wallList = new ArrayList<Wall>()
        for(int i = 0; i < level.getNumRows();i++)
        {
            wallList.add(new Wall(new Position(level.getNumRows()-1,i)))
            wallList.add (new Wall(new Position(0, i)))
        }
        for(int i = 0; i < level.getNumColumns(); i++)
        {
            wallList.add(new Wall(new Position(i,0)))
            wallList.add(new Wall(new Position(i,level.getNumColumns()-1)))
        }
    }

    def 'Level Creation'()
    {
        when:
        int row = level.getNumRows()
        int columns = level.getNumColumns()
        then:
        row == 10
        columns == 10
    }

    def 'Level Entity Introduction'()
    {
        when:
        level.generateEntities(player,enemyList,wallList)
        then:
        level.getCharacterAt(1,1) == ('p' as char)
        level.getCharacterAt(8,5) == ('v' as char)
        level.getCharacterAt(8,8) == ('v' as char)
        level.getCharacterAt(0,5) == ('#' as char)
    }

    def 'Level player movement'()
    {
        given:
        level.generateEntities(player,enemyList,wallList)
        KeyStroke key1 = Stub(KeyStroke.class)
        key1.getCharacter() >> 'w' >> 'a' >> 'W' >> 'S' >> 'd' >> 's'
        key1.getKeyType() >> KeyType.Character
        when:
        level.processKey(key1)//should not work because of wall
        level.processKey(key1)//should not work because of wall
        level.processKey(key1)//should not work because of wall
        level.processKey(key1)
        level.processKey(key1)
        level.processKey(key1)
        then:
        level.getCharacterAt(2,3) == 'p'as char
        level.getPlayer().getPosition() == new Position(2,3)
    }

    def 'Level Enemy movement'()
    {
        given:
        level.generateEntities(player,enemyList,wallList)
        when:
        level.moveEnemies()

        then:
        level.getEnemyList()[0].getPosition() == new Position(8,7)
        level.getEnemyList()[1].getPosition() == new Position(7,5)
    }

    def 'Level Move Bullets'()
    {
        level.generateEntities(player,enemyList,wallList)
        Bullet bullet1 = new Bullet(new Position(1,1),new HandCannon(), 'S' as char,true)
        Bullet bullet2 = new Bullet(new Position(5,5),new HandCannon(), 'N' as char,true)
        Bullet bullet3 = new Bullet(new Position(5,3),new HandCannon(), 'W' as char,true)
        Bullet bullet4 = new Bullet(new Position(5,6),new HandCannon(), 'E' as char,true)
        level.addBullet(bullet1)
        level.addBullet(bullet2)
        level.addBullet(bullet3)
        level.addBullet(bullet4)
        when:
        level.moveBullets()
        then:
        level.getBullets().get(0).position == new Position(1,2)
        level.getBullets().get(1).position == new Position(5,4)
        level.getBullets().get(2).position == new Position(4,3)
        level.getBullets().get(3).position == new Position(6,6)
    }

    def 'Collisions'()
    {
        level.generateEntities(player,enemyList,wallList)
        Bullet bullet1 = new Bullet(new Position(1,1),new HandCannon(), 'N' as char,true)
        Bullet bullet2 = new Bullet(new Position(8,5),new HandCannon(), 'N' as char,true)
        Bullet bullet3 = new Bullet(new Position(8,3),new HandCannon(), 'N' as char,true)
        level.addBullet(bullet1)
        level.addBullet(bullet2)
        level.addBullet(bullet3)
        when:
        level.checkCollisions()
        level.checkCollisions()
        then:
        level.getPlayer().getHealth() == 110
        level.getEnemyList().size() == 2
        level.getBullets().size() == 1
    }

    def 'Level Bullet Creation'()
    {
        level.generateEntities(player,enemyList,wallList)
        KeyStroke key1 = Stub(KeyStroke.class)
        key1.getKeyType()  >> KeyType.Character
        key1.getCharacter() >> 'S' >> 'e'
        when:
        level.processKey(key1) // this should not generate a bullet
        level.processKey(key1)
        level.moveEnemies()
        then:
        level.getBullets().get(0).getDirection() == ('S' as char) //the player's bullet
        level.getBullets().size() == 2
    }
    def 'Player Healing'()
    {
        Player player1 = new Player(new Position(1,1))
        when:
        player.takeDamage(5)
        then:
        player.getHealing() == 60
        player1.getHealing() == 0
    }
}
