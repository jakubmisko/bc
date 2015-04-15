/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author student
 */
public class Fire {
    private ParticleEmitter fireEffect;
    
    public Fire(Material fireMat, float x, float y, float z){
        fireEffect = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 80);
        
    //    fireMat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        fireEffect.setMaterial(fireMat);
        fireEffect.setLocalTranslation(x, y, z);
        fireEffect.setImagesX(4); 
        fireEffect.setImagesY(4); // 2x2 texture animation
        fireEffect.setEndColor( new ColorRGBA(1f, 0f, 0f, 1f) );   // red
        fireEffect.setStartColor( new ColorRGBA(1f, 1f, 0f, 0.5f) ); // yellow
        fireEffect.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 1.5f, 0));
        fireEffect.setStartSize(0.9f);
        fireEffect.setEndSize(0.2f);
        
//        fireEffect.setGravity(0f,0f,0f);
//        fireEffect.setLowLife(0.2f);
//        fireEffect.setHighLife(2f);
        fireEffect.getParticleInfluencer().setVelocityVariation(0.3f);
    }
    
    public ParticleEmitter fireNode(){
        return fireEffect;
    }
}
