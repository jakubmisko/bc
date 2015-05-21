package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Communicator;
import mygame.jadex.help.IAgentProps;

/**
 *
 * @author jakub
 */
public class SavedPlan extends Plan {
    /**
     * Plán vytvorený pri vyslobodení z horiacého domu
     */
    @Override
    public void body() {
        System.out.println("hura som von #"+getComponentName());
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        if (getComponentName().endsWith("_jr")) {
                Communicator.INSTANCE.getAgent("jozko").put(IAgentProps.ChildSafe, true);
                Communicator.INSTANCE.getAgent("anka").put(IAgentProps.ChildSafe, true);
        }
        p.put(IAgentProps.Follow, false);
        p.put(IAgentProps.Walking, false);
        //while (true) {
            System.out.println("tesim sa");
            waitFor(2000);
        //}
    }
}
