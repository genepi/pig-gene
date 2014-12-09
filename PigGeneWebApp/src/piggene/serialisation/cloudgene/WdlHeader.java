package piggene.serialisation.cloudgene;

public class WdlHeader {

	private boolean installed = false;
	private String source = "";
	private String description;
	private String version;
	private String website;
	private String name;
	private String category;
	private String author;

	public boolean isExpanded() {
		return false;
	}

	public boolean isLeaf() {
		return true;
	}

	public WdlHeader[] getChildren() {
		return null;
	}

	public boolean isInstalled() {
		return installed;
	}

	public void setInstalled(final boolean installed) {
		this.installed = installed;
	}

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(final String website) {
		this.website = website;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

}
