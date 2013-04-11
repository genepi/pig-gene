package server;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Main class of the server.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class Main {
	private static final Log log = LogFactory.getLog(Main.class);

	public static void main(final String[] args) throws IOException {
		try {
			final int port = 8080;
			new WebServer(port).start();
		} catch (final Exception e) {
			log.error("Can't launch the web server.\nAn unexpected exception occured: ", e);
			System.exit(1);
		}
	}

}