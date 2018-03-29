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
		//set reminder null
		note.setReminder(null);
		
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
	public List<NoteResponseDto> getAllNotes(int userId) {
		
		List<Note> list = noteDao.getAllNotes(userId);

		List<NoteResponseDto> notes = new ArrayList<>();
		for (Note note : list) {
			NoteResponseDto dto = new NoteResponseDto(note);
			notes.add(dto);
		}
		return notes;
	}

}
