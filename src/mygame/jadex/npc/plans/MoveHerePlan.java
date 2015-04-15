/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;
import mygame.jadex.communication.Casting;

/**
 *
 * @author jakub
 */
public class MoveHerePlan extends Plan{

    @Override
    public void body() {
        int timeout = Casting.toInt(getParameter("durationTime").getValue());
        System.out.println("[move]pockam tolko :"+timeout);
        waitFor(timeout*1000);
    }
    
}
