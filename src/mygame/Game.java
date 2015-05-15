package mygame;

import mygame.items.Fire;
import mygame.items.Water;
import mygame.items.Door;
import mygame.characters.Character;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
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
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.ui.Picture;
import com.jme3.water.SimpleWaterProcessor;
import java.util.ArrayList;
import java.util.Random;
import mygame.characters.Human;
import mygame.characters.Player;
import mygame.characters.Rescuee;
import mygame.jadex.JadexStarter;
import mygame.jadex.communication.AgentProps;
import mygame.jadex.help.Casting;
import mygame.jadex.communication.Communicator;
import mygame.jadex.help.IAgentProps;

/**
 * @author Jakub
 */
public class Game extends SimpleApplication implements ActionListener {

    // private Picture pic;
    private ArrayList<Spatial> walls;
    private ArrayList<Fire> fire;
    private ArrayList<Door> doorList;
    private Node cross, houseNode, equip;
    private BulletAppState bulletAppState;
    //private float time = 0.7f;
    private Vector3f walkDirection;
    private AudioNode audioNode;
    boolean walking = false;
    boolean meeting = true;
    private Spatial scene;
    private Communicator com;
    private AgentProps jadexJozko, jadexJanko, jadexJozkoJr, jadexAnka;
    private Rescuee jozkoJr, jozko, anka;
    private Human janko;
    private Player player;
    private JadexStarter start;
    private ArrayList<Spatial> furniture;

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

