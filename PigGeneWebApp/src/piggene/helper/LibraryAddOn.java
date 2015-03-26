package piggene.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;

import piggene.serialisation.cloudgene.CloudgeneYamlGenerator;
import piggene.serialisation.zip.ZipFileGenerator;

public class LibraryAddOn {
	private static Properties prop = new Properties();
	private static String definitions = "";
	private static String componentDefs = "";
	private static String workflowDefs = "";
	private static final String COMPONENTS_FOLDER_NAME = "components";
	private static final String WORKFLOWS_FOLDER_NAME = "workflows";
	private static final int TIMEOUT_MILLIS = 4000;

	static {
		try {
			prop.load(CloudgeneYamlGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			definitions = prop.getProperty("definitions");
			componentDefs = prop.getProperty("componentDefs");
			workflowDefs = prop.getProperty("workflowDefs");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	public static void downloadLibraryFile(final String libraryFileLink) throws IOException, ZipException, PigGenePackageException {
		final URL url = new URL(libraryFileLink);
		if (websiteExists(url)) {
			final String fileName = parseLibraryFileName(libraryFileLink);
			if (hasZipFileEnding(fileName)) {
				final ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				final String sourcePath = definitions + fileName;
				final FileOutputStream fos = new FileOutputStream(sourcePath);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				rbc.close();
				fos.close();
				unzipFile(sourcePath);
				final String unzippedFolderPath = getNameOfUnzippedFolder();
				verifyPigGenePackageStructure(unzippedFolderPath);
				moveComponentsAndWorkflows(unzippedFolderPath);
			}
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

	private static void unzipFile(final String zipFilePath) throws ZipException {
		ZipFileGenerator.extractZipFile(zipFilePath, definitions);
		final File zip = new File(zipFilePath);
		if (zip.exists()) {
			zip.delete();
		}
	}

	private static String getNameOfUnzippedFolder() throws IOException {
		final File directory = new File(definitions);
		final File[] listFiles = directory.listFiles();
		for (final File f : listFiles) {
			final String filePath = f.getPath().concat("/");
			if (!(filePath.equals(componentDefs) || filePath.equals(workflowDefs)) && f.isDirectory()) {
				return f.getPath();
			}
		}
		throw new IOException("There was a file unzipping problem.");
	}

	private static void verifyPigGenePackageStructure(final String folderPath) throws PigGenePackageException {
		final File directory = new File(folderPath);
		final File[] files = directory.listFiles();
		if (!compliesWithPigGenePackageFormat(files)) {
			directory.delete();
			throw new PigGenePackageException("library folder does not comply to the pigGene package format");
		}
	}

	private static boolean compliesWithPigGenePackageFormat(final File[] files) {
		if (files == null || files.length == 0) {
			return false;
		}
		final List<String> fileNames = new ArrayList<String>();
		for (final File f : files) {
			fileNames.add(f.getName());
		}
		if (!(fileNames.contains(COMPONENTS_FOLDER_NAME) || fileNames.contains(WORKFLOWS_FOLDER_NAME))) {
			return false;
		}
		return true;
	}

	private static void moveComponentsAndWorkflows(final String srcFolderPath) throws IOException {
		final File srcDirectoryComponents = new File(srcFolderPath + "/" + COMPONENTS_FOLDER_NAME);
		if (srcDirectoryComponents.exists() && srcDirectoryComponents.isDirectory()) {
			final File[] files = srcDirectoryComponents.listFiles();
			for (final File sourceFile : files) {
				Files.move(sourceFile.toPath(), new File(componentDefs.concat(sourceFile.getName())).toPath(), StandardCopyOption.ATOMIC_MOVE);
			}
		}
		final File srcDirectoryWorkflows = new File(srcFolderPath + "/" + WORKFLOWS_FOLDER_NAME);
		if (srcDirectoryWorkflows.exists() && srcDirectoryWorkflows.isDirectory()) {
			final File[] files = srcDirectoryWorkflows.listFiles();
			for (final File sourceFile : files) {
				Files.move(sourceFile.toPath(), new File(workflowDefs.concat(sourceFile.getName())).toPath(), StandardCopyOption.ATOMIC_MOVE);
			}
		}
		FileUtils.deleteDirectory(new File(srcFolderPath));
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