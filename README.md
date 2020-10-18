# NotesProject
Project to develop an application to organize note snippets according to customizable categories and tags.

For basic information on how to use this application, see section "Usage". 
For a detailed (yet still abridged) rundown on the project's internal organization, see section "Files - Basic Information".

## Files - Basic Information
NotesProject is divided into four main class files, some of which contain private classes for simplification and organization. 

### Note.java
Note is the simplest of the four classes, and is an object for storing the contents of a Note. A note is composed of a "content" String, an Identification integer "id", and a Vector containing an unbounded number of Strings called "tags" which represents the various tags a note is marked with. 

A note instance can be constructed with no parameters, all three parameters, or another Note object. It has accessor and mutator functions for each of its three instance variables.

A Note object is returned from several methods in BasicNotesDB, the DataBase class, and is primarily used for maintaining coherence between the database representation of "Notes" and how notes are represented in the user interface. The purpose of the Note is to maintain ease of communication between these two classes.

### BasicNotes.java
BasicNotes is the main class for the project. 

Containing the main method, an instance of BasicNotesDB, and an instance of BasicNotesApp, it gathers a few prerequisite pieces of information from the user (pathname for the database, whether the database should be preloadedd with test categories and notes), and accoringly constructs the database and application.

### BasicNotesDB.java
This class is the direct interface with the SQLite database, and draws heavily from the JDBC library. 

A BasicNotesDB instance can be constructed with two parameters: a String pathname "url" and a boolean "tests" to determine whether test notes and categories should be loaded.

BasicNotesDB is organized into two primary sections, with many subdivisions: private methods (helper methods) and public methods. Subdivisions include methods to maintain proper SQLite strings for all operations, construction methods, and methods concerning the notes table, categories table, and tags table.

The structure of the database is as follows: three tables are created on construction. 

#### Taginfo Table
The "taginfo" table contains information regarding the current maximum number of tags in both the "notes" and "categories" table. It has two columns, one "name" containing a String corresponding to eiter of the two other tables, and another "tags" corresponding to an integer of how many tag columns are currently in the table represented by "name". This table merely keeps track of the maximum number of tags in both tables to allow the SQLite statement strings to operate appropraitely on the other tables as they are repeatedly altered to add new tag columns.

#### Notes Table
The "notes" table contains all notes the user adds to the database, with columns for integer "id" number, by which they are identified, "content" String, or the content the user embodies in the note, an integer "tags" representing the number of non-null tags in the note, and a scalable number of columns "tag'i'", which are Strings, each representing one tag the user has identified the note with.  

The number of tag columns increases whenever the user saves a note with more tags than the current number of tag columns in the notes table. The table is altered, the taginfo table is updated, and all relevant SQL statement strings are adapted for this change. 

Many public-facing methods exist in notes to allow the user to add, update, get, and delete notes from the table. The one parameter a user cannot alter is a notes "id", which is used strictly for identification purposes.

#### Categories Table
The "categories" table contains all categories the user adds to teh database, with columns for the category's "name" String, an integer "tags" representing the number of non-null tags in the category, and a scalable number of columns "tag'i'", which are Strings, each representing one tag the user has created within the category.

The number of tag columns increases whenever the user adds a tag to a category to create more tags than the current number of tag columns in the categories table. The table is altered, the taginfo table is updated, and all relevant SQL statement strings are adapted for this change. 

Many public-facing methods exist in categories to allow the user to add a category, add a tag to an existing category, update the category name, get categories, tags within categories, and delete either of these two.

### BasicNotesApp.java
This class is the direct interface with the user, and draws heavily from the Java Swing library.

A BasicNotesApp instance is constructed with only one parameter--the BasicNotesDB instance--but on construction immediately requests from the user its appropriate width and height component, scalaing (somewhat) appropriately. (Note, at its current state, the app is usable but not practical below a certain size threshold. Best results at 1500x1000, which are the default dimensions).

The BasicNotesApp is a complexly nested JFrame with three main sub-Components, the Categories panel, the Selection panel, and the CurrentNote panel. It also contains a private class simpleListener which handles all events from the various components within BasicNotesApp, and a private class notePreview that extends JPanel and is used in the Selection Panel. 

#### Categories Panel
The categories panel is the smallest major panel of the three, alloted one fifth of the total application width. It is composed of several nested JPanels with a JScrollPanel that has an always-visible vertical scrollbar. 