    private void initFurniture() {
        Quaternion qua = new Quaternion();
        furniture = new ArrayList<Spatial>();

        Node table = (Node) assetManager.loadModel("Models/furniture/table/Computer Table.j3o");
        table.setLocalTranslation(43.2f, 0.05f, -3.7f);
        table.scale(0.5f);
        qua.fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));
        table.setLocalRotation(qua);

        Node bed = (Node) assetManager.loadModel("Models/furniture/bed/Bed.j3o");
        bed.setLocalTranslation(48.2f, 0.7f, -14.0f);
        qua.fromAngleAxis(-FastMath.PI / 2, new Vector3f(0, 1, 0));
        bed.setLocalRotation(qua);

        Node coffe = (Node) assetManager.loadModel("Models/furniture/cafe/Cafe Equipment.j3o");
        coffe.setLocalTranslation(31.1f, 0.05f, -3.3f);
        coffe.scale(4.0f);

        Node lamp = (Node) assetManager.loadModel("Models/furniture/lamp/3dm-lamp2.j3o");
        lamp.setLocalTranslation(49.0f, 0.05f, -12.0f);

        Node sofa = (Node) assetManager.loadModel("Models/furniture/sofa/sofa.j3o");
        sofa.setLocalTranslation(31.2f, 0.05f, -12.0f);

        furniture.add(table);
        furniture.add(bed);
        furniture.add(coffe);
        furniture.add(lamp);
        furniture.add(sofa);

        rootNode.attachChild(table);
        rootNode.attachChild(bed);
        rootNode.attachChild(coffe);
        rootNode.attachChild(lamp);
        rootNode.attachChild(sofa);
    }

    public void initJadex() {

        String[] agents = {"mygame/jadex/npc/jozko.agent.xml", "mygame/jadex/npc/jozko_jr.agent.xml",
            "mygame/jadex/npc/janko.agent.xml", "mygame/jadex/npc/anka.agent.xml"};
        //agents[0] = "mygame/jadex/meeting/jaime.agent.xml";
        //agents[1] = "mygame/jadex/meeting/joey.agent.xml";
        start = new JadexStarter(agents);
        Thread t = new Thread(start);
        t.start();
    }

    /**
     * Funkcia nastaví animáciu dverí na otvorenú v prípade, ak sa priblíži
     * hráč.
     */
    private void openDoor() {
        Thread doorThread = new Thread() {
            public void run() {
                for (int i = 0; i < doorList.size(); i++) {
                    Door door = doorList.get(i);
                    //if(player.compare(player.getNode().getLocalTranslation(), door.getDoorNode().getLocalTranslation(), 2))
                    if (player.getNode().getLocalTranslation().distance(door.getDoorNode().getLocalTranslation()) < 2
                            || jozko.getNode().getLocalTranslation().distance(door.getDoorNode().getLocalTranslation()) < 2
                            || jozkoJr.getNode().getLocalTranslation().distance(door.getDoorNode().getLocalTranslation()) < 2
                            || anka.getNode().getLocalTranslation().distance(door.getDoorNode().getLocalTranslation()) < 2) {
                        if (door.getAnim().equals("Closed")) {
                            door.setAnim("Opening");
                        } else if (door.getAnim().equals("Opening")) {
                            try {
                                Thread.sleep(1700);
                            } catch (InterruptedException ex) {
                                //Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            door.setAnim("Open");
                        }
                    }
                }
            }
        };

        doorThread.start();

    }

    /**
     * Funkcia pridáva modely dverí k domu. Trieda Doormá tri argumenty: -
     * rotácia(v radianch(floatoch)), pozícia a škálovanie
     */
    private void addDoor() {
        doorList = new ArrayList<Door>();
        doorList.add(new Door(assetManager, 0, new Vector3f(38.1f, 0.1f, 0.0f), new Vector3f(1.3f, 2.12f, 1.0f)));
        doorList.add(new Door(assetManager, 0, new Vector3f(34.2f, 0.1f, -6.7f), new Vector3f(1.44f, 2.2f, 1.0f)));
        doorList.add(new Door(assetManager, FastMath.PI / 2, new Vector3f(39.2f, 0.1f, -11.45f), new Vector3f(1.3f, 2.3f, 1.0f)));
        doorList.add(new Door(assetManager, FastMath.PI / 2, new Vector3f(39.2f, 0.1f, -19.1f), new Vector3f(1.2f, 2.3f, 1.0f)));
        doorList.add(new Door(assetManager, -FastMath.PI / 2, new Vector3f(42.2f, 0.1f, -17.5f), new Vector3f(1.4f, 2.2f, 1.0f)));
        doorList.add(new Door(assetManager, -FastMath.PI / 2, new Vector3f(42.2f, 0.1f, -10.4f), new Vector3f(1.4f, 2.06f, 1.0f)));
        doorList.add(new Door(assetManager, FastMath.PI, new Vector3f(46.6f, 0.1f, -6.0f), new Vector3f(1.5f, 2.3f, 1.0f)));

        for (int i = 0; i < doorList.size(); i++) {
            Door door = doorList.get(i);
            rootNode.attachChild(door.getDoorNode());
        }
    }

    @Override
    public void simpleInitApp() {
        com = Communicator.INSTANCE;
        initState();
        initLight();
        //flyCam.setMoveSpeed(50f);
        initScene();
        initFurniture();
        initInput();
        initAudio();
        addDoor();
        initFire();
        initHouse();
        initCross();
        // postavicky predposledne
        initPlayer();
        initJozko();
        initJozkoJr();
        initJanko();
        initAnka();
        // jadex posledny
        // initJadex();
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.action();
        jozko.walking();
        /*
        for(Spatial wall : walls){

            System.out.println(wall.getLocalTranslation());
        }
        */
         jozko.action();
         jozkoJr.action();
         janko.action();
         anka.action();

        openDoor();
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
        player = new Player("Hrac");
        //equip = (Node) assetManager.loadModel("Models/axe/axe.j3o");

        player.makeNode("Player");
        player.makeControl(new Vector3f(0.3f, 5f, 70f), new Vector3f(0, -80, 0));
        //player.makeAnimation("Stand");
        player.getNode().setLocalTranslation(32f, 0f, 5f);
        player.getControl().warp(new Vector3f(32.0f, 0f, 5.0f));
        player.getControl().setViewDirection(walkDirection);
        getPhysicsSpace().add(player.getControl());
        player.getNode().setModelBound(new BoundingBox());
        player.getNode().updateModelBound();
        player.setCam(cam);
        player.setGui(guiNode);
        player.setFire(fire);
        rootNode.attachChild(player.getNode());
        //equip.setLocalTranslation(10, 10, 10);
        Quaternion roll180 = new Quaternion();
        roll180.fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));
