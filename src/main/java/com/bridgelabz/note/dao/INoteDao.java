package com.bridgelabz.note.dao;

import java.util.List;

import java.util.Set;

import com.bridgelabz.note.model.Collaborator;
import com.bridgelabz.note.model.CollaboratorResponseDto;
import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteLabel;
import com.bridgelabz.user.model.User;
import com.bridgelabz.user.util.UrlData;

public interface INoteDao {

	void saveNote(Note note);

	boolean updateNote(Note note);

	boolean deleteNote(int noteId);

	Note getNoteById(int noteId);

	List<Note> getAllNotes(int userId);

	int noteCreatorByNoteId(int noteId);
	
   //.............Label......................//
	void saveLabel(Label label);

	List<Label> getAllLabels(int userId);
	
	boolean updateLabel(Label label);
	
	boolean deleteLabel(int labelId);
	
	Label getLabelById(int labelId);

	Set<Label> getLabelsByNote(Note note);
	void addLabel(NoteLabel noteLabel);
	void deleteLabelFromNote(NoteLabel noteLabel);
    List<Label> getNoteLabels(int noteId);
	boolean isLabelExists(String labelTitle);
	 
	 //.......................collaborator..................
     boolean saveCollaborator(Collaborator collaborator,int userId);
     
     public  User getUserById(int userId); 
     
 	

 	 
 	 List<CollaboratorResponseDto> getCollaboratorsByNote(int noteId);
 	 
 	CollaboratorResponseDto getSharedNotes(int noteId,int shareUserId);//get shared note response

	//List<Collaborator> getCollaboratorBySharedId(String email);
 	List<Collaborator> getCollaboratorNoteIdAndUserId(int userId);
	 User getsharedUserByEmail(String email);
	 void removeCollaborator(Collaborator collaborator);
	/*	public List<Note> getCollaboratedNotes(int noteId,String sharedUserId);*/
	 List<UrlData> getAllUrls(int noteId);


  	  
	

}
