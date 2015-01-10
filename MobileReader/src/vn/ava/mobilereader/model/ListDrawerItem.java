package vn.ava.mobilereader.model;

public class ListDrawerItem {

	private String text;
	private int icon;
	private int type;
	
	public ListDrawerItem(String text, int icon, int type){
		
		this.text = text;
		this.icon = icon;
		this.type = type;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
