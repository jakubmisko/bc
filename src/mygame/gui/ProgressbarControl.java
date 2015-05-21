package mygame.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;

import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

public class ProgressbarControl implements ScreenController{

    private Element progressBarElement, progressBarElement2, progressBarElement3, progressBarElement4;
    private Nifty nifty;
    private Screen screen;

    public void bind(Nifty nifty, Screen screen) {
        //System.out.println("bind2");
        this.nifty = nifty;
        this.screen = screen;
        progressBarElement = nifty.getScreen("hud").findElementByName("progress1");
        progressBarElement2 = nifty.getScreen("hud").findElementByName("progress2");
        progressBarElement3 = nifty.getScreen("hud").findElementByName("progress3");
        progressBarElement4 = nifty.getScreen("hud").findElementByName("progress4");
    }

    public void onEndScreen() {
        System.out.println("bye");
    }

    public void onStartScreen() {
        setProgress(1, 1.0f);
        setProgress(2, 1.0f);
        setProgress(3, 1.0f);
        setProgress(4, 1.0f);
    }

    public void onFocus(boolean getFocus) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return false;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void setValue(Element progressBarElement, float progress){
        final int MIN_WIDTH = 32;
        int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);
        progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
        progressBarElement.getParent().layoutElements();
    }
    public void setProgress(int name, float progressValue) {
        float progress = progressValue;
        if (progress < 0.0f) {
            progress = 0.0f;
        } else if (progress > 1.0f) {
            progress = 1.0f;
        }
        switch(name){
            case 1: setValue(progressBarElement, progress);
                break;
            case 2: setValue(progressBarElement2, progress);
                break;
            case 3: setValue(progressBarElement3, progress);
                break;
            case 4: setValue(progressBarElement4, progress);
                break;
        }

        //String progressText = String.format("%3.0f%%", progress * 100);
        // progressTextElement.getRenderer(TextRenderer.class).setText(progressText);
    }

}
