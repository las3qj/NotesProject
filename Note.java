package basicNotes;

import java.util.Vector;

public class Note {
	private String content;
	private int id;
	private Vector<String> tags;
	
	public Note(String c, int i, Vector<String> ts) {
		content=c;
		id=i;
		tags=ts;
	}
	
	public Note(Note n) {
		this.content=n.getContent();
		this.id=n.getId();
		this.tags = n.getTags();
	}
	
	public String getContent() {
		return content;
	}
	public int getId() {
		return id;
	}
	public Vector<String> getTags(){
		return tags;
	}
}
