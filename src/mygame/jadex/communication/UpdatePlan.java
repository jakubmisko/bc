/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.communication;

import jadex.bdi.runtime.IBeliefbase;
import jadex.bdi.runtime.Plan;

/**
 *
 * @author jakub
 */
public class UpdatePlan extends Plan {
    // private boolean fistTime = true;
    //private Communicator com = null;
    private AgentProps agent;

    @Override
    public void body() {
        IBeliefbase base  = getBeliefbase();
        System.out.println("[beliefs] aktualizujem...#"+getComponentName());
        agent = Communicator.INSTANCE.getAgent(getComponentName());
        String[] beliefs =  base.getBeliefNames();
        for(String name : beliefs){
            if(agent.contains(name)){
                base.getBelief(name).setFact(agent.get(name));
            }
        }
    }
    /*
    private void update(){
        
        getBeliefbase().getBelief(IAgentProps.Health).setFact(Casting.toInt(agent.get(IAgentProps.Health)));
        getBeliefbase().getBelief(IAgentProps.NearPlayer).setFact(Casting.toBool(agent.get(IAgentProps.NearPlayer)));
        getBeliefbase().getBelief(IAgentProps.Saved).setFact(Casting.toBool(agent.get(IAgentProps.Saved)));
        getBeliefbase().getBelief(IAgentProps.NearFire).setFact(Casting.toInt(agent.get(IAgentProps.NearFire)));
        getBeliefbase().getBelief(IAgentProps.Follow).setFact(Casting.toInt(agent.get(IAgentProps.Follow)));
       
        System.out.println("[help] beliefs updated");
    }
*/
}
