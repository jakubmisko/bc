/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import mygame.jadex.JadexStarter;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Communicator;
import mygame.jadex.communication.IAgentProps;

/**
 *
 * @author jakub
 */
public class Main {

    public static void main(String[] args) {
        Game g = new Game();
        g.turnOn();
//        
//        System.out.println("inicializujem..");
//        AgentProps p = new AgentProps("jozko");
//        
//        p.put("fire", Boolean.FALSE);
//        p.put("follow", Boolean.FALSE);
//        p.put(IAgentProps.Health, Integer.valueOf(100));
//        p.put("save", Boolean.FALSE);
//        p.put("nearP", Boolean.FALSE);
//        Communicator.INSTANCE.addAgent(p);
//        String[] str = {"mygame/jadex/npc/jozko.agent.xml"};
//        JadexStarter s = new JadexStarter(str);
//        s.run();
    }
}
