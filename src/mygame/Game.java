package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
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
import java.util.ArrayList;
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

    private float hp = 100f;
    private ArrayList<Spatial> fire;
    private BoundingBox playerSpace;
    private Node cross, houseNode;
    private BulletAppState bulletAppState;
    boolean w, a, s, d;
    private float time = 0.7f;
    private Vector3f walkDirection;
    private AudioNode audioNode;
    boolean walking = false;
    boolean meeting = true;
    private Spatial scene;
    private Communicator com;
//    private AgentProps jaimeJadex;
//    private AgentProps joeyJadex;
    Character janko, jozko, player, benny, crow, rytier;
    private boolean mouse;

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
        //initJadex();

    }

    private void playerAction(float tpf) {
        
        cam.setLocation(player.getNode().getLocalTranslation().add(new Vector3f(0.0f, 2.5f, 3.0f)));
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
        if(mouse){
            if(!player.getAnimation().equals("UseHatchet"))
            player.setAnimation("UseHatchet");
        } else{
            player.setAnimation("Stand");
        }
        
        walkDirection.setY(0);
        player.getControl().setWalkDirection(walkDirection);
        player.getControl().setViewDirection(cam.getDirection());
        // uberanie zivota
        if (player.nearFire(3, fire)) {
            hp -= 0.05;
            System.out.println("hp=" + hp);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        playerAction(tpf);
        janko.walking(0.5f);
        if (jozko.nearFire(2, fire)) {
            jozko.runFromFire(speed);
            hp -= 0.8;
            System.out.println("hp jozko" + hp);
        } else {
            jozko.setBusy(false);
            jozko.walking();
        }
        //flyCam.setEnabled(true);
//        CollisionResults results = new CollisionResults();
//        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
//        houseNode.collideWith(ray, results);
//        if (results.size() != 0 && results.getClosestCollision().getDistance() < 3) {
//            System.out.println("kolizia");
//        } else {
//            System.out.println("vsetko dobre");


        //jakub
//        Vector3f jozef = jozko.getNode().getLocalTranslation();
//        //System.out.println("[info] my position: x=" + player.getNode().getLocalTranslation().getX() + " y=" + player.getNode().getLocalTranslation().getY() + " z=" + player.getNode().getLocalTranslation().getZ());
//        if (!jozko.isNear(player, 3) && Casting.toBool(com.getAgent("jozko").get(IAgentProps.NearPlayer))) {
//            com.getAgent("jozko").put(IAgentProps.NearPlayer, false);
//            com.getAgent("jozko").put(IAgentProps.Follow, false);
//        } else if (jozef.getZ() > 11 && !Casting.toBool(com.getAgent("jozko").get(IAgentProps.Saved))) {
//            com.getAgent("jozko").put(IAgentProps.Saved, true);
//        } else if (Casting.toInt(com.getAgent("jozko").get(IAgentProps.Health)) == 0 && jozko.isAlive()) {
//            com.getAgent("jozko").put(IAgentProps.Follow, false);
//            com.getAgent("jozko").put(IAgentProps.Walking, false);
//            jozko.setAlive(false);
//            System.out.println("mrtvy");
//        }
//        if (Casting.toBool(com.getAgent("jozko").get(IAgentProps.Walking))) {
//            walking(jozko);
//        } else if (Casting.toBool(com.getAgent("jozko").get(IAgentProps.Follow))) {
//            nasleduj(jozko);
//        } else {
//            stopWalking(jozko);
//        }
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



//        player.getNode().updateGeometricState();
//
//        CollisionResults results = new CollisionResults();
//        houseNode.collideWith(player.getNode().getWorldBound(), results);
//        if (results.size() != 0 && results.getClosestCollision().getDistance() < 2
//                && !results.getClosestCollision().getGeometry().toString().startsWith("ID227071")
//                && !results.getClosestCollision().getGeometry().toString().startsWith("ID226941")
//                && !results.getClosestCollision().getGeometry().toString().startsWith("ID226201")) {
//            System.out.println("kolizia s " + results.getClosestCollision().getGeometry().toString());
//
//            back = true;
//        } else {
//            System.out.println("vsetko dobre");
//        }
//        if (back) {
//            //cam.setLocation(old);
//            //flyCam.setEnabled(false);
//            cam.setLocation(old);
//            player.getControl().setWalkDirection(Vector3f.ZERO);
//        }

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
                a.setAnimation("Stand");
                b.setAnimation("Stand");
                c.setAnimation("Stand");
                a.getControl().setWalkDirection(Vector3f.ZERO);
                b.getControl().setWalkDirection(Vector3f.ZERO);
                walking = true;
                a.doSomething();
                b.doSomething();
            }
        } else if (a.getNode().getWorldBound().distanceTo(b.getNode().getLocalTranslation()) < 3) {
            //System.out.println("Konverzuje " + a.getMeno() + " a " + b.getMeno() + ".");
            a.setAnimation("Stand");
            b.setAnimation("Stand");
            a.getControl().setWalkDirection(Vector3f.ZERO);
            b.getControl().setWalkDirection(Vector3f.ZERO);
            walking = true;
            a.doSomething();
            b.doSomething();
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
        walkDirection = Vector3f.ZERO;
        player = new Character("Hrac");
        Node node = (Node) assetManager.loadModel("Models/player/Hero.mesh.j3o");
        node.setName("playa");

        player.makeNode("Player");
        player.setNode(node);
        player.makeControl(new Vector3f(0.3f, 5f, 70f), new Vector3f(0, -80, 0));
        player.makeAnimation("Stand");
        player.getControl().warp(new Vector3f(-10.0f, 0f, 0.0f));
        player.getControl().setViewDirection(walkDirection);
        getPhysicsSpace().add(player.getControl());
        player.getNode().setModelBound(new BoundingBox());
        player.getNode().updateModelBound();
        node.setLocalTranslation(32f, 0f, 0f);
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
        inputManager.addMapping("E", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("LMB",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("f1", new KeyTrigger(KeyInput.KEY_F1));
        inputManager.addMapping("f2", new KeyTrigger(KeyInput.KEY_F2));
        inputManager.addMapping("f3", new KeyTrigger(KeyInput.KEY_F3));
        inputManager.addMapping("f4", new KeyTrigger(KeyInput.KEY_F4));
        inputManager.addMapping("f5", new KeyTrigger(KeyInput.KEY_F5));
        inputManager.addMapping("f6", new KeyTrigger(KeyInput.KEY_F6));
        inputManager.addMapping("f7", new KeyTrigger(KeyInput.KEY_F7));
        inputManager.addMapping("f8", new KeyTrigger(KeyInput.KEY_F8));
        inputManager.addMapping("f9", new KeyTrigger(KeyInput.KEY_F9));
        inputManager.addMapping("f10", new KeyTrigger(KeyInput.KEY_F10));
        inputManager.addMapping("f11", new KeyTrigger(KeyInput.KEY_F11));
        inputManager.addMapping("f12", new KeyTrigger(KeyInput.KEY_F12));
        inputManager.addListener(this, new String[]{"W", "A", "S", "D", "E", "LMB", "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "f10", "f11", "f12"});
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
        if (name.equalsIgnoreCase("E")) {
            if (jozko.isNear(player, 4) && !Casting.toBool(com.getAgent("jozko").get(IAgentProps.NearPlayer))
                    && !Casting.toBool(com.getAgent("jozko").get(IAgentProps.Saved))
                    && Casting.toInt(com.getAgent("jozko").get(IAgentProps.Health)) != 0) {
                com.getAgent("jozko").put(IAgentProps.Walking, false);
                com.getAgent("jozko").put(IAgentProps.NearPlayer, true);
                System.out.println("[game] nasledujem");
            }
        }
        if (name.equalsIgnoreCase("LMB")) {
           mouse = isPressed;
        }
        // debug
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
            com.getAgent("jozko").put(IAgentProps.Health, Integer.valueOf(0));
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f5")) {
            com.getAgent("jozko").put(IAgentProps.Saved, true);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f6")) {
            com.getAgent("jozko").put(IAgentProps.ChildSafe, true);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f7")) {
            com.getAgent("jozko").put(IAgentProps.Follow, false);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f8")) {
            com.getAgent("jozko").put(IAgentProps.NearFire, false);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f9")) {
            com.getAgent("jozko").put(IAgentProps.NearPlayer, false);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f10")) {
            com.getAgent("jozko").put(IAgentProps.Health, Integer.valueOf(100));
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f11")) {
            com.getAgent("jozko").put(IAgentProps.Saved, false);
            System.out.println("fko stlacene");
        }
        if (name.equalsIgnoreCase("f12")) {
            com.getAgent("jozko").put(IAgentProps.ChildSafe, false);
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
        jadexJozko.put(IAgentProps.Walking, true);
        jadexJozko.put(IAgentProps.ChildSafe, false);
        com.addAgent(jadexJozko);
        jozko = new Character("Jozko");
        Node node = (Node) assetManager.loadModel("Models/rescuee1/Hero.mesh.j3o");
        node.setLocalTranslation(33.0f, 0f, -3.0f);
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
        node.setLocalTranslation(35f, 0f, 5f);
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
        fire = new ArrayList<Spatial>();
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
        for (Spatial s : rootNode.getChildren()) {
            if (s.getName() != null && s.getName().equals("Emitter")) {
                fire.add(s);
            }
        }
    }
    /*
     * Funkcia vloží všetky uzly budovy do zoznamu a následne vymaže tie,
     * ktoré predstavujú dvere. Ulzy v zozname predstavujú statické body scény, 
     * ktoré sú neprechodné (ale dá sa cez ne vidieť).
     */

    private void dontWalkCrossWalls() {
        ArrayList walls = new ArrayList<Node>();
        System.out.println("House ma deti: " + houseNode.getChildren().size());

        for (int i = 0; i < houseNode.getChildren().size(); i++) {
            walls.add(houseNode.getChild(i));
        }

        walls.remove(houseNode.getChild("SketchUp.011"));
        walls.remove(houseNode.getChild("SketchUp.012"));
        walls.remove(houseNode.getChild("SketchUp.013"));
        walls.remove(houseNode.getChild("SketchUp.014"));
        walls.remove(houseNode.getChild("SketchUp.015"));
        walls.remove(houseNode.getChild("SketchUp.016"));
        walls.remove(houseNode.getChild("SketchUp.017"));

        for (int i = 0; i < walls.size(); i++) {
            Node wallNode = (Node) walls.get(i);
            CollisionShape houseShape = CollisionShapeFactory.createMeshShape(wallNode);
            RigidBodyControl houseControl = new RigidBodyControl(houseShape, 0);
            houseNode.addControl(houseControl);
            getPhysicsSpace().add(houseControl);
            rootNode.attachChild(houseNode);
        }
    }

    public void initHouse() {
        houseNode = (Node) assetManager.loadModel("Models/dom/dom.j3o");
        houseNode.setName("House");
        houseNode.setLocalTranslation(31.0f, 0.05f, 0.0f);

        /**
         * A white, directional light source
         */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(0.5f, 0.5f, 0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        rootNode.attachChild(houseNode);

        dontWalkCrossWalls();
    }
}
