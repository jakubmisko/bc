package mygame.jadex;

import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ThreadSuspendable;

/**
 * Spúšťač platformy systému Jadex
 */
public class JadexStarter implements Runnable {
    private IExternalAccess platform;
    private IComponentIdentifier[] identifiers;
    private String[] components;
    private ThreadSuspendable threadSuspendable;
    private IComponentManagementService cms;
    private final String[] defaultArgs = new String[]{
        "-gui", "false",
        "-welcome", "false",
        "-cli", "false",
        "-printpass", "false"
    };

    /**
     *
     * @param components Pole reťazcov s cestami k agentom, ktoré majú byť vytvorené
     */
    public JadexStarter(String components[]) {
        this.components = components;
        IFuture<IExternalAccess> platformFuture = Starter.createPlatform(defaultArgs);
        threadSuspendable = new ThreadSuspendable();
        platform = platformFuture.get(threadSuspendable);
        System.out.println("Started platform: " + platform.getComponentIdentifier());
        cms = SServiceProvider.getService(platform.getServiceProvider(), IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get(threadSuspendable);
    }

    /**
     * Odstráni vytvoreného agenta zo spustenej platformy
     *
     * @param id ID agenta, ktorý ma byť odstránený
     */
    public void killAgent(IComponentIdentifier id) {
        cms.destroyComponent(id);
    }

    /**
     * Metóda vytvarájúca agentov, po vytvorení uloží id do poľa
     */
    public void run() {
        int identifier = 0;
        identifiers = new IComponentIdentifier[components.length];
        for (String component : components) {
            identifiers[identifier] = cms.createComponent(null, component, null, null).get(threadSuspendable);
            // 	IComponentIdentifier cid = cms.createComponent(null, component, null, null).get(threadSuspendable);
            System.out.println("Started component: " + identifiers[identifier++]);
        }
    }
    /**
     * Vypnutie platformy
     */
    public void shutDown() {
        platform.shutdownNFPropertyProvider();
    }
}
