/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import jadex.rules.rulesystem.IAgenda;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.IAgentProps;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 *
 * @author jakub
 */
public class CelebratePlan extends Plan {

    @Override
    public void body() {
//        Engine e = (Engine) getBeliefbase().getBelief("engine").getFact();
//        e.isElement(R., R.JOY);
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, false);
        p.put(IAgentProps.NearFire, false);
        while (true) {
            System.out.println("hura hura");
            waitFor(1000);
        }
    }
}