//        pic = new Picture("axe");
//        pic.setImage(assetManager, "Models/FireAxe.png", true);
//        pic.setWidth(settings.getWidth() / 2);
//        pic.setHeight(settings.getHeight() / 2);
//        pic.setPosition(settings.getWidth() / 2, -10);
//        //pic.setLocalRotation(pic.getLocalRotation().multLocal(roll180));
//        guiNode.attachChild(pic);

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
        //mouse
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
            player.setW(isPressed);
        }
        if (name.equalsIgnoreCase("A")) {
            player.setA(isPressed);
        }
        if (name.equalsIgnoreCase("S")) {
            player.setS(isPressed);
        }
        if (name.equalsIgnoreCase("D")) {
            player.setD(isPressed);
        }
        if (name.equalsIgnoreCase("E")) {
            if (jozko.isNear(player, 4) && !Casting.toBool(jadexJozko.get(IAgentProps.NearPlayer))
                    && !Casting.toBool(jadexJozko.get(IAgentProps.Saved))
                    && Casting.toInt(jadexJozko.get(IAgentProps.Health)) != 0) {
                jozko.stopWalking();
                jadexJozko.put(IAgentProps.Walking, false);
                jadexJozko.put(IAgentProps.NearPlayer, true);
            } else if (jozkoJr.isNear(player, 4) && !Casting.toBool(jadexJozkoJr.get(IAgentProps.NearPlayer))
                    && !Casting.toBool(jadexJozkoJr.get(IAgentProps.Saved))
                    && Casting.toInt(jadexJozkoJr.get(IAgentProps.Health)) != 0) {
                jozkoJr.stopWalking();
                jadexJozkoJr.put(IAgentProps.Walking, false);
                jadexJozkoJr.put(IAgentProps.NearPlayer, true);
            } else if (anka.isNear(player, 4) && !Casting.toBool(jadexAnka.get(IAgentProps.NearPlayer))
                    && !Casting.toBool(jadexAnka.get(IAgentProps.Saved))
                    && Casting.toInt(jadexAnka.get(IAgentProps.Health)) != 0) {
                anka.stopWalking();
                jadexAnka.put(IAgentProps.Walking, false);
                jadexAnka.put(IAgentProps.NearPlayer, true);
            }
        }
        if (name.equalsIgnoreCase("LMB")) {
            //System.out.println(player.getNode().getLocalTranslation());
            extinguish(5);
        }
        // debug
        if (name.equalsIgnoreCase("f1")) {
            System.out.println("player : " + player.getNode().getLocalTranslation().toString());
        }
        if (name.equalsIgnoreCase("f2")) {
            jadexJozko.put(IAgentProps.NearFire, true);
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
    public void initAnka() {
        //jadex
        jadexAnka = new AgentProps("anka");
        jadexAnka.put(IAgentProps.Health, Integer.valueOf(100));
        jadexAnka.put(IAgentProps.Speed, Float.valueOf(1));
        jadexAnka.put(IAgentProps.Follow, false);
        jadexAnka.put(IAgentProps.NearFire, false);
        jadexAnka.put(IAgentProps.Saved, false);
        jadexAnka.put(IAgentProps.NearPlayer, false);
        jadexAnka.put(IAgentProps.Walking, true);
        jadexAnka.put(IAgentProps.ChildSafe, false);
        jadexAnka.put(IAgentProps.Cry, false);
        com.addAgent(jadexAnka);
        anka = new Rescuee("anka", jadexAnka);
        Node node = (Node) assetManager.loadModel("Models/mum/mother_Low-Poly.mesh.j3o");
        node.setLocalTranslation(34.0f, 0f, -19.0f);
        node.setLocalScale(0.15f);
        anka.setNode(node);
        anka.makeControl(new Vector3f(0.8f, 4f, 80f), new Vector3f(0.0f, -30f, 0.0f));
        anka.makeAnimation("stand");
        anka.setHp(100f);
        anka.setFire(fire);
        anka.setGui(guiNode);
        anka.setPlayer(player);
        getPhysicsSpace().add(anka.getControl());
        rootNode.attachChild(anka.getNode());
    }
    /*
     * Funkcia na vytvorenie postavy.
     */

    public void initJozko() {
        //jadex
        jadexJozko = new AgentProps("jozko");
        jadexJozko.put(IAgentProps.Health, Integer.valueOf(100));
        jadexJozko.put(IAgentProps.Speed, Float.valueOf(1));
        jadexJozko.put(IAgentProps.Follow, false);
        jadexJozko.put(IAgentProps.NearFire, false);
        jadexJozko.put(IAgentProps.Saved, false);
        jadexJozko.put(IAgentProps.NearPlayer, false);
        jadexJozko.put(IAgentProps.Walking, true);
        jadexJozko.put(IAgentProps.ChildSafe, false);
        jadexJozko.put(IAgentProps.Cry, false);
        com.addAgent(jadexJozko);
        jozko = new Rescuee("Jozko", jadexJozko);
        Node node = (Node) assetManager.loadModel("Models/dad/dad_Low-Poly.mesh.j3o");
        node.setLocalTranslation(34.0f, 0f, -12.0f);
        node.setLocalScale(0.15f);
        jozko.makeNode("jozko");
        jozko.setNode(node);
        jozko.makeControl(new Vector3f(0.8f, 4f, 80f), new Vector3f(0.0f, -30f, 0.0f));
        jozko.makeAnimation("stand");
        jozko.setHp(100f);
        jozko.setFire(fire);
        jozko.setGui(guiNode);
        jozko.setPlayer(player);
        jozko.setWalls(walls);
        getPhysicsSpace().add(jozko.getControl());
        rootNode.attachChild(jozko.getNode());
    }

    /*
     * Funkcia na vytvorenie postavy.
     */
    public void initJanko() {
        jadexJanko = new AgentProps("janko");
        jadexJanko.put(IAgentProps.Walking, true);
        jadexJanko.put(IAgentProps.NearAgent, false);
        jadexJanko.put(IAgentProps.NearFire, false);
        jadexJanko.put(IAgentProps.Speed, Float.valueOf(1));
        com.addAgent(jadexJanko);
        janko = new Human("Janko", jadexJanko);
        janko.addAgent(jozko);
        janko.addAgent(jozkoJr);
        janko.setFire(fire);
        Node node = (Node) assetManager.loadModel("Models/okoloiduci/okoloiduci.j3o");//(Node) assetManager.loadModel("Models/rescuee2/Hero.mesh.j3o");
        node.setLocalTranslation(21f, 0f, 12f);
        //janko.makeNode("janko");
        janko.setNode(node);
        janko.getNode().setLocalScale(0.15f);
        janko.makeControl(new Vector3f(0.5f, 4f, 80f), new Vector3f(0.0f, -30f, 0.0f));
        janko.makeAnimation("stand");
        getPhysicsSpace().add(janko.getControl());
        rootNode.attachChild(janko.getNode());
    }
    /*
     * Funkcia na vytvorenie postavy.
     */

    public void initJozkoJr() {
        //jadex
        jadexJozkoJr = new AgentProps("jozko_jr");
        jadexJozkoJr.put(IAgentProps.Health, Integer.valueOf(100));
        jadexJozkoJr.put(IAgentProps.Speed, Float.valueOf(1));
        jadexJozkoJr.put(IAgentProps.Follow, false);
        jadexJozkoJr.put(IAgentProps.NearFire, false);
        jadexJozkoJr.put(IAgentProps.Saved, false);
        jadexJozkoJr.put(IAgentProps.NearPlayer, false);
        jadexJozkoJr.put(IAgentProps.Walking, true);
        jadexJozkoJr.put(IAgentProps.ChildSafe, false);
        jadexJozkoJr.put(IAgentProps.Cry, false);
        com.addAgent(jadexJozkoJr);
        jozkoJr = new Rescuee("JozkoJr", jadexJozkoJr);
        jozkoJr.setFire(fire);
        jozkoJr.setGui(guiNode);
        jozkoJr.setPlayer(player);
        Node node = (Node) assetManager.loadModel("Models/son/chlapec_HighPolyEyes.mesh.j3o");
        node.setLocalTranslation(47.0f, 0f, -6.0f);
        node.setLocalScale(0.15f);
        //jozko.makeNode("jozko");
        jozkoJr.setNode(node);
        jozkoJr.makeControl(new Vector3f(0.8f, 4f, 80f), new Vector3f(0.0f, -30f, 0.0f));
        jozkoJr.makeAnimation("stand");
        jozkoJr.setHp(100f);
        getPhysicsSpace().add(jozkoJr.getControl());
        rootNode.attachChild(jozkoJr.getNode());
    }
    /*
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
     */

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
        Fire fire1 = new Fire(assetManager, 50, 0, -2);
        Fire fire2 = new Fire(assetManager, 50, 0, -10);
        Fire fire3 = new Fire(assetManager, 50, 0, -18);
        Fire fire4 = new Fire(assetManager, 31, 0, -18);
        //Fire fire5 = new Fire(assetManager, 32, 0, -9);
        Fire fire6 = new Fire(assetManager, 32, 0, -1);
        Fire fire7 = new Fire(assetManager, 33, 0, 0);
        Fire fire8 = new Fire(assetManager, 37, 0, 0);
        Fire fire9 = new Fire(assetManager, 40, 0, 0);
        Fire fire10 = new Fire(assetManager, 48, 0, 0);
        Fire fire11 = new Fire(assetManager, 42, 5, -10);
        Fire fire12 = new Fire(assetManager, 34.3f, 0, -6.7f);
        Fire fire13 = new Fire(assetManager, 39f, 0, -11.3f);
        Fire fire14 = new Fire(assetManager, 42.34f, 0, -10.45f);
        fire = new ArrayList<Fire>();

        fire.add(fire1);
        fire.add(fire2);
        fire.add(fire3);
        fire.add(fire4);
        //fire.add(fire5);
        fire.add(fire6);
        fire.add(fire7);
        fire.add(fire8);
        fire.add(fire9);
        fire.add(fire10);
        fire.add(fire11);
        fire.add(fire12);
        fire.add(fire13);
        fire.add(fire14);
        for (Fire f : fire) {
            rootNode.attachChild(f.fireNode());
        }
    }
    /*
     * Funkcia vloží všetky uzly budovy do zoznamu a následne vymaže tie,
     * ktoré predstavujú dvere. Ulzy v zozname predstavujú statické body scény, 
     * ktoré sú neprechodné (ale dá sa cez ne vidieť).
     */

    private void dontWalkCrossWalls() {
        walls = new ArrayList<Spatial>();
        //System.out.println("House ma deti: " + houseNode.getChildren().size());

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

    private void dontLookCrossWalls() {
        float x = cam.getDirection().getX();
        float z = cam.getDirection().getZ();
        float cislo = 1f;
        if (x >= 0 && z >= 0) {
            System.out.println("Pozerám vpravo pred seba.");
            player.getNode().getLocalTranslation().add(new Vector3f(-cislo, 2.8f, -cislo));
        } else if (x < 0 && z >= 0) {
            System.out.println("Pozerám vpravo za seba.");
            player.getNode().getLocalTranslation().add(new Vector3f(cislo, 2.8f, -cislo));
        } else if (x < 0 && z < 0) {
            System.out.println("Pozerám vľavo za seba.");
            player.getNode().getLocalTranslation().add(new Vector3f(cislo, 2.8f, cislo));
        }
        if (x >= 0 && z < 0) {
            System.out.println("Pozerám vľavo pred seba.");
            player.getNode().getLocalTranslation().add(new Vector3f(-cislo, 2.8f, cislo));
        }
    }

    public void initHouse() {
        houseNode = (Node) assetManager.loadModel("Models/dom/dom.j3o");
        houseNode.setName("House");
        houseNode.setLocalTranslation(30.0f, 0.05f, 0.0f);

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

    public void extinguish(int area) {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        Water water = new Water(assetManager, new Vector3f(cam.getLocation()));
        SphereCollisionShape bulletCollisionShape = new SphereCollisionShape(0.4f);
        RigidBodyControl bulletNode = new RigidBodyControl(bulletCollisionShape, 1);
        bulletNode.setLinearVelocity(cam.getDirection().mult(25));
        water.getWaterEffect().addControl(bulletNode);
        rootNode.attachChild(water.getWaterEffect());
        getPhysicsSpace().add(bulletNode);
        float x = cam.getDirection().getX();
        float z = cam.getDirection().getZ();
        if (x >= 0 && z >= 0) {
            // System.out.println("Pozerám vpravo pred seba.");
            vzdialenostOhna((-area), (-area));
        } else if (x < 0 && z >= 0) {
            //System.out.println("Pozerám vpravo za seba.");
            vzdialenostOhna(area, (-area));
        } else if (x < 0 && z < 0) {
            //System.out.println("Pozerám vľavo za seba.");
            vzdialenostOhna(area, area);
        }
        if (x >= 0 && z < 0) {
            //System.out.println("Pozerám vľavo pred seba.");
            vzdialenostOhna((-area), area);
        }
    }

    /**
     *
     * @param rozmedzieA určuje X-ovú súradnicu vzdialenosti v kladnom aj
     * zápornom smere
     * @param rozmedzieB určuje Z-ovú súradnicu vzdialenosti v kladnom aj
     * zápornom smere
     */
    public void vzdialenostOhna(int rozmedzieA, int rozmedzieB) {
        float playerX = player.getNode().getLocalTranslation().getX();
        float playerZ = player.getNode().getLocalTranslation().getZ();

        for (Fire f : fire) {
            float fireX = f.getX();
            float fireZ = f.getZ();

            if (fireX > playerX && fireZ > playerZ) {
                if ((playerX < fireX && fireX < (playerX - rozmedzieA)) && (playerZ < fireZ && fireZ < (playerZ - rozmedzieB))) {
                    float a = f.fireNode().getLocalTranslation().getX();
                    float b = f.fireNode().getLocalTranslation().getY() - 1;
                    float c = f.fireNode().getLocalTranslation().getZ();
                    f.fireNode().setLocalTranslation(a, b, c);
                    zrusenieOhna(f);
                }
            } else if (fireX > playerX && fireZ < playerZ) {
                if ((playerX < fireX && fireX < (playerX - rozmedzieA)) && (playerZ > fireZ && fireZ > (playerZ - rozmedzieB))) {
                    float a = f.fireNode().getLocalTranslation().getX();
                    float b = f.fireNode().getLocalTranslation().getY() - 1;
                    float c = f.fireNode().getLocalTranslation().getZ();
                    f.fireNode().setLocalTranslation(a, b, c);
                    zrusenieOhna(f);
                }
            } else if (fireX < playerX && fireZ > playerZ) {
                if ((playerX > fireX && fireX > (playerX - rozmedzieA)) && (playerZ < fireZ && fireZ < (playerZ - rozmedzieB))) {
                    float a = f.fireNode().getLocalTranslation().getX();
                    float b = f.fireNode().getLocalTranslation().getY() - 1;
                    float c = f.fireNode().getLocalTranslation().getZ();
                    f.fireNode().setLocalTranslation(a, b, c);
                    zrusenieOhna(f);
                }
            } else if (fireX < playerX && fireZ < playerZ) {
                if ((playerX > fireX && fireX > (playerX - rozmedzieA)) && (playerZ > fireZ && fireZ > (playerZ - rozmedzieB))) {
                    float a = f.fireNode().getLocalTranslation().getX();
                    float b = f.fireNode().getLocalTranslation().getY() - 1;
                    float c = f.fireNode().getLocalTranslation().getZ();
                    f.fireNode().setLocalTranslation(a, b, c);
                    zrusenieOhna(f);
                }
            }
        }

    }

    private void zrusenieOhna(Fire fire) {
        if (fire.getY() <= -6) {
            fire.fireNode().killAllParticles();
            fire.fireNode().setEnabled(false);
        }
    }
}
