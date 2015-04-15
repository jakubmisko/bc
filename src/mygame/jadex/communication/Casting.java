/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.communication;

import com.jme3.math.Vector3f;

/**
 *
 * @author jakub
 */
public class Casting {
    public static boolean toBool(Object o){
        if(o instanceof Boolean){
            return ((Boolean)o).booleanValue();
        }
       throw new RuntimeException("Not a boolean value!");
    }
    
    public static int toInt(Object o){
        if(o instanceof Integer){
            return ((Integer)o).intValue();
        }
       throw new RuntimeException("Not a integer value!");
    }
    
    public static Vector3f toVector(Object o){
        if(o instanceof Vector3f){
            return (Vector3f)o;
        }
       throw new RuntimeException("Not a 3d vector!");
    }
}
