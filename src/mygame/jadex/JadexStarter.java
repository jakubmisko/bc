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
 * Starter class for jadex agents
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
     * @param components Array of paths to jadex agents which needs to be
     * created
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
     * Method that disable jadex agent
     *
     * @param id ID of already running agent
     */
    public void killAgent(IComponentIdentifier id) {
        cms.destroyComponent(id);
    }

    /**
     * Method that create components
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

    public void shutDown() {
        platform.shutdownNFPropertyProvider();

    }
}
