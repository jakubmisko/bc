/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.characters;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.FlyByCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.Fire;

/**
 *
 * @author Jakub
 */
public abstract class Character {

    private float hp;
    private Node node;
    private BetterCharacterControl control;
    private AnimChannel animacia;
    private String meno;
    private boolean saved;
    private boolean busy;
    private boolean follow;
    
    
    public Character(String meno) {
        this.meno = meno;
        saved = false;
        busy = false;
        hp = 100;
    }

    public boolean isFollowing() {
        return follow;
    }

    public void setFollowing(boolean follow) {
        this.follow = follow;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public void decreaseHp(float value) {
        hp -= value;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void makeNode(String meno) {
        node = new Node(meno);

    }

    public void makeControl(Vector3f parametre, Vector3f gravitacia) {
        setControl(new BetterCharacterControl(parametre.x, parametre.y, parametre.z));
        getControl().setGravity(gravitacia);
        node.addControl(getControl());
    }

    public void makeAnimation(String anim) {
        AnimControl animControl = node.getControl(AnimControl.class);
        setAnimChanel(animControl.createChannel());
        getAnimChanel().setAnim(anim);
    }

    public void makeAnimation(String anim, String child) {
        Node no = (Node) node.getChild(child);
        AnimControl animControl = no.getControl(AnimControl.class);
        setAnimChanel(animControl.createChannel());
        getAnimChanel().setAnim(anim);
    }

//    public void doSomething() {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                setKraca(false);
//                System.out.println(meno + " som v kolízií a preto by som chcel niečo robiť."
//                        + "Budem sa zhovárať 5 sekúnd.");
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Character.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                setKraca(true);
//            }
//        };
//        thread.start();
//    }
    /**
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * @return the control
     */
    public BetterCharacterControl getControl() {
        return control;
    }

    /**
     * @param control the control to set
     */
    public void setControl(BetterCharacterControl control) {
        this.control = control;
    }

    /**
     *
     * @return meno aktualnej animacie
     */
    public String getAnimation() {
        return animacia.getAnimationName();
    }

    /**
     * nastavenie novej animacie
     *
     * @param name nova animacia
     */
    public void setAnimation(String name) {
        animacia.setAnim(name);
    }

  

    public void setAnimation(String name, float blend) {
        animacia.setAnim(name, blend);
    }

    /**
     * @return the animacia
     */
    public AnimChannel getAnimChanel() {
        return animacia;
    }

    /**
     * @param animacia the animacia to set
     */
    public void setAnimChanel(AnimChannel animacia) {
        this.animacia = animacia;
    }

    /**
     * @return the meno
     */
    public String getMeno() {
        return meno;
    }

    /**
     * @param meno the meno to set
     */
    public void setMeno(String meno) {
        this.meno = meno;
    }

    /**
     * @return the saved status
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * @param saved the saved status to set
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    private boolean compare(Vector3f x, Vector3f y, float dist) {
//        return (Math.abs(x.getX() - y.getX()) < dist
//                && Math.abs(x.getY() - y.getY()) < dist
//                && Math.abs(x.getZ() - y.getZ()) < dist);
        return x.distance(y) < dist;
    }

    public boolean isNear(Character who, float distance) {
        Vector3f follower = node.getLocalTranslation();
        Vector3f following = who.getNode().getLocalTranslation();

        return compare(follower, following, distance);
    }

    public boolean nearFire(float distance, List<Fire> fire) {
        Vector3f me = node.getLocalTranslation();
        for (Fire f : fire) {
            Vector3f fireLoc = f.fireNode().getLocalTranslation();
            if (compare(fireLoc, me, distance)) {
                return true;
            }
        }
        return false;
    }

    public void follow(Character player) {
        follow(player, 1);
    }

    /**
     * Postava a bude nasledovať hráča.
     *
     * @param player postava, ktorá má nasledovať
     * @param rychlost rýchlosť pohybu postavy, čím vyššie číslo tým pomalšia
     * rýchlosť
     */
    public void follow(Character player, float rychlost) {
        if (isNear(player, 2)) {
            control.setWalkDirection(new Vector3f(0f, 0f, 0f));
            setAnimation("Stand");
        } else {
            if (animacia.getAnimationName().equalsIgnoreCase("Stand")) {
                setAnimation("Walk");
            }
            Vector3f pozicia = player.getNode().getLocalTranslation().subtract(node.getLocalTranslation());
            control.setWalkDirection(new Vector3f(pozicia.getX() / rychlost, pozicia.getY() / rychlost, pozicia.getZ() / rychlost));
            control.setViewDirection(pozicia);
        }
    }

    /**
     * pohyb standardoun rychlostou
     */
    public void walking() {
        walking(1);
    }

    /**
     * nahodny pohyb hraca
     *
     * @param rychlost rychlost - 1 standardna, < 1 pomalsia, > 1 rychlejsia
     */
    public void walking(float rychlost) {
        animacia.setSpeed(rychlost);
        int rozsah = 500;
        Random generate = new Random();
        float k = generate.nextInt(rozsah);
        float l = generate.nextInt(rozsah);
        float x = control.getWalkDirection().getX();
        float y = control.getWalkDirection().getY();
        float z = control.getWalkDirection().getZ();
        float pomx = x;
        float pomz = z;

        if (k == 10) {
            x = rychlost;
        } else if (k == 20) {
            x = 0;
        } else if (k == 30) {
            x = -rychlost;
        }
        if (l == 15) {
            z = rychlost;
        } else if (l == 25) {
            z = 0;
        } else if (l == 35) {
            z = -rychlost;
        }
        if ((pomx == rychlost || pomz == rychlost || pomx == -rychlost || pomz == -rychlost) && (x == 0 && z == 0)) {
            animacia.setAnim("Stand");
        } else if ((pomx == 0 && pomz == 0) && (x == rychlost || z == rychlost || x == -rychlost || z == -rychlost)) {
            animacia.setAnim("Walk");
        }

        Vector3f position = new Vector3f(x, y, z);
        control.setWalkDirection(position);
        if (!position.equals(new Vector3f(0, y, 0))) {
            control.setViewDirection(position);
        }
    }

    public void runFromFire(float speed) {
        if (!busy) {
            animacia.setAnim("Fear1");
            Vector3f dir = control.getWalkDirection();
            dir.addLocal(dir.negate().mult(2f));
            if (dir.equals(Vector3f.ZERO)) {
                dir = new Vector3f(1f, 0f, 0f);
            }
            control.setWalkDirection(dir);
            control.setViewDirection(dir);
            busy = true;
        }
    }

    /**
     * zastavi pohyb postavy
     */
    public void stopWalking() {
        control.setWalkDirection(new Vector3f(0f, 0f, 0f));
        animacia.setAnim("Stand");
    }

    /**
     * Metóda, ktorá zabezpečuje, aby postava nespadla zo sveta. Ak postava
     * dôjde na kraj, otočí sa o 180° a pokračuje novým smerom.
     */
    public void stayInWorld() {
        if (node.getLocalTranslation().getX() > 20) {
            control.setWalkDirection(new Vector3f(-1, 0, 0));
            control.setViewDirection(new Vector3f(-1, 0, 0));
        } else if (node.getLocalTranslation().getX() < -20) {
            control.setWalkDirection(new Vector3f(1, 0, 0));
            control.setViewDirection(new Vector3f(1, 0, 0));
        } else if (node.getLocalTranslation().getZ() < -20) {
            control.setWalkDirection(new Vector3f(0, 0, 1));
            control.setViewDirection(new Vector3f(0, 0, 1));
        } else if (node.getLocalTranslation().getZ() > 20) {
            control.setWalkDirection(new Vector3f(0, 0, -1));
            control.setViewDirection(new Vector3f(0, 0, -1));
        }
    }
    public abstract void action();
}