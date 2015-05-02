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
public class DeathPlan extends Plan{

    @Override
    public void body() {
        System.out.println("zomrel som :( #"+getComponentName());
        //waitFor(2000);
    }
    
}
