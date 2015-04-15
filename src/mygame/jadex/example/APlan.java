/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.example;

import jadex.bdi.runtime.Plan;

public class APlan extends Plan{

	@Override
	public void body() {
		for(int i = 0; i < 10; i++)
			System.out.println("Agent: a");
	}

}

