package piggene.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

	static {
		try {
			prop.load(CloudgeneYamlGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowDefs = prop.getProperty("workflowDefs");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void downloadLibraryFile(final String libraryFileLink) throws IOException, ZipException {
		final URL website = new URL(libraryFileLink);
		final ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		final String fileName = parseLibraryFileName(libraryFileLink);
		final String sourcePath = workflowDefs + fileName;
		final FileOutputStream fos = new FileOutputStream(sourcePath);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

		if (hasZipFileEnding(fileName)) {
			ZipFileGenerator.extractZipFile(sourcePath, workflowDefs);
			final File zip = new File(sourcePath);
			if (zip.exists()) {
				zip.delete();
			}
		}
	}

	private static String parseLibraryFileName(final String libraryFileLink) {
		final int startIndex = libraryFileLink.lastIndexOf("/");
		return libraryFileLink.substring(startIndex);
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