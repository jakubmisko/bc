/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.IAgentProps;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;

/**
 *
 * @author jakub
 */
public class CryPlan extends Plan {

    @Override
    public void body() {
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();

        p.put(IAgentProps.Walking, false);
        p.put(IAgentProps.Cry, true);
        //e.g

        long timeout = (long) (10000 * Math.random());
        System.out.println("Q.Q #" + getComponentName() + " -t " + timeout);
        waitFor(timeout);
        p.put(IAgentProps.Cry, false);
        p.put(IAgentProps.Walking, true);
        System.out.println("uz neplacem");
        IGoal wander = createGoal("wander");
        dispatchTopLevelGoal(wander);
        // waitFor(300);
    }
}
