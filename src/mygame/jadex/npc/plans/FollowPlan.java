/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Casting;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class FollowPlan extends Plan {

    @Override
    public void body() {
        // getBeliefbase().getBelief("follow").setFact(true);;
        waitFor(2000);

        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, false);
        //p.put(IAgentProps.Follow, true);
        if (!getComponentName().endsWith("_jr") && !Casting.toBool(p.get(IAgentProps.ChildSafe))) {
            IGoal reject = createGoal("reject_follow");
            dispatchSubgoalAndWait(reject);
        }
        p.put(IAgentProps.Follow, true);
        while (true) {

            System.out.println("nasledujem #" + getComponentName());
            waitFor(500);
        }
    }
}
