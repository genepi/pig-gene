package server;

import org.restlet.Component;
import org.restlet.VirtualHost;
import org.restlet.data.Protocol;

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