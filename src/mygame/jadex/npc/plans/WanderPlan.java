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
import mygame.jadex.communication.Casting;
import mygame.jadex.communication.IAgentProps;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 *
 * @author jakub
 */
public class WanderPlan extends Plan {

    @Override
    public void body() {
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, true);
        Engine e = (Engine) getBeliefbase().getBelief("emotional_engine").getFact();
        while (true) {
            if (e.getElement("run_from_fire", R.GOAL).getEmotion(R.FEAR).getIntensity() > 0.25
                    && !Casting.toBool(p.get(IAgentProps.Cry))) {
                p.put(IAgentProps.Walking, false);
                IGoal cry = createGoal("cry");
                dispatchTopLevelGoal(cry);
                fail();
                //waitFor(5000);
            } else {
                System.out.println("chodim #" + getComponentName());
                waitFor(2000);
            }

        }

    }
}
