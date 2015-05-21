/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.Casting;
import mygame.jadex.help.IAgentProps;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 *
 * @author jakub
 */
public class WanderPlan extends Plan {
    /**
     * Plán náhodného pohybu, podľa emócii sa nastaví rýchlosť pohybu a pri 
     * vysokej hodnote strachu človek upadne do šoku čím sa vytvorí cieľ Cry
     */
    @Override
    public void body() {
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, true);
        Engine e = (Engine) getBeliefbase().getBelief("emotional_engine").getFact();
        while (true) {
            if (!getComponentName().equals("janko") && e.getElement("run_from_fire", R.GOAL).getEmotion(R.FEAR).getIntensity() > 0.25
                    && !Casting.toBool(p.get(IAgentProps.Cry))) {
                p.put(IAgentProps.Walking, false);
                IGoal cry = createGoal("cry");
                dispatchTopLevelGoal(cry);
                fail();
                //waitFor(5000);
            } else {
                System.out.println("chodim #" + getComponentName());
                float speed = (Float) getBeliefbase().getBelief("speed").getFact();
                if(e.getElement("wander", R.GOAL).getEmotion(R.FEAR).getIntensity() < 0.25
                        && speed != 1.0f){
                    p.put(IAgentProps.Speed, new Float(1.0f));
                } else if(e.getElement("wander", R.GOAL).getEmotion(R.FEAR).getIntensity() > 0.25
                        && speed != 1.2f){
                    p.put(IAgentProps.Speed, new Float(1.2f));
                }
                waitFor(2000);
            }

        }

    }
}
