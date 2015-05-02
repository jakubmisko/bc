/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Communicator;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class SavedPlan extends Plan {

    @Override
    public void body() {
        System.out.println("hura som von #"+getComponentName());
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        if (getComponentName().endsWith("_jr")) {
            String[] others = (String[]) getPlanbase().getPlans("initialize_emotional_engine_plan")[0].
                    getParameterSet("emotional_others").getValues();
            for (String name : others) {
                Communicator.INSTANCE.getAgent(name).put(IAgentProps.ChildSafe, true);
            }
        }
        p.put(IAgentProps.Follow, false);
        p.put(IAgentProps.Walking, false);
        System.out.println("stojim");
        //while (true) {
            System.out.println("tesim sa");
            waitFor(2000);
        //}
    }
}
