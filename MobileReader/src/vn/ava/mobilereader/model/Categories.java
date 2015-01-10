package vn.ava.mobilereader.model;

public class Categories {

	private String url;
	private String src;
	private String title;
	private int type;
	private int id;

	public Categories() {

	}

	public Categories( String url, String src, String title, int type) {

		this.url = url;
		this.src = src;
		this.title = title;
		this.type = type;

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


}
