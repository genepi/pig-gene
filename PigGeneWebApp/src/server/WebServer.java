package server;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;

/**
 * Webserver class.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class WebServer extends Component {

	public WebServer(final int port) throws Exception {

		// Add the connectors
		getServers().add(Protocol.HTTP, port);
		getClients().add(Protocol.FILE);
		getClients().add(Protocol.CLAP);
		final VirtualHost host = new VirtualHost(getContext());
		host.attach(new WebApp());
		getHosts().add(host);
	}

}