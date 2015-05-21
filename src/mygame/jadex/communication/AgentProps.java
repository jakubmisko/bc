package mygame.jadex.communication;

import jadex.commons.SimplePropertyChangeSupport;
import jadex.commons.beans.PropertyChangeListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Trieda so zdieľanými vlastnosťami agenta
 * @author jakub
 */
public class AgentProps extends ConcurrentHashMap<String, Object> {
    //private boolean updated;    
    private String name;
    private SimplePropertyChangeSupport pcs;

    public AgentProps(){
        this("");
    }   
    /**
     * Vytvorí agenta a priradí mu meno 
     * @param name meno nového agenta
     */
    public AgentProps(String name) {
        this.name = name;
        this.pcs = new SimplePropertyChangeSupport(this);
    }
    /**
     * nastavenie nového mena
     * @param name meno
     */
    public void setName(String name){
        this.name = name;
    }
    /**
     * vráti meno agenta
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Uloží objekt pod daným kľúčom do HashMapy
     * @param type kľúč k vlastnosti agenta 
     * @param ob hodnota vlastnosti
     * @return vráti predchádzajúcu hodnotu uloženú pod daným kľúčom, ak sa nenachádza
     * tak null
     */
    @Override
    public Object put(String type, Object ob) {
        
        Object ret = super.put(type, ob);
        if (ret != null) {
            pcs.firePropertyChange("map", null, this); // hack
        }
        return ret;
    }

    /**
     *  Získanie objektu na základe kľúča
     * @param type kluc k objektu
     * @return objekt triedy java.lang.Object, ktory treba pretypovat
     */
    @Override
    public Object get(Object type) {
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
    /**
     * Odstáni objekt uložený v kolekcii
     * @param type kľúč k objektu
     * @return vráti hodnotu, kotorá bola na daný kľúč namapovaná
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
