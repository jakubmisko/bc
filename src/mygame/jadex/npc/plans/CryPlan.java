/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Communicator;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;


/**
 *
 * @author jakub
 */
public class CryPlan extends Plan {

    @Override
    public void body() {
        Engine e = (Engine) getBeliefbase().getBelief("emotional_engine");
        //e.g
        System.out.println("Q.Q zhoriiiim");
        
       // waitFor(300);
    }
}
