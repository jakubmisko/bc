/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.npc.plans;

import jadex.bdi.runtime.Plan;

/**
 *
 * @author jakub
 */
public class DeathPlan extends Plan{
    /**
     * Plán spustený pri klesnutí hodnoty života na 0
     */
    @Override
    public void body() {
        System.out.println("zomrel som :( #"+getComponentName());
        //waitFor(2000);
    }
    
}
