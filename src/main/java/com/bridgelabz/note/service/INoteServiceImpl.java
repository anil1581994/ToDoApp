package com.bridgelabz.note.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.exceptions.EmailAlreadyExistsException;
import com.bridgelabz.exceptions.UnAuthorizedAccessUser;
import com.bridgelabz.note.controller.NoteController;
import com.bridgelabz.note.dao.INoteDao;
import com.bridgelabz.note.model.Collaborator;
import com.bridgelabz.note.model.CollaboratorResponseDto;
import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteLabel;
import com.bridgelabz.note.model.NoteRequestDto;
import com.bridgelabz.note.model.NoteResponseDto;
import com.bridgelabz.note.model.UpdateNoteDto;
import com.bridgelabz.user.dao.UserDao;
import com.bridgelabz.user.model.User;

@Service
public class INoteServiceImpl implements INoteService {

	@Autowired
	INoteDao noteDao;

	@Autowired
	UserDao userDao;
	@Autowired
    Collaborator collaborator;
	@Override
	public NoteResponseDto createNote(NoteRequestDto noteRequestDto, int userId) {

		Note note = new Note(noteRequestDto);

		Date date = new Date();
		note.setCreateDate(date);
		note.setLastUpdateDate(date);

		User user = new User();
		user.setId(userId);
		note.setUser(user);

		noteDao.saveNote(note);

		NoteResponseDto noteResponseDto = new NoteResponseDto(note);
		return noteResponseDto;
	}

	@Override
	public boolean updateNote(UpdateNoteDto updateNoteDto) {

		Note note = new Note(updateNoteDto);

		Date date = new Date();
		note.setLastUpdateDate(date);
		note.setReminder(updateNoteDto.getReminder());
		note.setImage(updateNoteDto.getImage());

		boolean status = noteDao.updateNote(note);
		return status;
	}

	@Override
	public Note getNoteById(int noteId) {
		return noteDao.getNoteById(noteId);
	}

	@Override
	public void deleteNote(int noteId, int userId) {
		Note note = noteDao.getNoteById(noteId);
		System.out.println("user"+note.getUser().getId());
		System.out.println("note"+note.getNoteId());
		if (note.getUser().getId() != userId) {
			
			throw new UnAuthorizedAccessUser();
		}
		noteDao.deleteNote(noteId);
	}

	@Override
	public List<NoteResponseDto> getAllNotes(int userId)
	{

		
		List<Note> list = noteDao.getAllNotes(userId);//2391

		List<Collaborator> collaboratorList = noteDao.getCollaboratorNoteIdAndUserId(userId);//2391																						// collaborator

		if (collaboratorList != null) 
		{
			for (Collaborator object : collaboratorList)
			{
				
				CollaboratorResponseDto collabObj=noteDao.getSharedNotes(object.getNoteId(),object.getSharedUserId());
				
			    collabObj.setOwnerId(object.getUserId());
			
		
	           Note obj = new Note(collabObj);
	           User user=userDao.getUserById(object.getUserId());
	             obj.setCollaboratorName(user.getName());
	              list.add(obj);
			}
		}

		List<NoteResponseDto> notes = new ArrayList<>();
		for (Note note : list)
		{
			NoteResponseDto dto = new NoteResponseDto(note);
			dto.setLabels(noteDao.getLabelsByNote(note));// set all label to note
			
			List<CollaboratorResponseDto> collaborators = noteDao.getCollaboratorsByNote(dto.getNoteId());

			dto.setCollaborators(collaborators);

			notes.add(dto);
			
		}
		return notes;
	}
		
	

	
	@Override
	public void createLabel(Label label, int userId) {
		User user = new User();
		user.setId(userId);
		label.setUser(user);
		noteDao.saveLabel(label);

	}

	@Override
	public List<Label> getAllLabels(int userId) {

		List<Label> list = noteDao.getAllLabels(userId);

		List<Label> labels = new ArrayList<>();
		for (Label label : list) {
			labels.add(label);
		}
		return labels;
	}

	@Override
	public boolean updateLabel(Label label) {
		boolean status = noteDao.updateLabel(label);
		return status;
	}

	@Override
	public void deleteLabel(int labelId, int userId) {
		Label label = noteDao.getLabelById(labelId);
		if (label.getUser().getId() != userId) {
			throw new UnAuthorizedAccessUser();
		}
		noteDao.deleteLabel(labelId);
	}

	@Override
	public void addLabel(int noteId, int labelId) {
		
		NoteLabel noteLabel = new NoteLabel();
		noteLabel.setLabelId(labelId);
		noteLabel.setNoteId(noteId);
		noteDao.addLabel(noteLabel);

	}

	@Override
	public void deleteLabelFromNote(int noteId, int labelId) {
		NoteLabel noteLabel = new NoteLabel();
		noteLabel.setLabelId(labelId);
		noteLabel.setNoteId(noteId);
		noteDao.deleteLabelFromNote(noteLabel);

	}

	@Override
	public List<Label> getNoteLabels(int noteId) {
		List<Label> list = noteDao.getNoteLabels(noteId);

		List<Label> labels = new ArrayList<>();
		for (Label label : list) {

			labels.add(label);
		}
		return labels;

	}

	@Override
	public boolean isLabelExists(String labelTitle) {
		boolean status = noteDao.isLabelExists(labelTitle);
		return status;
	}

	
	@Override
	public int addCollaborator(String sharedUserId, int noteId, int userId)
	{

	Collaborator collaborator = new Collaborator();
	
	
	   User sharedUser=userDao.getUserByEmailId(sharedUserId);
            if (sharedUser != null) 
            {  
			   if (sharedUser.getId() == userId)
			   {
				return -1;
			   }
			   collaborator.setNoteId(noteId);
			   collaborator.setSharedUserId(sharedUser.getId());
			   noteDao.saveCollaborator(collaborator, userId);
			  return 1;
	     	}
		return 10;
		}


	public void removeCollaborator(String sharedUserId, int noteId, int userId) 
	{

		    User sharedUser=userDao.getUserByEmailId(sharedUserId);
		      int sharedId=sharedUser.getId();
		      collaborator.setSharedUserId(sharedId);
		      collaborator.setNoteId(noteId);
		      noteDao.removeCollaborator(collaborator);
		
	}

	@Override
	public List<NoteResponseDto> getLabeldNotes(int labelId) {
	     List<Note> list=noteDao.getLabeldNotes(labelId);
		
		List<NoteResponseDto> notes=new ArrayList<>();
	       for (Note note : list) 
	       {
	    		NoteResponseDto dto = new NoteResponseDto(note);
	    		dto.setLabels(noteDao.getLabelsByNote(note));// set all label to note
				
				List<CollaboratorResponseDto> collaborators = noteDao.getCollaboratorsByNote(dto.getNoteId());

				dto.setCollaborators(collaborators);
	    		notes.add(dto);
	         }
		
		return notes;
	}
}
