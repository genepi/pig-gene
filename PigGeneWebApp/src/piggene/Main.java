package piggene;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
	private static final Log log = LogFactory.getLog(Main.class);

	public static void main(String[] args) throws IOException {
		try {
			int port = 8080;
			new WebServer(port).start();
		} catch (Exception e) {
			log.error("Can't launch the web server.\nAn unexpected exception occured:", e);
			System.exit(1);
		}
	}
	
}