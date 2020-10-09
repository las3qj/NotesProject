import java.util.*;

public class Note {
	private String content;
	//private int size;
	//private int lines;
	//private Vector<Tag> tags;
	
	//Accessor functions
	public String getContent() {return content;}
	//public int getSize() {return size;}
	//public int getLines() {return lines;}
	//public Vector<Tag> getTags() {return tags;}
	
	//Mutator functions
	public void setContent(String newContent) {
		content = newContent;
	}
	
	//Constructor
	public Note(String newContent) {
		content = newContent;
	}

}
