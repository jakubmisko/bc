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
public class SavedPlan extends Plan{

    @Override
    public void body() {
        System.out.println("hura som von");
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        
        p.put("follow", Boolean.FALSE);
        
        System.out.println("stojim");
        while(true){
            System.out.println(p.get("follow"));
            waitFor(2000);
        }
       // p.put("follow", Boolean.TRUE);
    }
    
}
