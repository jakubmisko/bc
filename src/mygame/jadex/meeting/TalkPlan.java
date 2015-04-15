/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.meeting;

import com.jme3.math.Vector3f;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Communicator;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class TalkPlan extends Plan {

    private Communicator com = Communicator.INSTANCE;
    private AgentProps agent;

    @Override
    public void body() {
        waitFor(3000);
        String name = (String) getBeliefbase().getBelief("name").getFact();
        agent = com.getAgent(name);
        
        
        IGoal leave = createGoal("leave");
        System.out.println(name+ " zachvilu pojdeme");
        //getBeliefbase().getBelief("spoke").setFact(true);
        dispatchTopLevelGoal(leave);
    }
}
