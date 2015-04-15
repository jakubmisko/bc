/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Casting;

/**
 *
 * @author jakub
 */
public class FollowPlan extends Plan {

    @Override
    public void body() {
        // getBeliefbase().getBelief("follow").setFact(true);
        System.out.println("idem zatebou zatebou");
        waitFor(2000);
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
         p.put("follow", true);
         while(Casting.toBool(p.get("follow"))){
             waitFor(1000);
         }
         System.out.println("[info] prestal som nasledovat");
         passed();
    }
}
