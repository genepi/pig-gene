package piggene.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lingala.zip4j.exception.ZipException;
import piggene.serialisation.cloudgene.CloudgeneYamlGenerator;
import piggene.serialisation.zip.ZipFileGenerator;

public class LibraryAddOn {
	private static Properties prop = new Properties();
	private static String workflowDefs = "";
	private static final int TIMEOUT_MILLIS = 4000;

	static {
		try {
			prop.load(CloudgeneYamlGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowDefs = prop.getProperty("workflowDefs");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	public static void downloadLibraryFile(final String libraryFileLink) throws IOException, ZipException {
		final URL url = new URL(libraryFileLink);
		if (websiteExists(url)) {
			final ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			final String fileName = parseLibraryFileName(libraryFileLink);
			final String sourcePath = workflowDefs + fileName;
			final FileOutputStream fos = new FileOutputStream(sourcePath);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			rbc.close();

			if (hasZipFileEnding(fileName)) {
				ZipFileGenerator.extractZipFile(sourcePath, workflowDefs);
				final File zip = new File(sourcePath);
				if (zip.exists()) {
					zip.delete();
				}
			}
			fos.close();
		}
	}

	private static boolean websiteExists(final URL url) throws IOException {
		final HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		huc.setRequestMethod("GET");
		huc.setConnectTimeout(TIMEOUT_MILLIS);
		huc.connect();
		final int responseCode = huc.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
			throw new IOException("website not existing");
		}
		return true;
	}

	private static String parseLibraryFileName(final String libraryFileLink) {
		final int startIndex = libraryFileLink.lastIndexOf("/");
		return libraryFileLink.substring(startIndex + 1);
	}

	private static boolean hasZipFileEnding(final String fileName) {
		final String regex = "(.*)(.zip)(\\b)";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(fileName);
		if (m.find()) {
			return true;
		}
		return false;
	}

}