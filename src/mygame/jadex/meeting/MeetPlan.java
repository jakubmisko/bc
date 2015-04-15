/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.meeting;

import com.jme3.math.Vector3f;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Communicator;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class MeetPlan extends Plan {

    private AgentProps agent;
    private Communicator com = Communicator.INSTANCE;


    @Override
    public void body() {
            String name = (String) getBeliefbase().getBelief("name").getFact();
            
            agent = new AgentProps(name);
            //Object startVect = jaimeNode.getLocalTranslation();
            //jaime.set(IAgentProps.StartPoint, startVect);
            Object destVect;
            destVect = Vector3f.ZERO;
            agent.put(IAgentProps.Collide, false);
            agent.put(IAgentProps.MoveTo, destVect);
            agent.put(IAgentProps.Freeze, false);
            agent.put(IAgentProps.Leave, false);
            com.addAgent(agent);
        while(true){
            if ((Boolean) agent.get(IAgentProps.Collide)) {
                getBeliefbase().getBelief("met").setFact(true);
                IGoal talk = createGoal("talk");
                dispatchTopLevelGoal(talk);
                break;
            }
        }
    }
}