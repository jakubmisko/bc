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
public class RunFromFirePlan extends Plan {

    @Override
    public void body() {
        while (true) {
            System.out.println("fuj ohen idem prec");
            waitFor(2000);

        }
    }
}
