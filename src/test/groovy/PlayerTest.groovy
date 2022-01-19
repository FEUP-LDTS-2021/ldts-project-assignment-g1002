import game.Player
import game.Position
import game.weapons.AutoRifle
import game.weapons.HandCannon
import spock.lang.Specification
class PlayerTest extends Specification
{
    def 'Player Creation'() {
        given:
        Player player = new Player(new Position(10,10));

        when:
        def health = player.getHealth()
        def color = player.getColor()
        then:
        color == "#000000"
        health == 150
        player.getPosition() == new Position(10,10)
    }
    def 'Move Player'()
    {
        given:
        Player player1 = new Player(new Position(10,10));
        Player player2 = new Player(new Position(10,10));
        when:
        player1.setPosition(player1.moveUp())
        player1.setPosition(player1.moveLeft())
        player2.setPosition(player2.moveRight())
        player2.setPosition(player2.moveDown())
        then:
        player1.getPosition() == new Position(9,9)
        player2.getPosition() == new Position(11,11)
    }

    def 'Player Shoot'()
    {
        given:
        Player player = new Player(new Position(10,10))
        player.setPrimaryWeapon(new AutoRifle())
        int ammo = player.getUsingWeapon().getAmmo()
        when:
        player.getUsingWeapon().shoot();
        then:
        player.getUsingWeapon().getAmmo() == ammo -1
    }

    def 'Player damaged'()
    {
        given:
        Player player = new Player(new Position(10,10))
        Player player1 = new Player(new Position(10,10))
        when:
        player.takeDamage(1)
        player1.takeDamage(160)
        then:
        player.getHealth() == 149
        player1.getHealth() == 0
    }
    def 'Player healing'()
    {
        given:
        Player player = new Player(new Position(10,10))
        Player player1 = new Player(new Position(10,10))
        when:
        player.takeDamage(1)
        player1.takeDamage(160)
        then:
        player.getHealing() == 60
        player1.getHealing() == 60
    }
}

