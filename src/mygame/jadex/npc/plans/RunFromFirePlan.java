package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.IAgentProps;
import sk.tuke.fei.bdi.emotionalengine.component.Engine;
import sk.tuke.fei.bdi.emotionalengine.res.R;

/**
 *
 * @author jakub
 */
public class RunFromFirePlan extends Plan {
    /**
     * Plán spustený pri kontakte z ohňom, na základe emócii sa rozhodne či
     * osoba utečie alebo nie
     */
    @Override
    public void body() {
        //boolean approval = true;
        Engine e = (Engine) getBeliefbase().getBelief("emotional_engine").getFact();
        if(e.getElement("wanderplan", R.PLAN).getEmotion(R.DISAPPROVING).getIntensity() > 0.7){
            AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
            p.put(IAgentProps.GiveUp, true);
            fail();
        } else {
            System.out.println("malo");
        }
        AgentProps p = (AgentProps) getBeliefbase().getBelief("shared").getFact();
        p.put(IAgentProps.Walking, false);
        p.put(IAgentProps.Follow, false);
        /*if(!approval){
            IGoal giveUp = createGoal("give_up");
            dispatchSubgoalAndWait(giveUp);
        }*/
        while (true) {
            System.out.println("fuj ohen idem prec #"+getComponentName());
            waitFor(2000);

        }
    }
}
