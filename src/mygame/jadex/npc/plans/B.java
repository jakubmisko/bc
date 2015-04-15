/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;

/**
 *
 * @author jakub
 */
public class B extends Plan {

    @Override
    public void body() {
        while(true){
            AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
            for(String s : p.keySet()){
                System.out.println(s+" = "+p.get(s));
            }
            waitFor(2000);
        }
    }
}
