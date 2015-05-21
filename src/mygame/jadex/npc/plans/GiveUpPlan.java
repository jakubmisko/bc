package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.IAgentProps;

/**
 *
 * @author jakub
 */
public class GiveUpPlan extends Plan {
    /**
     * Plán spustený pri vysokej hodnote emócie Disapproval 
     */
    @Override
    public void body() {
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
       // p.put(IAgentProps.GiveUp, true);
        p.put(IAgentProps.Walking, false);
        while (true) {
            System.out.println("vzdavam sa #"+getComponentName());
            waitFor(2000);
        }
    }
}
