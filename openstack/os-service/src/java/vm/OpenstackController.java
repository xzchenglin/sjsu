package vm;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.identity.EndpointURLResolver;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.identity.URLResolverParams;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.identity.internal.DefaultEndpointURLResolver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lin Cheng
 *
 */
public class OpenstackController {

    static final String PROJ_ID = "cc931089a00f455a87be0f39df3ef40c";
    static final String HOST = "localhost";

    static final EndpointURLResolver endpointUrlResolver = new DefaultEndpointURLResolver(){
        @Override
        public String findURLV3(URLResolverParams arg0) {
            String ret = super.findURLV3(arg0);
            ret = ret.replace("controller", HOST);
            return ret;
        }

        @Override
        public String findURLV2(URLResolverParams arg0) {
            String ret = super.findURLV2(arg0);
            ret = ret.replace("controller", HOST);
            return ret;
        }
    };

    private OpenstackController() {
    }

    private static OpenstackController instance = new OpenstackController();
    private OSClient.OSClientV3 os;

    public static OpenstackController instance(){
        if(instance.os == null){
            instance.authenticate();
        }
        if(instance.os.getToken().getExpires().before(new Date())){//expired
            instance.authenticate();
        }
        return instance;
    }

    public Token authenticate(){
        os = OSFactory.builderV3()
                .endpoint("http://" + HOST + ":5000/v3")
                .credentials("admin", "admin_user_secret", Identifier.byName("Default"))
                .scopeToProject(Identifier.byId(PROJ_ID))
                .withConfig(Config.newConfig().withEndpointURLResolver(endpointUrlResolver))
                .authenticate();

        return os.getToken();
    }

    public List<? extends Server> getServers(){
        List<? extends Server> ret = os.compute().servers().list();
        return ret;
    }

    public List<? extends Flavor> getFlavors(){
        List<? extends Flavor> ret = os.compute().flavors().list();
        return ret;
    }

    public List<? extends Image> getImages(){
        List<? extends Image> ret = os.compute().images().list();
        return ret;
    }

    public List<? extends Network> getNetworks(){
        List<? extends Network> ret = os.networking().network().list();
        return ret;
    }

    public List<? extends Subnet> getSubnets(){
        List<? extends Subnet> ret = os.networking().subnet().list();
        return ret;
    }

    public Server createServer(String name, String flavorId, String imageId, String netId){
        List<String> netList = new ArrayList<>();
        netList.add(netId);
        ServerCreate sc = Builders.server()
                .name(name)
                .flavor(flavorId)
                .image(imageId)
//                .addNetworkPort("")
                .networks(netList)
                .build();

        Server server = os.compute().servers().boot(sc);
        os.compute().servers().action(server.getId(), Action.START);
        return server;
    }

    public int delServer(String id){
        ActionResponse ret = os.compute().servers().delete(id);
        return ret.getCode();
    }

    public int opServer(String id, String action){
        ActionResponse ret = os.compute().servers().action(id, Action.valueOf(action));
        return ret.getCode();
    }

}
