/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.characters;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import mygame.Fire;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Casting;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class Human extends Character {

    private List<Fire> fire;
    private AgentProps jadex;
    private Vector3f dir = Vector3f.UNIT_X;
    private boolean b = true;
    private List<Character> agents;

    public Human(String meno, AgentProps jadex) {
        super(meno);
        this.jadex = jadex;
        agents = new ArrayList<Character>();
    }

    public void addAgent(Character c) {
        if (c == null) {
            System.out.println("null");
        } else {
            agents.add(c);
        }
    }

    public void removeCharacter(Character c) {
        agents.remove(c);
    }

    private boolean nearAnybody(float distance) {
        for (Character c : agents) {
            if (isNear(c, distance)) {
                return true;
            }
        }
        return false;
    }

    public List<Fire> getFire() {
        return fire;
    }

    public void setFire(List<Fire> fire) {
        this.fire = fire;
    }

    @Override
    public void action() {
        if (Casting.toBool(jadex.get(IAgentProps.Walking))) {
            if (getNode().getLocalTranslation().getX() > 50 && b) {
                dir.addLocal(dir.negate().mult(2f));
                b = false;
            } else if (getNode().getLocalTranslation().getX() < 20 && !b) {
                dir.addLocal(dir.negate().mult(2f));
                b = true;
            }
            if (!getAnimation().equals("walk")) {
                setAnimation("walk");
            }
            getControl().setWalkDirection(dir);
            getControl().setViewDirection(dir);
        } else {
            getControl().setWalkDirection(new Vector3f(0, 0, 0));
            if (getAnimation().equals("walk")) {
                setAnimation("stand");
                jadex.put(IAgentProps.Walking, false);
            }
        }
        if (nearAnybody(10) && !Casting.toBool(jadex.get(IAgentProps.NearAgent))) {
            jadex.put(IAgentProps.Walking, false);
            jadex.put(IAgentProps.NearAgent, true);
            setAnimation("applause");
        } else if (nearFire(13, fire) && !Casting.toBool(jadex.get(IAgentProps.NearFire))) {
            jadex.put(IAgentProps.Walking, false);
            jadex.put(IAgentProps.NearFire, true);
            //setAnimation("Fear1");
        }
        if (nearAnybody(10) && Casting.toBool(jadex.get(IAgentProps.NearAgent))) {
            jadex.put(IAgentProps.NearAgent, false);
        } else if (!nearFire(13, fire) && Casting.toBool(jadex.get(IAgentProps.NearFire))) {
            jadex.put(IAgentProps.NearFire, false);
        }
//        if (!getAnimation().equals("Stand") && !Casting.toBool(jadex.get(IAgentProps.Walking))
//                && !Casting.toBool(jadex.get(IAgentProps.NearFire))
//                && !Casting.toBool(jadex.get(IAgentProps.NearAgent))) {
//            getControl().setWalkDirection(new Vector3f(0, 0, 0));
//            setAnimation("Stand");
//        }
    }
}
