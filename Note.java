package basicNotes;

import java.util.Vector;

public class Note {
	private String content;
	private int id;
	private String tag1, tag2;
	
	public Note(String c, int i, String t1, String t2) {
		content=c;
		id=i;
		tag1=t1;
		tag2=t2;
	}
	
	public Note(Note n) {
		this.content=n.getContent();
		this.id=n.getId();
		this.tag1 = n.getTags().get(0);
		this.tag2 = n.getTags().get(1);
	}
	
	public String getContent() {
		return content;
	}
	public int getId() {
		return id;
	}
	public Vector<String> getTags(){
		Vector<String> tags = new Vector<String>();
		tags.add(tag1);
		tags.add(tag2);
		return tags;
	}
}
