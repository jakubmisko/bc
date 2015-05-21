package mygame.jadex.help;

import com.jme3.math.Vector3f;

/**
 * 
 * @author jakub
 */
public class Casting {
    /**
     * Pretypovanie objektu na hodnotu typu boolean, v prípade iného objektu 
     * ako je Boolean vyhodí výnimku
     * @param o Objekt na pretypovanie
     * @return true alebo false
     */
    public static boolean toBool(Object o){
        if(o instanceof Boolean){
            return ((Boolean)o).booleanValue();
        }
       throw new RuntimeException("Not a boolean value!");
    }
    /**
     * Pretypovanie objektu na hodnotu typu int, v prípade iného objektu 
     * ako je Integer vyhodí výnimku
     * @param o Objekt na pretypovanie
     * @return celociselna hodnota
     */
    public static int toInt(Object o){
        if(o instanceof Integer){
            return ((Integer)o).intValue();
        }
       throw new RuntimeException("Not a integer value!");
    }
    /**
     * Pretypovanie objektu na hodnotu typu float, v prípade iného objektu 
     * ako je Float vyhodí výnimku
     * @param o Objekt na pretypovanie
     * @return hodnota čísla s desatinnou časťou
     */
    public static float toFloat(Object o){
        if(o instanceof Float){
            return ((Float)o).floatValue();
        }
       throw new RuntimeException("Not a float value!");
    }
    /**
     * Pretypovanie objektu na typ Vector3f, v prípade iného objektu 
     * ako je Vector3f vyhodí výnimku
     * @param o Objekt na pretypovanie
     * @return Objekt trojrozmerneho vektora Vector3f
     */
    public static Vector3f toVector(Object o){
        if(o instanceof Vector3f){
            return (Vector3f)o;
        }
       throw new RuntimeException("Not a 3d vector!");
    }
}
