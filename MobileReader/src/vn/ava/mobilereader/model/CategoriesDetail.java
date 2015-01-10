package vn.ava.mobilereader.model;

import java.io.Serializable;


public class CategoriesDetail implements Serializable{

	private String title;
	private String image;
	private String descript;
	private String publish;
	private int id;
	private int type;
	private String url;

	public CategoriesDetail() {

	}

	public CategoriesDetail(String title, String image, String descript,
			String publish,String url) {
		this.title = title;
		this.image = image;
		this.descript = descript;
		this.publish = publish;
		this.url = url;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoriesDetail other = (CategoriesDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
