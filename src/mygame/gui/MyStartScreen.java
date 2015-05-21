package mygame.gui;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 */
public class MyStartScreen extends AbstractAppState implements ScreenController {

    public static boolean bool = false;
    private Nifty nifty;
    private Application app;
    private Screen screen;

    /**
     * custom methods
     */
    public MyStartScreen() {
        /**
         * You custom constructor, can accept arguments
         */
    }

    public void startGame() {
        bool = true;
        nifty.fromXml("Interface/hud.xml", "hud");
    }

    public void quitGame() {
        app.stop();
//    System.exit(1);
    }

    public String getPlayerName() {
        return System.getProperty("user.name");
    }

    /**
     * Nifty GUI ScreenControl methods
     */
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }

    /**
     * jME3 AppState methods
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;
    }

    @Override
    public void update(float tpf) {
        if (screen.getScreenId().equals("hud")) {
            System.out.println("bla");
        }
    }
}
