/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class RunFromFirePlan extends Plan {

    @Override
    public void body() {
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, false);
        p.put(IAgentProps.Follow, false);
        while (true) {
            System.out.println("fuj ohen idem prec #"+getComponentName());
            waitFor(2000);

        }
    }
}
