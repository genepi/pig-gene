package piggene.serialisation.cloudgene;

import java.util.Map;

public abstract class WdlParameter {

	private String id;

	private String description;

	private String value = "";

	private String type = "";

	private boolean makeAbsolute = true;

	private boolean input;

	private boolean temp = false;

	private boolean download = true;

	private boolean autoExport = false;

	private boolean mergeOutput = true;

	private boolean zip = true;

	private boolean removeHeader = true;

	private boolean required = true;

	private String format = null;

	private boolean visible = true;

	private boolean adminOnly = false;

	public static final String LOCAL_FOLDER = "local-folder";

	public static final String LOCAL_FILE = "local-file";

	public static final String HDFS_FOLDER = "hdfs-folder";

	public static final String HDFS_FILE = "hdfs-file";

	public static final String LABEL = "label";

	public Map<String, String> values;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public boolean isMergeOutput() {
		return mergeOutput;
	}

	public void setMergeOutput(final boolean mergeOutput) {
		this.mergeOutput = mergeOutput;
	}

	public boolean isInput() {
		return input;
	}

	public void setInput(final boolean input) {
		this.input = input;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(final Map<String, String> values) {
		this.values = values;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(final boolean download) {
		this.download = download;
	}

	public boolean isMakeAbsolute() {
		return makeAbsolute;
	}

	public void setMakeAbsolute(final boolean absolute) {
		this.makeAbsolute = absolute;
	}

	public boolean isTemp() {
		return temp;
	}

	public void setTemp(final boolean temp) {
		this.temp = temp;
	}

	public void setZip(final boolean zip) {
		this.zip = zip;
	}

	public boolean isZip() {
		return zip;
	}

	public void setRemoveHeader(final boolean removeHeader) {
		this.removeHeader = removeHeader;
	}

	public boolean isRemoveHeader() {
		return removeHeader;
	}

	public void setRequired(final boolean required) {
		this.required = required;
	}

	public boolean isRequired() {
		return required;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(final String format) {
		this.format = format;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setAutoExport(final boolean autoExport) {
		this.autoExport = autoExport;
	}

	public boolean isAutoExport() {
		return autoExport;
	}

	public void setAdminOnly(final boolean adminOnly) {
		this.adminOnly = adminOnly;
	}

	public boolean isAdminOnly() {
		return adminOnly;
	}

}
