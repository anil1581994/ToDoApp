package com.bridgelabz.note.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteRequestDto;
import com.bridgelabz.note.model.NoteResponseDto;
import com.bridgelabz.note.model.UpdateNoteDto;
import com.bridgelabz.note.service.INoteService;
import com.bridgelabz.user.ResponseDTO.Response;

@RestController
@RequestMapping("/note")
public class NoteController {
	@Autowired
	private INoteService noteService;

	private static final Logger logger = Logger.getLogger(NoteController.class);

	@RequestMapping(value = "/createNote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNote(@RequestBody NoteRequestDto noteRequestDto,
			@RequestAttribute(name = "userId") int userId) {

		Response response = new Response();

		NoteResponseDto noteResponse = noteService.createNote(noteRequestDto, userId);
		response.setMsg("note save successfully");
		response.setStatus(200);

		logger.info(response.getMsg());

		return new ResponseEntity<NoteResponseDto>(noteResponse, HttpStatus.OK);

	}

	@RequestMapping(value = "/updateNote", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateNote(@RequestBody UpdateNoteDto updateNoteDto, HttpServletRequest request) {

		// int userId = TokenUtils.verifyToken(request.getHeader("Authorization"));
		int userId = (int) request.getAttribute("userId");
		Response response = new Response();

		try {

			noteService.updateNote(updateNoteDto);
			response.setMsg("note update successfully");
			response.setStatus(200);
			logger.info("note updated successFully");
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();

			response.setMsg("note is not update");
			response.setStatus(418);

			logger.error("note is not saved");

			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/getNote/{noteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getNote(@PathVariable("noteId") int noteId) {

		Response response = new Response();
		Note note = noteService.getNoteById(noteId);

		response.setMsg("note receieve successfully");
		response.setStatus(1);

		logger.info("note receieve successfully");

		return new ResponseEntity<Note>(note, HttpStatus.OK);

	}

	@RequestMapping(value = "/deleteNote/{noteId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteNote(@PathVariable("noteId") int noteId, HttpServletRequest request) {
		// int userId = TokenUtils.verifyToken(request.getHeader("Authorization"));

		int userId = (int) request.getAttribute("userId");
		noteService.deleteNote(noteId, userId);

		Response response = new Response();
		response.setMsg("note deleted successfully");
		response.setStatus(1);

		logger.info("note deleted successfully");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllNotes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<NoteResponseDto>> getAllNotes(HttpServletRequest request) {
		// int userId = TokenUtils.verifyToken(request.getHeader("Authorization"));
		int userId = (int) request.getAttribute("userId");
		Response response = new Response();
		List<NoteResponseDto> notes = noteService.getAllNotes(userId);
		response.setMsg("note receieve successfully");
		response.setStatus(1);

		logger.info("note receieve successfully");

		return new ResponseEntity<List<NoteResponseDto>>(notes, HttpStatus.OK);

	}
}