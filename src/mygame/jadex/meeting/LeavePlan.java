/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.meeting;

import com.jme3.math.Vector3f;
import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Communicator;
import mygame.jadex.help.IAgentProps;

/**
 *
 * @author jakub
 */
public class LeavePlan extends Plan {

    private Communicator com = Communicator.INSTANCE;
    private boolean setUp = true;
    private AgentProps jaime, joey;

    @Override
    public void body() {
        String name = (String) getBeliefbase().getBelief("name").getFact();
        System.out.println("leave : "+name+" ideme prec");
        Object destJaime = new Vector3f(-24f, 0, -24f);
        Object destJoey = new Vector3f(24f, 0, 24f);
        
        if (name.equals("jaime")) {
            jaime = com.getAgent("jaime");
            jaime.put(IAgentProps.Leave, true);
            jaime.put(IAgentProps.Collide, false);
            jaime.put(IAgentProps.MoveTo, destJaime);
            jaime.put(IAgentProps.Freeze, false);
            waitFor(2);
            jaime.put(IAgentProps.Leave, false);
            
        } else {
            joey = com.getAgent("joey");
            joey.put(IAgentProps.Leave, true);
            joey.put(IAgentProps.Collide, false);
            joey.put(IAgentProps.Freeze, false);
            joey.put(IAgentProps.MoveTo, destJoey);
            waitFor(2);
            joey.put(IAgentProps.Leave, false);
        }
    }
}