Within the smallest subpanel is contained a JTree which represents each category within the database as well as each category's list of tags. At the end of each category is a node entitled "Add tag" and at the end of all categories is a node entitled "Add category". Each node is user-interactable and with the exception of the "add" nodes populates the selection panel with notes. The "add" nodes, expectedly, allow the user to add a category or a tag to the database and reflects this change immediately within the tree structure.

#### Selection Panel
The selection panel is the next smallest major panel and is alloted three tenths of the total application width. Similarly to the categories panel, it is composed of several nested JPanels with a JSCrollPanel that has an always-visible vertical scrollbar.

Within the JScrollPanel is a JPanel that contains a vertical list of notePreview objects, which contain information from an associated Note object, that correspond to the notes in whichever category, tag, or multiple selection is currently selected in the categories panel. The only (note: currently) information from the note displayed in the currNote object is its content text, which is wrapped horizontally and may be cut off by the limits of the currNote object, which is restricted in size. If there are more notes than can be displayed on screen, the user can use the scrollbar to scroll down to new notes.

Each currNote object uses a common instance of the simpleListener class, and upon selection of one note, that note's contents populate the current note panel.

#### Current Note Panel
The current note panel is the largest of the three major panels and is alloted one half of the total application width. It is composed of three main sub-elements, a JPanel of "tag" labels at the top, a JPanel of buttons at the bottom, and a JTextArea containing the current note's editable contents in the center.

The top panel, when a note has been selected from the selection panel, or a note has been created from the bottom panel, lists all tags currently associated with the current note. In addition, a JButton appears at the end of the list of tags to allow the user to add a additional tags to the note.

The bottom panel always contains two buttons: one which allows the user to create a new note and populate the current note panel with its contents, and one to allow the user to save the current note panel's contents to the corresponding note in the database. If the save button has been activated without any tags present on the note, a JOptionPane will alert the user that a note cannot be saved without tags.

The middle panel always contains a JTextArea object which represents the editable content of the note selected or being created. The content, along with the tags, is only saved to a note once the "save" button has been activated.

## Usage
Usage will focus on how to best interact with the user interface, and what tools within the application the user can utilize to begin organizing their notes!

### Startup
The application will prompt for a proper location to create a database. To get a set of testing notes to allow quick familiarization with the environment, respond "yes" to the prompt concerning test data. Lastly, two prompts will request app dimensions, for which the application is scalable.
Note: the default dimensions are those for which it is optimized. It will scale for other dimensions, and may work quite well for many, but the default is the current recommendation.

![image](https://user-images.githubusercontent.com/72612647/96357292-18c50200-10c8-11eb-9cad-32c13c42ed9c.png)

(App on startup)

### Categories
What can you do with the categories panel (the leftmost panel containing the tree)? Selecting a category or tag will populate the middle panel (the selection panel) with notes associated with that tag or with all tags beneath that category.
Note: control-click and shift-click is supported for tag selections! Selection multiple tags currently displays the Union of their corresponding notes.

To add a new tag, simply navigate to the corresponding category and select the "add tag" option. The same is true for adding a new category.

To delete a tag, right click a tag and select "delete". Notice that in the corresponding selection panel and current note panel the deleted tag is removed. The same is true of categories.

### Selecting Notes
Once a category or tag has been selected, the middle panel will become populated with notes corresponding to that tag, category, or multiple selection. Selecting one of those notes will populate the rightmost panel (the current note panel) with the content of the note you selected.

To delete a note, right click its preview in the selection panel and click "delete". The note will be removed from the selection panel, the current note panel (if applicable) and the database.

### Current Note
Once a note has been selected, its content can be changed and its tags can be edited. The top panel displays all current tags of the currently displayed note. To add a tag, simply click the "Add tag" button. To delete a tag, right click a tag and select "delete". 

To edit the content of the note, simply type away in the middle text area!

To save the changes made to the note, click the save button in the bottom panel. Note the reflected changes in the selection panel!
Note: changes to the note content or tags are not saved automatically, and must be manually saved from this button.

To create a new note, select the "Create" button on the bottom panel. The panel will populate with an empty text area and an empty list of tags. Save the note to publish it and see reflected changes (if applicable) in the selection panel!
Note: a note cannot be saved without any tags.

### Close 
Simply click the x in the top right to close the application. All changes commited will be saved to the database and ready to resume upon next usage!
