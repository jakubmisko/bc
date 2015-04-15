package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;
import java.util.Random;
import mygame.jadex.JadexStarter;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.communication.Casting;
import mygame.jadex.communication.Communicator;
import mygame.jadex.communication.IAgentProps;

/**
 * @author Jakub
 */
public class Game extends SimpleApplication implements ActionListener {

    private Node cross;
    private BulletAppState bulletAppState;
    boolean w, a, s, d;
    private float time = 0.7f;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private Vector3f zeroDirection = new Vector3f(0, 0, 0);
    private AudioNode audioNode;
    boolean walking = false;
    boolean meeting = true;
    private Spatial scene;
    private Communicator com;
//    private AgentProps jaimeJadex;
//    private AgentProps joeyJadex;
    Character janko, jozko, player, benny, crow, rytier;

    public void turnOn() {
        this.start();
    }

    private void initCross() {
        cross = new Node("cross");
        //cross
        Geometry geomv = new Geometry("crossv", new Box(0.25f, 2, 1));
        Geometry geomh = new Geometry("crossh", new Box(2, 0.25f, 1));
        cross.attachChild(geomv);
        cross.attachChild(geomh);
        Material mat3 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", ColorRGBA.Green);
        geomv.setMaterial(mat3);
        geomv.scale(5f);
        geomv.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0);
        geomh.setMaterial(mat3);
        geomh.scale(5f);
        geomh.setLocalTranslation(settings.getWidth() / 2, settings.getHeight() / 2, 0);
        /*
         inputManager.addMapping("Shoot",
         new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
         inputManager.addListener(shoot, "Shoot");*/
        guiNode.attachChild(cross);
    }

    public void initJadex() {

        String[] agents = {"mygame/jadex/npc/jozko.agent.xml"};
        //agents[0] = "mygame/jadex/meeting/jaime.agent.xml";
        //agents[1] = "mygame/jadex/meeting/joey.agent.xml";
        JadexStarter start = new JadexStarter(agents);
        Thread t = new Thread(start);
        t.start();
    }

    @Override
    public void simpleInitApp() {
        com = Communicator.INSTANCE;
        initState();
        initLight();
        flyCam.setMoveSpeed(50f);
        initScene();
        initPlayer();
        initInput();
        initAudio();

        initRescuee1();
        initRescuee2();
        initWater();
        initDog();
        initBird();
        initFire();
        initHouse();
        initCross();
        initJadex();
    }

    private void stopWalking(Character a) {
        a.getControl().setWalkDirection(Vector3f.ZERO);
        setAnimation(a.getAnimacia(), "Stand");
    }

    @Override
    public void simpleUpdate(float tpf) {
//        if(jozko.isKraca() || player.isKraca())
//            collision(jozko, player, null);
//        if(janko.isKraca() || player.isKraca())
//            collision(janko, player, null);  

//        walkInWorld(jozko, janko);  
        if (Casting.toBool(com.getAgent("jozko").get(IAgentProps.Follow))) {
            walking(jozko);
        } else {
             stopWalking(jozko);
        }
        //walking(janko);
        //walking(benny);
        //walking(crow);
        //System.out.println("X: " + player.getNode().getLocalTranslation().getX());
        //System.out.println("Z: " + player.getNode().getLocalTranslation().getZ());


//         System.out.println("jozko "+(Vector3f) jaimeJadex.get(IAgentProps.MoveTo)+" janko "+(Vector3f)joeyJadex.get(IAgentProps.MoveTo));
//        if ((Boolean) joeyJadex.get(IAgentProps.Leave) && (Boolean) jaimeJadex.get(IAgentProps.Leave) && 
//                joeyJadex.get(IAgentProps.MoveTo) != null && jaimeJadex.get(IAgentProps.MoveTo) != null) {
//            presun(jozko.getNode(), jozko.getControl(), (Vector3f) jaimeJadex.get(IAgentProps.MoveTo));
//            System.out.println("jozko ide tu "+jaimeJadex.get(IAgentProps.MoveTo));
//            presun(janko.getMeno(), janko.getControl(), (Vector3f)joeyJadex.get(IAgentProps.MoveTo));
//            System.out.println("janko ide tu "+joeyJadex.get(IAgentProps.MoveTo));
//            setAnimation(jozko.getAnimacia(), "Walk");
//            setAnimation(janko.getAnimacia(), "Walk");
//        }

        cam.setLocation(player.getNode().getLocalTranslation().add(new Vector3f(0.0f, 1.8f, 0.0f)));
        walkDirection.set(0, 0, 0);
        time += tpf;

        if (a) {
            walkDirection.addLocal(cam.getLeft().mult(5f));
            if (time >= 0.7f) {
                audioNode.play();
                time = 0;
            }
        }
        if (s) {
            walkDirection.addLocal(cam.getDirection().negate().mult(5f));
            if (time >= 0.7f) {
                audioNode.play();
                time = 0;
            }
        }
        if (d) {
            walkDirection.addLocal(cam.getLeft().negate().mult(5f));
            if (time >= 0.7f) {
                audioNode.play();
                time = 0;
            }
        }
        if (w) {
            walkDirection.addLocal(cam.getDirection().mult(5f));
            if (time >= 0.7f) {
                audioNode.play();
                time = 0;
            }
        }

        walkDirection.setY(0);
        player.getControl().setWalkDirection(walkDirection);
    }

    /**
     * Hlavná funkcia zabezpečujúca kráčanie postáv "a" a "b" po svete. Funkcia
     * volá funkciu pre kolíziu postáv a funkciu zabezpečujúcu nespadnutie
     * postáv z herného sveta. V prípade, že sa postavy "a" a "b" k sebe
     * približia na menej ako 9 jednotiek, je ich smer nastavený tak, aby prišli
     * k sebe a začali konvezáciu. Po konverzácií postavy môžu popri sebe
     * prechádzať, no ďalšiu konverzáciu začnú až ked sa ich vzdialenosť zväčší
     * na 10 jednotiek a následne zmenší pod 9 jednotiek.
     *
     * @param a postava
     * @param b postava
     */
    private void walkInWorld(Character a, Character b) {
        Node aNode = a.getNode();
        Node bNode = b.getNode();
        BetterCharacterControl aControl = a.getControl();
        BetterCharacterControl bControl = b.getControl();

        if (a.isKraca() && b.isKraca()) {
            if (walking) {
                walking(a);
                walking(b);
                meeting = false;
            }
            if (aNode.getWorldBound().distanceTo(bNode.getLocalTranslation()) > 10) {
                walking = false;
                meeting = true;
            }
            if (meeting) {
                //System.out.println(a.getMeno() + ": " + bNode.getWorldBound().distanceTo(aNode.getLocalTranslation()));
                //System.out.println(b.getMeno() + ": " + aNode.getWorldBound().distanceTo(bNode.getLocalTranslation()));

                if (aNode.getWorldBound().distanceTo(bNode.getLocalTranslation()) > 9) {
                    walking(a);
                    walking(b);
                } else {
                    bControl.setViewDirection(aNode.getLocalTranslation().subtract(bNode.getLocalTranslation()));
                    bControl.setWalkDirection(aNode.getLocalTranslation().subtract(bNode.getLocalTranslation()));
                    aControl.setViewDirection(bNode.getLocalTranslation().subtract(aNode.getLocalTranslation()));
                    aControl.setWalkDirection(bNode.getLocalTranslation().subtract(aNode.getLocalTranslation()));

                    collision(a, b, player);
                    collision(a, b, null);
                }
            }
        }

        if (!aControl.getWalkDirection().equals(zeroDirection)) {
            stayInWorld(aNode, aControl);
        }
        if (!bControl.getWalkDirection().equals(zeroDirection)) {
            stayInWorld(bNode, bControl);
        }
    }

    /**
     * Funkcia kontrolujúca kolíziu jednotlivých postáv a,b,c. Môžu nastať dva
     * prípady, že sú zadané všetke tri postavy alebo len dva postavy, ktorých
     * vzájomnú kolíziu je potebné kontrolovať.
     *
     * @param a Postava, ktorej je kontrolovaná kolízia.
     * @param b Postava, s ktorou je kontrolovaná kolízia.
     * @param c Postava, s ktorou je kontrolovaná kolízia.
     */
    private void collision(Character a, Character b, Character c) {
        if (c != null) {
            if (a.getNode().getWorldBound().distanceTo(b.getNode().getLocalTranslation()) < 3 && a.getNode().getWorldBound().distanceTo(c.getNode().getLocalTranslation()) < 2) {
                //System.out.println("Konverzuje " + a.getMeno() + " a " + b.getMeno() + " a " + c.getMeno() + ".");
                setAnimation(a.getAnimacia(), "Stand");
                setAnimation(b.getAnimacia(), "Stand");
                setAnimation(c.getAnimacia(), "Stand");
                a.getControl().setWalkDirection(zeroDirection);
                b.getControl().setWalkDirection(zeroDirection);
                walking = true;
                a.doSomething();
                b.doSomething();
            }
        } else if (a.getNode().getWorldBound().distanceTo(b.getNode().getLocalTranslation()) < 3) {
            //System.out.println("Konverzuje " + a.getMeno() + " a " + b.getMeno() + ".");
            setAnimation(a.getAnimacia(), "Stand");
            setAnimation(b.getAnimacia(), "Stand");
            a.getControl().setWalkDirection(zeroDirection);
            b.getControl().setWalkDirection(zeroDirection);
            walking = true;
            a.doSomething();
            b.doSomething();
        }
    }

    /**
     * Funkcia walking zabezpečuje náhodne kráčanie postavy po svete. Animácie
     * sa menia iba v tom prípade, že sa zmenil stav postavy zo stojacej na
     * kráčajúcu, respektíve naopak. Posledný príkaz znamená, že ak sa postava
     * zastaví, nenastaví sa jej smerovanie na 0,0,0 (dopredu), ale zostane v
     * tom smere, v ktorom sa pohybovala.
     *
     * @param control Control postavy,ktorá má kráčať.
     * @param anim Premenná pre zmenu animácie postavy pri zastávení a pohnutí
     * sa
     */
    private void walking(Character a) {
        int rozsah = 500;
        Random generate = new Random();
        float k = generate.nextInt(rozsah);
        float l = generate.nextInt(rozsah);
        float x = a.getControl().getWalkDirection().getX();
        float y = a.getControl().getWalkDirection().getY();
        float z = a.getControl().getWalkDirection().getZ();
        float pomx = x;
        float pomz = z;

        if (k == 10) {
            x = 1;
        } else if (k == 20) {
            x = 0;
        } else if (k == 30) {
            x = -1;
        }
        if (l == 15) {
            z = 1;
        } else if (l == 25) {
            z = 0;
        } else if (l == 35) {
            z = -1;
        }
        if ((pomx == 1 || pomz == 1 || pomx == -1 || pomz == -1) && (x == 0 && z == 0) && (a.getMeno().equals("Jozko") || a.getMeno().equals("Janko"))) {
            setAnimation(a.getAnimacia(), "Stand");
        } else if ((pomx == 0 && pomz == 0) && (x == 1 || z == 1 || x == -1 || z == -1) && (a.getMeno().equals("Jozko") || a.getMeno().equals("Janko"))) {
            setAnimation(a.getAnimacia(), "Walk");
        } else if ((pomx == 0 && pomz == 0) && (x == 1 || z == 1 || x == -1 || z == -1) && a.getMeno().equals("Benny")) {
            setAnimation(a.getAnimacia(), "run");
        } else if ((pomx == 1 || pomz == 1 || pomx == -1 || pomz == -1) && (x == 0 && z == 0) && a.getMeno().equals("Benny")) {
            setAnimation(a.getAnimacia(), "idle");
        }

        Vector3f position = new Vector3f(x, y, z);
        a.getControl().setWalkDirection(position);
        if (!position.equals(new Vector3f(0, y, 0))) {
            a.getControl().setViewDirection(position);
        }
    }

    /**
     * Metóda, ktorá zabezpečuje, aby postava nespadla zo sveta. Ak postava
     * dôjde na kraj, otočí sa o 180° a pokračuje novým smerom.
     *
     * @param node Uzol postavy, ktorej pozícia je kontrolovaná.
     * @param control Ovládanie postavy, ktorej má byť zmenený smer pohybu.
     */
    private void stayInWorld(Node node, BetterCharacterControl control) {
        if (node.getLocalTranslation().getX() > 20) {
            control.setWalkDirection(new Vector3f(-1, 0, 0));
            control.setViewDirection(new Vector3f(-1, 0, 0));
        } else if (node.getLocalTranslation().getX() < -20) {
            control.setWalkDirection(new Vector3f(1, 0, 0));
            control.setViewDirection(new Vector3f(1, 0, 0));
        } else if (node.getLocalTranslation().getZ() < -20) {
            control.setWalkDirection(new Vector3f(0, 0, 1));
            control.setViewDirection(new Vector3f(0, 0, 1));
        } else if (node.getLocalTranslation().getZ() > 20) {
            control.setWalkDirection(new Vector3f(0, 0, -1));
            control.setViewDirection(new Vector3f(0, 0, -1));
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    /*
     * Zabezpečí inicializáciu scény = mapu sveta.
     */
    public void initScene() {
//        scene = assetManager.loadModel("Scenes/Island.j3o");
        scene = assetManager.loadModel("Scenes/town/main.j3o");
        RigidBodyControl terrainControl = new RigidBodyControl(0f);
        scene.addControl(terrainControl);
        getPhysicsSpace().add(terrainControl);
        scene.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(scene);
    }

    /*
     * Zabzepečí inicializáciu svetla (slnka) vo vytvorenom svete.
     */
    public void initLight() {
        /**
         * A white, directional light source.
         */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        /**
         * A white ambient light source.
         */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);

    }
    /*
     * Zabezepčí inicializáciu stavu.
     */

    public void initState() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(false);
    }

    /*
     * Pridávanie audio súborov do prostredia.
     */
    private void initAudio() {
        /* Neprehráva mp3jky */
        audioNode = new AudioNode(assetManager, "Sounds/footsteps-6.wav", false);
        audioNode.setPositional(false);
        audioNode.setLooping(false);
        rootNode.attachChild(audioNode);

        AudioNode natureAudio = new AudioNode(assetManager, "Sounds/Nature.ogg", false);
        natureAudio.setPositional(false);
        natureAudio.setLooping(true);
        rootNode.attachChild(natureAudio);
        natureAudio.play();
    }

    /*
     * Zabezpečí inicializáciu hráča.
     */
    public void initPlayer() {
        player = new Character("Hrac");
        Node node = (Node) assetManager.loadModel("Models/player/Hero.mesh.j3o");
        player.makeNode("Player");
        player.setNode(node);
        player.makeControl(new Vector3f(0.3f, 5f, 70f), new Vector3f(0, -80, 0));
        player.makeAnimation("Stand");
        player.getControl().warp(new Vector3f(-10.0f, 0f, 0.0f));
        player.getControl().setViewDirection(walkDirection);
        getPhysicsSpace().add(player.getControl());
        rootNode.attachChild(player.getNode());
    }

    /*
     * Zabezpečuje čítanie znakov z klávesnice.
     */
    public void initInput() {
        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("f1", new KeyTrigger(KeyInput.KEY_F1));
        inputManager.addMapping("f2", new KeyTrigger(KeyInput.KEY_F2));
        inputManager.addMapping("f3", new KeyTrigger(KeyInput.KEY_F3));
        inputManager.addMapping("f4", new KeyTrigger(KeyInput.KEY_F4));
        inputManager.addMapping("f5", new KeyTrigger(KeyInput.KEY_F5));
        inputManager.addListener(this, new String[]{"W", "A", "S", "D", "f1", "f2", "f3", "f4", "f5"});
    }
    /*
     * Priradi konkrétnemu znaku hodnotu pravda, v prípade stlačenia klávesy.
     */

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equalsIgnoreCase("W")) {
            w = isPressed;
        }
        if (name.equalsIgnoreCase("A")) {
            a = isPressed;
        }
        if (name.equalsIgnoreCase("S")) {
            s = isPressed;
        }
        if (name.equalsIgnoreCase("D")) {
            d = isPressed;
        }
        if (name.equalsIgnoreCase("f1")) {
            com.getAgent("jozko").put(IAgentProps.Follow, true);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f2")) {
            com.getAgent("jozko").put(IAgentProps.NearFire, true);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f3")) {
            com.getAgent("jozko").put(IAgentProps.NearPlayer, true);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f4")) {
            com.getAgent("jozko").put(IAgentProps.Health, Integer.valueOf(-1));
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f5")) {
            com.getAgent("jozko").put(IAgentProps.Saved, true);
            System.out.println("fko stlacene");
        }
    }

    /*
     * Funkcia na vytvorenie postavy.
     */
    public void initRescuee1() {
        //jadex
        AgentProps jadexJozko = new AgentProps("jozko");
        jadexJozko.put(IAgentProps.Health, Integer.valueOf(100));
        jadexJozko.put(IAgentProps.Follow, false);
        jadexJozko.put(IAgentProps.NearFire, false);
        jadexJozko.put(IAgentProps.Saved, false);
        jadexJozko.put(IAgentProps.NearPlayer, false);
        com.addAgent(jadexJozko);
        jozko = new Character("Jozko");
        Node node = (Node) assetManager.loadModel("Models/rescuee1/Hero.mesh.j3o");
        node.setLocalTranslation(-20.0f, 0f, -20.0f);
        jozko.makeNode("jozko");
        jozko.setNode(node);
        jozko.makeControl(new Vector3f(0.8f, 4f, 80f), new Vector3f(0.0f, -30f, 0.0f));
        jozko.makeAnimation("Stand");
        getPhysicsSpace().add(jozko.getControl());
        rootNode.attachChild(jozko.getNode());
    }

    /*
     * Funkcia na vytvorenie postavy.
     */
    public void initRescuee2() {
        janko = new Character("Janko");
        Node node = (Node) assetManager.loadModel("Models/rescuee2/Hero.mesh.j3o");
        node.setLocalTranslation(-10.0f, 0f, -20.0f);
        janko.makeNode("janko");
        janko.setNode(node);
        janko.makeControl(new Vector3f(0.5f, 4f, 80f), new Vector3f(0.0f, -30f, 0.0f));
        janko.makeAnimation("Stand");
        getPhysicsSpace().add(janko.getControl());
        rootNode.attachChild(janko.getNode());
    }

    private void initDog() {
        benny = new Character("Benny");
        Node node = (Node) assetManager.loadModel("Models/prostredie/pes/benny.j3o");
        node.setLocalTranslation(-10.0f, 0.0f, -15.0f);
        benny.makeNode("Benny");
        node.scale(0.6f, 0.6f, 0.6f);
//        node.setLocalTranslation(17.0f, 0.0f, -1.0f);
        benny.setNode(node);
        benny.makeControl(new Vector3f(0.6f, 4f, 80f), new Vector3f(0.0f, -30f, 0.0f));
        benny.makeAnimation("idle", "Cube");
        benny.getControl().setViewDirection(new Vector3f(-1, 0.0f, 0));
        getPhysicsSpace().add(benny.getControl());
        rootNode.attachChild(benny.getNode());
    }

    private void initBird() {
        crow = new Character("Crow");
        Node node = (Node) assetManager.loadModel("Models/prostredie/vták/crow.j3o");
        crow.makeNode("Crow");
        node.scale(0.2f, 0.2f, 0.2f);
        node.setLocalTranslation(0.0f, 3.0f, 0.0f);
        crow.setNode(node);
        crow.makeControl(new Vector3f(0.56f, 4f, 80f), new Vector3f(0.0f, 0f, 0.0f));
        crow.makeAnimation("Fly", "Crow");
        crow.getControl().setViewDirection(new Vector3f(-1, 0.0f, -1.0f));
//        getPhysicsSpace().add(crow.getControl());
        rootNode.attachChild(crow.getNode());
    }

    /**
     * Nastavenie animácie postavám.
     *
     * @param anim Postava, ktorej má byť animácia pridelená.
     * @param type Typ animácie, ktorý má byť priradený.
     */
    private void setAnimation(AnimChannel anim, String type) {
        anim.setAnim(type);
    }

    /**
     * Funkcia vytvorí vodnú plochu o veľkosti quad. Polohu vody udáva
     * water.setLocalTranslation- daný bod tvorí ľavý dolný bod vodného
     * štvoruholníka.
     */
    public void initWater() {
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(scene);
        waterProcessor.setWaterColor(ColorRGBA.Brown);

        Quad quad = new Quad(300, 300);
        quad.scaleTextureCoordinates(new Vector2f(3f, 3f));

        Geometry water = new Geometry("water", quad);
        water.setShadowMode(RenderQueue.ShadowMode.Receive);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setMaterial(waterProcessor.getMaterial());
        water.setLocalTranslation(-100, -0.1f, 100f);

        rootNode.attachChild(water);
        viewPort.addProcessor(waterProcessor);
        rootNode.attachChild(scene);
    }

    private void initFire() {
        Material fireMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        Fire fire1 = new Fire(fireMat, 50, 0, -2);
        Fire fire2 = new Fire(fireMat, 50, 0, -10);
        Fire fire3 = new Fire(fireMat, 50, 0, -18);
        Fire fire4 = new Fire(fireMat, 32, 0, -17);
        Fire fire5 = new Fire(fireMat, 32, 0, -9);
        Fire fire6 = new Fire(fireMat, 32, 0, -1);
        Fire fire7 = new Fire(fireMat, 33, 0, 0);
        Fire fire8 = new Fire(fireMat, 37, 0, 0);
        Fire fire9 = new Fire(fireMat, 40, 0, 0);
        Fire fire10 = new Fire(fireMat, 48, 0, 0);
        Fire fire11 = new Fire(fireMat, 42, 5, -10);
        rootNode.attachChild(fire1.fireNode());
        rootNode.attachChild(fire2.fireNode());
        rootNode.attachChild(fire3.fireNode());
        rootNode.attachChild(fire4.fireNode());
        rootNode.attachChild(fire5.fireNode());
        rootNode.attachChild(fire6.fireNode());
        rootNode.attachChild(fire7.fireNode());
        rootNode.attachChild(fire8.fireNode());
        rootNode.attachChild(fire9.fireNode());
        rootNode.attachChild(fire10.fireNode());
        rootNode.attachChild(fire11.fireNode());
    }

    public void initHouse() {
//        Node houseNode = (Node)assetManager.loadModel("Scenes/town/dom.j3o"); 
        Node houseNode = (Node) assetManager.loadModel("Models/newHouseDAE/newHouse.j3o");
        houseNode.setName("House");
        houseNode.scale(2.2f, 2, 2.2f);
        houseNode.setLocalTranslation(32.0f, 0.01f, 0.0f);

        /**
         * A white, directional light source
         */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(0.5f, 0.5f, 0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        rootNode.attachChild(houseNode);
    }
}
