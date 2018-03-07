package nunet.rbsk.model;

import java.util.ArrayList;

public class Parent {
	private String name;

	private boolean checked;
	private ArrayList<Question> children;

	public String getName() {
		return name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Question> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Question> children) {
		this.children = children;
	}
}
