/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.Casting;
import mygame.jadex.communication.Communicator;
import mygame.jadex.help.IAgentProps;

/**
 *
 * @author jakub
 */
public class ObservePlan extends Plan {

    @Override
    public void body() {
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, false);
        p.put(IAgentProps.NearAgent, false);
        while (true) {
            waitFor(1000);
            System.out.println("pozeram");
            if(Casting.toBool(Communicator.INSTANCE.getAgent("jozko").get(IAgentProps.Saved))){
                p.put(IAgentProps.NearAgent, true);
                
            }
        }
    }
}
