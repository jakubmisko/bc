/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.characters;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.List;
import mygame.items.Fire;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.Casting;
import mygame.jadex.help.IAgentProps;

/**
 *
 * @author jakub
 */
public class Rescuee extends Character {
    private boolean alive;
    private Character player;
    private Node gui;
    private List<Fire> fire;
    private AgentProps jadex;
    // constants
    private final String WALK_ANIM = "walk";
    private final String FIRE_ANIM = "nearFire";
    private final String STAND_ANIM = "stand";
    private final String FEAR_ANIM = "downWithFear";
    private final String HAPPYj_ANIM = "HuraJump";
    private final String HAPPY_ANIM = "happy";
    private final String REJECT_ANIM = "dontSaveMe";
    private final Vector3f ZERO = new Vector3f(0f, 0f, 0f);

    public Rescuee(String meno, AgentProps jadex) {
        super(meno);
        alive = true;
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

                if (!Casting.toBool(jadex.get(IAgentProps.NearFire))
                        && Casting.toBool(jadex.get(IAgentProps.Walking))) {
                    jadex.put(IAgentProps.NearFire, true);
                    jadex.put(IAgentProps.Walking, false);

                }
                //System.out.println("blizko ohna " + getHp());
                if (Casting.toBool(jadex.get(IAgentProps.Follow))) {
                    jadex.put(IAgentProps.Follow, false);
                }
                if (!Casting.toBool(jadex.get(IAgentProps.GiveUp))) {
                    runFromFire(Casting.toFloat(jadex.get(IAgentProps.Speed)));
                } else {
                    getControl().setWalkDirection(ZERO);
                    if (!getAnimation().equals(STAND_ANIM)) {
                        setAnimation(STAND_ANIM);
                    }
                }

                decreaseHp(2f);
                System.out.println("hp "+getName()+" "+getHp());
                //System.out.println("blizko ohna");
            } else if (!nearFire(3, fire)) {
                setBusy(false);
                if (Casting.toBool(jadex.get(IAgentProps.Walking))) {
                    walking(Casting.toFloat(jadex.get(IAgentProps.Speed)));
                    //System.out.println("[game] chodim");
                } else if (!Casting.toBool(jadex.get(IAgentProps.Walking))
                        && !Casting.toBool(jadex.get(IAgentProps.Follow))) {
                    //stopWalking();
                    getControl().setWalkDirection(ZERO);
                    getControl().setViewDirection(player.getControl().getViewDirection().negate());
                    if (!getAnimation().equals(FEAR_ANIM) && Casting.toBool(jadex.get(IAgentProps.Cry))) {
                        setAnimation(FEAR_ANIM);
                        // System.out.println("fear anim");
                    } else if (!getName().endsWith("Jr")
                            && !getAnimation().equals(REJECT_ANIM)
                            && Casting.toBool(jadex.get(IAgentProps.NearPlayer))
                            && !Casting.toBool(jadex.get(IAgentProps.ChildSafe))) {
                        setAnimation(REJECT_ANIM);
                    }

                } else if (Casting.toBool(jadex.get(IAgentProps.Follow))) {
                    //System.out.println("[game] follow");
                    follow(player, Casting.toFloat(jadex.get(IAgentProps.Speed)));
                }
                if (!Casting.toBool(jadex.get(IAgentProps.Walking))
                        && !Casting.toBool(jadex.get(IAgentProps.Cry))
                        && !Casting.toBool(jadex.get(IAgentProps.Follow))
                        && !Casting.toBool(jadex.get(IAgentProps.NearPlayer))) {
                    jadex.put(IAgentProps.Walking, true);
                    jadex.put(IAgentProps.NearFire, false);
                    if (!getAnimation().equals(WALK_ANIM)) {
                        setAnimation(WALK_ANIM);
                    }
                    //System.out.println("walk anim");

                }
                if (!isNear(player, 3) && Casting.toBool(jadex.get(IAgentProps.NearPlayer))) {
                    jadex.put(IAgentProps.NearPlayer, false);
                    jadex.put(IAgentProps.Walking, true);
                    jadex.put(IAgentProps.Follow, false);
                    setFollowing(false);
                }
                //System.out.println("daleko od ohna");
            } else {
                walking();
                setBusy(false);
                //System.out.println("v rozmedzi");
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
                if (getName().equals("JozkoJr")) {
                    setAnimation(HAPPYj_ANIM);
                } else {
                    setAnimation(HAPPY_ANIM);

                }
            } else if (getHp() <= 0 && alive) {
                getControl().setWalkDirection(new Vector3f(0, 0, 0));
                jadex.put(IAgentProps.Follow, false);
                jadex.put(IAgentProps.NearFire, false);
                jadex.put(IAgentProps.NearPlayer, false);
                jadex.put(IAgentProps.NearAgent, false);
                jadex.put(IAgentProps.Walking, false);
                jadex.put(IAgentProps.Health, 0);
                // System.out.println("[game] mrtvy");
                alive = false;
            }
        }
    }
}
