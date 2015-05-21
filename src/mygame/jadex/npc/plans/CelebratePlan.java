package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.IAgentProps;

/**
 *
 * @author jakub
 */
public class CelebratePlan extends Plan {
    /**
     * Plán, ktorý sa spustí vprípade úspešnej záchrany osôb z horiaceho domu
     */
    @Override
    public void body() {
//        Engine e = (Engine) getBeliefbase().getBelief("engine").getFact();
//        e.isElement(R., R.JOY);
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, false);
        p.put(IAgentProps.NearFire, false);
        while (true) {
            System.out.println("hura hura");
            waitFor(1000);
        }
    }
}
