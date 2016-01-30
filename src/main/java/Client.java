import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by lyz on 1/30/16.
 */
public class Client {
    private TransportClient client;

    public org.elasticsearch.client.Client getClient() throws UnknownHostException {
        if (this.client == null) {
            Settings settings = Settings.settingsBuilder().put("cluster.name", "my-application").build();

            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bd0"), 9300));
        }
        return client;
    }

    public void closeClient() {
        if (this.client != null) {
            client.close();
        }
    }
}
