/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import com.jme3.math.Vector3f;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;
import java.util.Random;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class WanderPlan extends Plan{
    @Override
    public void body() {
//        IGoal moveHere = createGoal("move_here");
//        Vector3f here = new Vector3f(1f, 2f, 3f);
//        moveHere.getParameter("here").setValue(here);
//        moveHere.getParameter("currentlyFollowingPlayer").setValue(false);
//        int a = new Random().nextInt(3) + 3;
//        moveHere.getParameter("durationTime").setValue(Integer.valueOf(a));
//        System.out.println("chodim chodim chodim");
//        waitFor(2000);
//        System.out.println("[wander] dispatched");
//        dispatchSubgoalAndWait(moveHere);
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, true);
        while(true){
            waitFor(500);
            System.out.println("chodim");
        }
        
    }
    
}
