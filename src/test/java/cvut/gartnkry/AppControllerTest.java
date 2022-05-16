package cvut.gartnkry;

import cvut.gartnkry.control.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.entities.Player;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;

import static org.junit.jupiter.api.Assertions.*;

public class AppControllerTest {
    private FxRobot robot;

    @Before
    public void setUp() throws InterruptedException {
        Thread t = new Thread("JavaFX Game testing Thread") {
            public void run() {
                Application.launch(AppController.class, "test_save.json");
            }
        };
        t.setDaemon(true);
        t.start();
        robot = new FxRobot();
        Thread.sleep(2000); // wait for game to load
    }

    /**
     * Functional test for basic game mechanics
     */
    @Test
    public void FunctionalTest() throws InterruptedException {
        Model model = Model.getInstance();
        Player player = model.getPlayer();
        Sprite playerSprite = player.getSprite();

        testPlayerMovement(KeyCode.A, false, true, playerSprite);
        testPlayerMovement(KeyCode.D, true, true, playerSprite);
        testPlayerMovement(KeyCode.W, false, false, playerSprite);
        testPlayerMovement(KeyCode.S, true, false, playerSprite);
        testPlayerSidewaysMovement(playerSprite);
        testCollisions(playerSprite);

        testDamage(player);
        testPickingUpItem(player);
        testShooting(model);
        testItemsSwitching(player);
        testHealing(player);

        Thread.sleep(500);
    }

    private void testHealing(Player player) {
        int prevHealth = player.getHealth();
        robot.type(KeyCode.C);
        assertGreaterThan(player.getHealth(), prevHealth);
    }

    private void testItemsSwitching(Player player) throws InterruptedException {
        robot.press(KeyCode.A);
        Thread.sleep(600);
        robot.release(KeyCode.A);
        Thread.sleep(40);
        assertEquals(player.getInventory().getSelectedItem().getName(), "Gun");
        robot.type(KeyCode.F);
        assertEquals(player.getInventory().getSelectedItem().getName(), "Bottle");
        robot.type(KeyCode.E);
        assertNull(player.getInventory().getSelectedItem());
        robot.type(KeyCode.F);
        assertEquals(player.getInventory().getSelectedItem().getName(), "Gun");
        robot.type(KeyCode.Q);
        assertEquals(player.getInventory().getSelectedItem().getName(), "Bottle");
    }

    private void testShooting(Model model) throws InterruptedException {
        assertFalse(model.getEntities().isEmpty());
        robot.press(KeyCode.LEFT);
        Thread.sleep(3000);
        robot.release(KeyCode.LEFT);
        assertTrue(model.getEntities().isEmpty());
    }

    private void testPickingUpItem(Player player) throws InterruptedException {
        robot.press(KeyCode.S);
        Thread.sleep(2000);
        robot.release(KeyCode.S);
        Thread.sleep(40);
        assertNull(player.getInventory().getSelectedItem());
        robot.type(KeyCode.F);
        assertEquals(player.getInventory().getSelectedItem().getName(), "Gun");
    }

    private void testDamage(Player player) throws InterruptedException {
        int prevDamage = player.getHealth();
        robot.press(KeyCode.A);
        Thread.sleep(1000);
        robot.release(KeyCode.A);
        Thread.sleep(100);
        robot.press(KeyCode.D);
        Thread.sleep(100);
        robot.release(KeyCode.D);
        Thread.sleep(100);
        assertEquals(player.getHealth() , prevDamage - Settings.CACTUS_DAMAGE);
    }

    private void testCollisions(Sprite playerSprite) throws InterruptedException {
        robot.press(KeyCode.D);
        Thread.sleep(800);
        robot.release(KeyCode.D);
        Thread.sleep(200);
        double firstX = playerSprite.getX();
        double firstY = playerSprite.getY();
        robot.press(KeyCode.D);
        Thread.sleep(800);
        robot.release(KeyCode.D);
        Thread.sleep(200);
        double lastX = playerSprite.getX();
        double lastY = playerSprite.getY();
        assertEquals(firstX, lastX);
        assertEquals(firstY, lastY);
    }


    private void testPlayerMovement(KeyCode keyCode, boolean increase, boolean x, Sprite playerSprite) throws InterruptedException {
        double prevX = playerSprite.getX();
        double prevY = playerSprite.getY();
        robot.press(keyCode);
        Thread.sleep(500);
        robot.release(keyCode);
        Thread.sleep(100);
        double arg1, arg2, equals1, equals2;
        if (x) {
            arg1 = playerSprite.getX();
            arg2 = prevX;
            equals1 = playerSprite.getY();
            equals2 = prevY;
        } else {
            arg1 = playerSprite.getY();
            arg2 = prevY;
            equals1 = playerSprite.getX();
            equals2 = prevX;
        }
        if (increase) {
            assertGreaterThan(arg1, arg2);
        } else {
            assertLesserThan(arg1, arg2);
        }
        assertEquals(equals1, equals2);
    }

    private void testPlayerSidewaysMovement(Sprite playerSprite) throws InterruptedException {
        double prevX = playerSprite.getX();
        double prevY = playerSprite.getY();
        robot.press(KeyCode.D);
        robot.press(KeyCode.W);
        Thread.sleep(500);
        robot.release(KeyCode.D);
        robot.release(KeyCode.W);
        Thread.sleep(200);
        assertGreaterThan(playerSprite.getX(), prevX);
        assertLesserThan(playerSprite.getY(), prevY);
    }

    private void assertLesserThan(double a, double b) {
        assertTrue(a < b, "(" + a + ") should be lesser than (" + b + ")");
    }

    private void assertGreaterThan(double a, double b) {
        assertTrue(a > b, "(" + a + ") should be greater than (" + b + ")");
    }


}