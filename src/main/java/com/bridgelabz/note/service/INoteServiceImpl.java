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
		// set reminder null
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
		if (note.getUser().getId() != userId) {
			throw new UnAuthorizedAccessUser();
		}
		noteDao.deleteNote(noteId);
	}

	@Override
	public List<NoteResponseDto> getAllNotes(int userId)
	{

		
		List<Note> list = noteDao.getAllNotes(userId);//2391

		//User user = userDao.getUserById(userId);//sana
		//List<Collaborator> collaboratorList = noteDao.getCollaboratorBySharedId(user.getEmail());// get list of
		//get shared noteId and userid ie coolaborator object
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
		
	

	
	// label
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
			// Label dto = new Label(label);
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
		// get all labels by noteId

		// noteDao.
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

//	public int saveCollaborator(Collaborator collaborator, int userId) {
//
//		//User sharedUser = userDao.getUserByEmailId(collaborator.getSharedUserId());
//         User sharedUser=userDao.getUserById(collaborator.getSharedUserId());
//		if (sharedUser != null) 
	     //{  //if shared user null
//			if (sharedUser.getId() == userId)
	//      {//owner unable to share himself
//				return -1;
//			}
//			noteDao.saveCollaborator(collaborator, userId);//save collaborator
//			return 1;
//		}
//		return 10;
//
//	}

	@Override
	public void removeCollaborator(Collaborator collaborator,int userId) {
		if (collaborator.getUserId()!= userId) {
			throw new UnAuthorizedAccessUser();
		}
		         noteDao.removeCollaborator(collaborator);
	}

	@Override
	public int addCollaborator(String sharedUserId, int noteId, int userId)
	{

	Collaborator collaborator = new Collaborator();
	
	//collaborator.setUserId(userId);
	//User user=userDao.getUserByEmailId(sharedUserId);
	//collaborator.setSharedUserId(user.getId());
	
	//Note note = noteDao.getNoteById(noteId);
	
	//collaborator.setNoteId(noteId);
	
	//User sharedUser=userDao.getUserById(collaborator.getSharedUserId());
	   User sharedUser=userDao.getUserByEmailId(sharedUserId);
             if (sharedUser != null) 
            {  //if shared user null
			   if (sharedUser.getId() == userId)
			   {//owner unable to share himself
				return -1;
			   }
			   collaborator.setNoteId(noteId);
			   collaborator.setSharedUserId(sharedUser.getId());
			   noteDao.saveCollaborator(collaborator, userId);//save collaborator
			  return 1;
	     	}
		return 10;
		}
}
