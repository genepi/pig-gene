package piggene.serialisation.pig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import piggene.helper.MissingConnectionException;
import piggene.helper.WorkabilityChecker;
import piggene.representation.WorkflowFlowSequence;
import piggene.serialisation.workflow.Workflow;

public class PigScriptGenerator {
	private static Properties prop = new Properties();
	private static String scriptFilesPath;
	private static String libFilesPath;
	private static String jarPath = "libs/";
	private static String fileExtension = ".pig";
	private static String lineSeparator = System.getProperty("line.separator");
	private static String[] jarLibNames = new String[] { "pigGene.jar", "SeqPig.jar", "hadoop-bam-6.2.jar", "samtools-1.107.jar", "picard-1.107.jar",
			"commons-jexl-2.1.1.jar" };

	static {
		try {
			prop.load(PigScriptGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			scriptFilesPath = prop.getProperty("scriptFiles");
			libFilesPath = prop.getProperty("libFiles");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	public static void generateAndStoreScript(final Workflow workflow) throws IOException, MissingConnectionException {
		final String workflowName = workflow.getName();
		final StringBuilder sb = new StringBuilder();
		sb.append(insertHeader());
		sb.append(lineSeparator);
		sb.append(insertDefinedFunctionNames());

		// tests if all input and output connections are set may throw Exception
		WorkabilityChecker.checkConnectionIntegrity(workflow);
		final List<Workflow> workflowOrdering = WorkflowFlowSequence.constructWorkflowFlowSequence(workflow);
		for (final Workflow wf : workflowOrdering) {
			sb.append(wf.getPigScriptRepresentation(workflow));
		}
		createNeededFolders(workflowName);
		PigScriptGenerator.write(sb.toString(), workflowName);
	}

	private static void createNeededFolders(final String folderName) throws IOException {
		final File destinationFolder = new File(scriptFilesPath + folderName + "/");
		File libs = null;
		if (destinationFolder.exists()) {
			FileUtils.deleteDirectory(destinationFolder);
		}
		destinationFolder.mkdir();
		libs = new File(destinationFolder.getPath() + "/libs");
		libs.mkdir();
		copyLibFiles(libFilesPath.concat("/"), libs.toString().concat("/"));
	}

	private static void copyLibFiles(final String sourceFolderPath, final String destinationFolderPath) throws IOException {
		File source = null;
		File destination = null;
		for (final String fileName : jarLibNames) {
			source = new File(sourceFolderPath.concat(fileName));
			destination = new File(destinationFolderPath.concat(fileName));
			Files.copy(source.toPath(), destination.toPath());
		}
	}

	private static String insertHeader() {
		final StringBuilder sb = new StringBuilder();
		for (final String lib : jarLibNames) {
			sb.append("REGISTER ");
			sb.append(jarPath);
			sb.append(lib);
			sb.append(";");
			sb.append(lineSeparator);
		}
		return sb.toString();
	}

	private static String insertDefinedFunctionNames() {
		final StringBuilder sb = new StringBuilder();
		//loaders
		sb.append("DEFINE BamLoader fi.aalto.seqpig.io.BamLoader('yes');");
		sb.append(lineSeparator);
		sb.append("DEFINE SamLoader fi.aalto.seqpig.io.SamLoader('yes');");
		sb.append(lineSeparator);
		sb.append("DEFINE FastqLoader fi.aalto.seqpig.io.FastqLoader();");
		sb.append(lineSeparator);
		sb.append("DEFINE FastaLoader fi.aalto.seqpig.io.FastaLoader();");
		sb.append(lineSeparator);
		sb.append("DEFINE QseqLoader fi.aalto.seqpig.io.QseqLoader();");
		sb.append(lineSeparator);
		
		//UDF's
		sb.append("DEFINE ReadPaired fi.aalto.seqpig.filter.SAMFlagsFilter('HasMultipleSegments');");
		sb.append(lineSeparator);
		sb.append("DEFINE ReadMappedInPair fi.aalto.seqpig.filter.SAMFlagsFilter('IsProperlyAligned');");
		sb.append(lineSeparator);
		sb.append("DEFINE ReadUnmapped fi.aalto.seqpig.filter.SAMFlagsFilter('HasSegmentUnmapped');");
		sb.append(lineSeparator);
		sb.append("DEFINE MateUnmapped fi.aalto.seqpig.filter.SAMFlagsFilter('NextSegmentUnmapped');");
		sb.append(lineSeparator);
		sb.append("DEFINE ReadReverseStrand fi.aalto.seqpig.filter.SAMFlagsFilter('IsReverseComplemented');");
		sb.append(lineSeparator);
		sb.append("DEFINE MateReverseStrand fi.aalto.seqpig.filter.SAMFlagsFilter('NextSegmentReversed');");
		sb.append(lineSeparator);
		sb.append("DEFINE FirstInPair fi.aalto.seqpig.filter.SAMFlagsFilter('IsFirstSegment');");
		sb.append(lineSeparator);
		sb.append("DEFINE SecondInPair fi.aalto.seqpig.filter.SAMFlagsFilter('IsLastSegment');");
		sb.append(lineSeparator);
		sb.append("DEFINE NotPrimaryAlignment fi.aalto.seqpig.filter.SAMFlagsFilter('HasSecondaryAlignment');");
		sb.append(lineSeparator);
		sb.append("DEFINE ReadFailsQC fi.aalto.seqpig.filter.SAMFlagsFilter('FailsQC');");
		sb.append(lineSeparator);
		sb.append("DEFINE IsDuplicate fi.aalto.seqpig.filter.SAMFlagsFilter('IsDuplicate');");
		sb.append(lineSeparator);
		sb.append("DEFINE AvgBaseQualCounts fi.aalto.seqpig.stats.AvgBaseQualCounts();");
		sb.append(lineSeparator);
		sb.append("DEFINE FormatAvgBaseQualCounts fi.aalto.seqpig.stats.FormatAvgBaseQualCounts();");
		sb.append(lineSeparator);
		return sb.toString();
	}

	private static void write(final String pigScript, final String name) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(scriptFilesPath.concat(name).concat("/").concat(name.concat(".pig"))));
			out.write(pigScript);
		} finally {
			try {
				out.close();
			} catch (final IOException ignore) {
				// ignore
			}
		}
	}

	public static String load(final String scriptName) throws IOException {
		final Path path = FileSystems.getDefault().getPath(scriptFilesPath, "/", scriptName, scriptName.concat(fileExtension));
		String script = null;
		final byte[] bytes = Files.readAllBytes(path);
		script = new String(bytes, "UTF-8");
		return script;
	}

}