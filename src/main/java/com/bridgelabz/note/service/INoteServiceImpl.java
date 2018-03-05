package com.bridgelabz.note.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.exceptions.UnAuthorizedAccessUser;
import com.bridgelabz.note.controller.NoteController;
import com.bridgelabz.note.dao.INoteDao;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteRequestDto;
import com.bridgelabz.note.model.NoteResponseDto;
import com.bridgelabz.note.model.UpdateNoteDto;
import com.bridgelabz.user.dao.UserDao;
import com.bridgelabz.user.model.User;

@Service
public class INoteServiceImpl implements INoteService {
	private static final Logger logger = Logger.getLogger(INoteServiceImpl.class);
	@Autowired
	INoteDao noteDao;
	@Autowired
	UserDao userDao;

	@Override
	public NoteResponseDto createNote(NoteRequestDto noteRequestDto, int userId) {
		User user = userDao.getUserById(userId);
		if (user != null) {
			Date date = new Date();// util
			// Date date=new java.sql.Date(dateObj.getTime());
			Note note = new Note(noteRequestDto);
			note.setCreateDate(date);
			note.setLastUpdateDate(date);
			note.setUser(user);
			noteDao.saveNote(note);
			NoteResponseDto noteResponseDto = new NoteResponseDto(note);
			return noteResponseDto;
		}
		return null;
	}

	@Override
	public boolean updateNote(UpdateNoteDto updateNoteDto) {
		Note note = new Note(updateNoteDto);
		Date date = new Date();// util
		note.setLastUpdateDate(date);
		boolean status = noteDao.updateNote(note);
		return status;
	}

	@Override
	public Note getNoteById(int noteId) {
		Note note = noteDao.getNoteById(noteId);
		return note;
	}

	/*@Override
	public boolean deleteNote(int noteId,int userId) {
		boolean validCreator=false;
		int creatorId=noteCreatorByNoteId(noteId);
		logger.info("note creator id:"+creatorId);
		logger.info(" deleter userId :"+userId);
		if (creatorId==userId) {
			validCreator = noteDao.deleteNote(noteId);
		}else
		{
			throw new UnAuthorizedAccessUser();
		}
		//boolean status = noteDao.deleteNote(noteId);
		return validCreator;
	}*/
	
	@Override
	public void deleteNote(int noteId,int userId) {
		Note note = noteDao.getNoteById(noteId);
		if (note.getUser().getId() != userId) {
			throw new UnAuthorizedAccessUser();
		}
		noteDao.deleteNote(noteId);
	}

	@Override
	public List<NoteResponseDto> getAllNotes(int userId) {
		User dbUser = userDao.getUserById(userId);//users from db
		if (dbUser.getId() != userId) {
			throw new UnAuthorizedAccessUser();
		}
		List<Note> list = noteDao.getAllNotes(userId);

		List<NoteResponseDto> notes = new ArrayList<>();
		for (Note note : list) {
			NoteResponseDto dto = new NoteResponseDto(note);
			notes.add(dto);
		}
		return notes;
	}

	@Override
	public int noteCreatorByNoteId(int noteId) {
		int creatorId=noteDao.noteCreatorByNoteId(noteId);
		return creatorId;
	}

	
}
