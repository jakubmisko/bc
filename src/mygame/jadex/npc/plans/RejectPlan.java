/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.IAgentProps;

/**
 *
 * @author jakub
 */
public class RejectPlan extends Plan{

    @Override
    public void body() {
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Follow, false);
        while(true){
        System.out.println("nejdem kym nezachranis dieta");
        waitFor(2000);
        }
    }
    
}
