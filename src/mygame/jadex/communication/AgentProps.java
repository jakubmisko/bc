/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.jadex.communication;

import jadex.commons.SimplePropertyChangeSupport;
import jadex.commons.beans.PropertyChangeListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jakub
 */
public class AgentProps extends ConcurrentHashMap<String, Object> {
    //private boolean updated;    
    private String name;
    private SimplePropertyChangeSupport pcs;

    public AgentProps(){
        this("");
    }
    public AgentProps(String name) {
        this.name = name;
        //updated = false;
        this.pcs = new SimplePropertyChangeSupport(this);
    }
    
    public void setName(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }


    @Override
    public Object put(String type, Object ob) {
        
        Object ret = super.put(type, ob);
        if (ret != null) {
            pcs.firePropertyChange("map", null, this); // hack
        }
        return ret;
    }

    /**
     *
     * @param type kluc k objektu
     * @return objekt triedy java.lang.Object, ktory treba pretypovat
     */
    public Object get(String type) {
        //Object ret = super.get(type);
       // pcs.firePropertyChange("HashMap", null, this); // hack
        return super.get(type);
    }

    /**
     * kontrola ci dana vlastnost agenta je vlozena
     *
     * @param type kluc k objektu
     * @return true ak kluc je namapovany inak false
     */
    public boolean contains(String type) {
        //return super.contains(type);
        return super.containsKey(type);
    }
    /*
     public boolean getBool(String type) throws ClassCastException{
     synchronized(shared){
     Object o = shared.get(type);
     if(o instanceof Boolean){
     return ((Boolean)o).booleanValue();
     }
     throw new ClassCastException();
     }
     }
    
     public Object getUpdate(String type){
     synchronized(shared){
     if(updated && shared.containsKey(type)){
     updated = false;
     return shared.get(type);
     }
     return null;
     }
     }
     * 

    public boolean needUpdate() {
        if (updated) {
            updated = false;
            return true;
        }
        return false;
    }
*/
    public Object remove(String type) {
        return super.remove(type);
    }
    // java beans event mechanism
    
    public Map getMap(){
        throw new UnsupportedOperationException("Dummy method. Do not use!");
    }
    
    public void setMap(){
        throw new UnsupportedOperationException("Dummy method. Do not use!");
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
