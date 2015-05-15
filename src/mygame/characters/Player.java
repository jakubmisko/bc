/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.characters;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.List;
import mygame.items.Fire;

/**
 *
 * @author jakub
 */
public class Player extends Character {

    private List<Fire> fire;
    private boolean a;
    private boolean s;
    private boolean d;
    private boolean w;
    private Vector3f walkDirection;
    private Camera cam;
    private Node gui;

    public Player(String meno) {
        super(meno);
        walkDirection = Vector3f.ZERO;
    }

    public Node getGui() {
        return gui;
    }

    public void setGui(Node gui) {
        this.gui = gui;
    }

    public boolean isA() {
        return a;
    }

    public void setA(boolean a) {
        this.a = a;
    }

    public boolean isS() {
        return s;
    }

    public void setS(boolean s) {
        this.s = s;
    }

    public boolean isD() {
        return d;
    }

    public void setD(boolean d) {
        this.d = d;
    }

    public boolean isW() {
        return w;
    }

    public void setW(boolean w) {
        this.w = w;
    }

    public Camera getCam() {
        return cam;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
    }

    public List<Fire> getFire() {
        return fire;
    }

    public void setFire(List<Fire> fire) {
        this.fire = fire;
    }

    @Override
    public void action() {

        cam.setLocation(getNode().getLocalTranslation().add(new Vector3f(0.0f, 2.5f, 0f)));
        walkDirection.set(0, 0, 0);
        //time += tpf;
        if (a) {
            walkDirection.addLocal(cam.getLeft().mult(5f));
//            if (time >= 0.7f) {
//                audioNode.play();
//                time = 0;
//            }
        }
        if (s) {
            walkDirection.addLocal(cam.getDirection().negate().mult(5f));
//            if (time >= 0.7f) {
//                audioNode.play();
//                time = 0;
//            }
        }
        if (d) {
            walkDirection.addLocal(cam.getLeft().negate().mult(5f));
//            if (time >= 0.7f) {
//                audioNode.play();
//                time = 0;
//            }
        }
        if (w) {
            walkDirection.addLocal(cam.getDirection().mult(5f));
//            if (time >= 0.7f) {
//                audioNode.play();
//                time = 0;
//            }
        }
//        if (mouse) {
//            if (!player.getAnimation().equals("UseHatchet")) {
//                player.setAnimation("UseHatchet");
//            }
//        } else {
//
//            player.setAnimation("Stand");
//        }

        walkDirection.setY(0);
        getControl().setWalkDirection(walkDirection);
        getControl().setViewDirection(cam.getDirection());
        // uberanie zivota
        if (nearFire(3, fire)) {
            decreaseHp(0.05f);

            //System.out.println("hp=" + hp);
        }

    }
}
