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

    /**
     * plán, ktorý aktualizuje <beliefs> v adf agenta na základe kolekcie
     * zdieľaných objektov
     */
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
}
