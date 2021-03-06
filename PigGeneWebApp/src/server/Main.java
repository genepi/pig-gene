package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
	private static Properties prop = new Properties();

	public static void initializeFolders() {
		final List<String> paths = new ArrayList<String>();
		paths.add(prop.getProperty("workflowDefs"));
		paths.add(prop.getProperty("componentDefs"));
		paths.add(prop.getProperty("cloudgeneYamls"));
		paths.add(prop.getProperty("scriptFiles"));
		for (final String path : paths) {
			final File f = new File(path);
			if (!(f.exists() && f.isDirectory())) {
				f.mkdirs();
			}
		}
	}

	public static void main(final String[] args) throws IOException {
		try {
			prop.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
			final int port = Integer.valueOf(prop.getProperty("port"));
			initializeFolders();
			new WebServer(port).start();
		} catch (final Exception e) {
			log.error("Can't launch the web server.\nAn unexpected exception occured: ", e);
			System.exit(1);
		}
	}

}