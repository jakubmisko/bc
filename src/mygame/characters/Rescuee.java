/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.characters;

import com.jme3.font.BitmapText;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.List;
import mygame.Fire;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Casting;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class Rescuee extends Character {

    private Character player;
    private Node gui;
    private List<Fire> fire;
    private AgentProps jadex;

    public Rescuee(String meno, AgentProps jadex) {
        super(meno);
        this.jadex = jadex;
    }

    public List<Fire> getFire() {
        return fire;
    }

    public void setFire(List<Fire> fire) {
        this.fire = fire;
    }

    public Node getGui() {
        return gui;
    }

    public void setGui(Node gui) {
        this.gui = gui;
    }

    public Character getPlayer() {
        return player;
    }

    public void setPlayer(Character player) {
        this.player = player;
    }

    @Override
    public void action() {
//        System.out.println("walking =" + jadex.get(IAgentProps.Walking));
//        System.out.println("fire="+jadex.get(IAgentProps.NearFire));
        if (getNode().getLocalTranslation().getZ() < 6 && getHp() > 0) {

            if (nearFire(2, fire)) {
                runFromFire(1);

                decreaseHp(2f);
                ((BitmapText) gui.getChild("player")).setText(String.valueOf(getHp()) + "%");
                if (!Casting.toBool(jadex.get(IAgentProps.NearFire))
                        && Casting.toBool(jadex.get(IAgentProps.Walking))) {
                    jadex.put(IAgentProps.NearFire, true);
                    jadex.put(IAgentProps.Walking, false);

                }
                System.out.println("blizko ohna " + getHp());
                if (Casting.toBool(jadex.get(IAgentProps.Follow))) {
                    jadex.put(IAgentProps.Follow, false);
                }
                //System.out.println("hp jozko" + getHp());
            } else if (!nearFire(3, fire)) {
                setBusy(false);
                if (!Casting.toBool(jadex.get(IAgentProps.Walking))
                        && !getAnimation().equals("Walk")
                        && !Casting.toBool(jadex.get(IAgentProps.Cry))
                        && !Casting.toBool(jadex.get(IAgentProps.Follow))) {
                    jadex.put(IAgentProps.Walking, true);
                    jadex.put(IAgentProps.NearFire, false);
                    setAnimation("Walk");

                }
                if (!isNear(player, 3) && Casting.toBool(jadex.get(IAgentProps.NearPlayer))) {
                    jadex.put(IAgentProps.NearPlayer, false);
                    jadex.put(IAgentProps.Walking, true);
                    jadex.put(IAgentProps.Follow, false);
                    setFollowing(false);
                }
                if (Casting.toBool(jadex.get(IAgentProps.Walking))) {
                    walking();
                } else if (!Casting.toBool(jadex.get(IAgentProps.Walking))
                        && !Casting.toBool(jadex.get(IAgentProps.Follow))) {
                    stopWalking();
                    if (!getAnimation().equals("Fear2")) {
                        setAnimation("Fear2");
                    }
                } else if (Casting.toBool(jadex.get(IAgentProps.Follow))) {
                    System.out.println("[game] follow");
                    follow(player);
                }
            }
        } else {
            if (!isSaved() && getHp() > 0) {
                setSaved(true);
                getControl().setWalkDirection(new Vector3f(0, 0, 0));
                jadex.put(IAgentProps.Follow, false);
                jadex.put(IAgentProps.NearFire, false);
                jadex.put(IAgentProps.NearPlayer, false);
                jadex.put(IAgentProps.NearAgent, false);
                jadex.put(IAgentProps.Walking, false);
                jadex.put(IAgentProps.Saved, true);
                setAnimation("Happy2");
            } else if (getHp() < 0) {
                getControl().setWalkDirection(new Vector3f(0, 0, 0));
                jadex.put(IAgentProps.Follow, false);
                jadex.put(IAgentProps.NearFire, false);
                jadex.put(IAgentProps.NearPlayer, false);
                jadex.put(IAgentProps.NearAgent, false);
                jadex.put(IAgentProps.Walking, false);
                jadex.put(IAgentProps.Health, 0);
                System.out.println("[game] mrtvy");
            }
        }
    }
}
