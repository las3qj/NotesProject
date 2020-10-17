package basicNotes;

import java.util.Vector;

public class Note {
	private String content;
	private int id;
	private Vector<String> tags;
	
	public Note(String c, int i, Vector<String> ts) {
		content=c;
		id=i;
		tags=new Vector<String>();
		for(int j=0; j<ts.size(); j++) {
			if(ts.get(j)!=null)
				tags.add(ts.get(j));
		}
	}
	
	public Note(Note n) {
		this.content=n.getContent();
		this.id=n.getId();
		this.tags = n.getTags();
	}
	
	public Note() {
		id=-1;
		content = "";
		tags = new Vector<String>();
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
	public void setTags(Vector<String> ts) {
		tags = ts;
	}
	public void setId(int i) {
		id=i;
	}
	public void setContent(String con) {
		content = con;
	}
	}
