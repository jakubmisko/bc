/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jakub
 */
public class Communicator {

    public static final Communicator INSTANCE = new Communicator();
    private Map<String, AgentProps> agents;

    private Communicator() {
        agents = new HashMap<String, AgentProps>();
    }


    public List<String> getAgents() {
        return new ArrayList<String>(agents.keySet());
    }

    public void addAgent(AgentProps newOne) {
            agents.put(newOne.getName(), newOne);
    }

    public AgentProps getAgent(String name) {
            return agents.get(name);
    }
}
