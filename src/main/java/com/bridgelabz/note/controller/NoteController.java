package com.bridgelabz.note.controller;

import java.io.IOException;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.note.model.Collaborator;
import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteRequestDto;
import com.bridgelabz.note.model.NoteResponseDto;
import com.bridgelabz.note.model.UpdateNoteDto;
import com.bridgelabz.note.service.INoteService;
import com.bridgelabz.user.ResponseDTO.Response;
import com.bridgelabz.user.model.User;
import com.bridgelabz.user.util.LinkScrapper;
import com.bridgelabz.user.util.UrlData;

/**
 * <p>
 * This is RestController class map the requestUrl to respected API
 * </p>
 * 
 * @author bridgeit
 *
 */
@RestController
@RequestMapping("/note")
public class NoteController {
	@Autowired
	private INoteService noteService;

	private static final Logger logger = Logger.getLogger(NoteController.class);

	/**
	 * <p>
	 * This API is used to create new note of particular user
	 * </p>
	 * 
	 * @param noteRequestDto
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/createNote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createNote(@RequestBody NoteRequestDto noteRequestDto,
			@RequestAttribute(name = "userId") int userId) {

		Response response = new Response();
		NoteResponseDto noteResponse = noteService.createNote(noteRequestDto, userId);
		
		response.setMsg("note created successfully");
		response.setStatus(200);
		logger.info(response.getMsg());
		
		return new ResponseEntity<NoteResponseDto>(noteResponse, HttpStatus.OK);

	}

	/**
	 * <p>
	 * This API is used to update note of particular noteId
	 * </p>
	 * 
	 * @param updateNoteDto
	 * @param request
	 * @return updated Note
	 */
	@RequestMapping(value = "/updateNote", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateNote(@RequestBody UpdateNoteDto updateNoteDto, HttpServletRequest request) {

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
			logger.error("note is not update");
			
			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}
	}

	/**
	 * <p>
	 * This API is used to get the Specific note on the basis of notId
	 * </p>
	 * 
	 * @param noteId
	 * @return
	 */
	@RequestMapping(value = "/getNote/{noteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getNote(@PathVariable("noteId") int noteId) {

		Response response = new Response();
		Note note = noteService.getNoteById(noteId);

		response.setMsg("note receieve successfully");
		response.setStatus(1);
		logger.info("note receieve successfully");

		return new ResponseEntity<Note>(note, HttpStatus.OK);

	}

	/**
	 * <p>
	 * This API is going to delete the particular note on this basis noteId
	 * </p>
	 * 
	 * @param noteId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteNote/{noteId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteNote(@PathVariable("noteId") int noteId, HttpServletRequest request) {

		int userId = (int) request.getAttribute("userId");
		Response response = new Response();
		noteService.deleteNote(noteId, userId);

		response.setMsg("note deleted successfully");
		response.setStatus(1);
		logger.info("note deleted successfully");
		
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	/**
	 * <p>
	 * This API is used get all Notes of particular user on the basis userId
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllNotes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<NoteResponseDto>> getAllNotes(HttpServletRequest request) {

		int userId = (int) request.getAttribute("userId");
		Response response = new Response();
		List<NoteResponseDto> notes = noteService.getAllNotes(userId);
		
		response.setMsg("notes receieve successfully");
		response.setStatus(1);
        logger.info("notes receieve successfully");

		return new ResponseEntity<List<NoteResponseDto>>(notes, HttpStatus.OK);

	}

	/**
	 * <p>
	 * This API is used to create a new Label
	 * </p>
	 * 
	 * @param label
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/createlabel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createLabel(@RequestBody Label label, @RequestAttribute(name = "userId") int userId) {

		Response response = new Response();
		
        noteService.createLabel(label, userId);
		response.setMsg("label created successfully");
		response.setStatus(200);
        logger.info(response.getMsg());

		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	/**
	 * <p>
	 * This is getAllLabels API is used get all the labels created by particular
	 * User
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllLabels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Label>> getAllLabels(HttpServletRequest request) {

		int userId = (int) request.getAttribute("userId");
		Response response = new Response();
		List<Label> labels = noteService.getAllLabels(userId);
		
		response.setMsg("labels received successfully");
		response.setStatus(1);
		logger.info("labels received succesfully");

		return new ResponseEntity<List<Label>>(labels, HttpStatus.OK);

	}

	/**
	 * <p>
	 * This API used update the label of particular labelId
	 * </p>
	 * 
	 * @param label
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateLabel", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateLabel(@RequestBody Label label, HttpServletRequest request) {

		int userId = (int) request.getAttribute("userId");
		Response response = new Response();

		try {
			noteService.updateLabel(label);
			response.setMsg("label update successfully");
			response.setStatus(200);
			logger.info("label updated successFully");
			
			return new ResponseEntity<Response>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			response.setMsg("label  is not update");
			response.setStatus(418);
			logger.error("label is not update");

			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}
	}

	/**
	 * <p>
	 * This API is used to delete the specific label on the basis labelId
	 * </p>
	 * 
	 * @param labelId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteLabel/{labelId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteLabel(@PathVariable("labelId") int labelId, HttpServletRequest request) {

		int userId = (int) request.getAttribute("userId");
		noteService.deleteLabel(labelId, userId);
		
		Response response = new Response();
		response.setMsg("label deleted successfully");
		response.setStatus(1);

		logger.info("label deleted successfully");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	/**
	 * <p>
	 * This dual request accepting the same API is used going to use for adding
	 * Label to particular note and removing Label from particular note
	 * </p>
	 * 
	 * @param noteId
	 * @param labelId
	 * @param operation
	 *            parameter is used to decide the addLabel and removeLabel operation
	 * @param request
	 */
	@RequestMapping(value = "/addLabelToNote/{noteId}/{labelId}/{operation}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addLabelToNote(@PathVariable("noteId") int noteId, @PathVariable("labelId") int labelId,
			@PathVariable("operation") String operation, HttpServletRequest request) {
		boolean operation1 = Boolean.valueOf(operation);
		if (operation1 == true) {
			noteService.addLabel(noteId, labelId);
			
			Response response = new Response();
			response.setMsg("note update with label");
			response.setStatus(1);

			logger.info("note update with label successfully");

		} else if (operation1 == false) {
			noteService.deleteLabelFromNote(noteId, labelId);
			
			Response response = new Response();
			response.setMsg("label deleted successfully");
			response.setStatus(1);
            logger.info("label deleted successfully");
		} else {
			logger.info("invalid api");
		}

	}

	/**
	 * <p>
	 * This API is used get the labels of particular note on this basis of noteId
	 * </p>
	 * 
	 * @param request
	 * @param noteId
	 * @return
	 */
	@RequestMapping(value = "/getNoteLabels/{noteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Label>> getAllLabels(HttpServletRequest request, @PathVariable("noteId") int noteId) {
		Response response = new Response();
		List<Label> labels = noteService.getNoteLabels(noteId);
		
		response.setMsg("labels received successfully");
		response.setStatus(1);
		logger.info("labels received succesfully");

		return new ResponseEntity<List<Label>>(labels, HttpStatus.OK);

	}

	/**
	 * <p>
	 * This API is used for Collaborator creation and to collaborate note of one
	 * user to another user
	 * <p>
	 * 
	 * @param sharedUserId
	 * @param noteId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addCollaborator", method = RequestMethod.POST)
	public ResponseEntity<?> createCollaborator(@RequestParam String sharedUserId, @RequestParam int noteId,
			HttpServletRequest request) {

		int userId = (int) request.getAttribute("userId");// get owner
		Response response = new Response();

		int status = noteService.addCollaborator(sharedUserId, noteId, userId);
		
		if (status == 1) {
			response.setMsg("collaborator created successfully");
			response.setStatus(1);
			
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} else if (status == -1) {
			response.setMsg("collaborator not created");
			response.setStatus(-1);
			
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} else {
			response.setMsg("Email does not exist in database");
			response.setStatus(10);

			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}

	}

	/**
	 * <p>
	 * This API is used to remove a particular collaborator on the basis of shared
	 * user emaiId
	 * </p>
	 * 
	 * @param sharedUserId
	 * @param noteId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/removeCollborator", method = RequestMethod.DELETE)
	public ResponseEntity<Void> removeCollaborator(@RequestParam String sharedUserId, @RequestParam int noteId,
			HttpServletRequest request) {

		int userId = (int) request.getAttribute("userId");
		try {

			noteService.removeCollaborator(sharedUserId, noteId, userId);
			
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
	}

	/**
	 * <p>
	 * This API used to Add the image on Note
	 * </p>
	 * 
	 * @param updateNoteDto
	 * @param req
	 * @param userId
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public void uploadImage(@RequestBody UpdateNoteDto updateNoteDto, HttpServletRequest req,
			@RequestAttribute(name = "userId") int userId) {
		
		noteService.updateNote(updateNoteDto);
		logger.info("image is uploaded suceessFully");

	}

	/**
	 * <p>
	 * This API is used to get Title,Image,UrlhostDomain of Article of requested url
	 * </p>
	 * 
	 * @param urls
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getUrls", method = RequestMethod.POST)
	public List<UrlData> getUrlInfo(@RequestBody List<String> urls, HttpServletRequest request) {

		LinkScrapper link = new LinkScrapper();
		UrlData urlData = null;
		List<UrlData> urlDatas = new ArrayList<>();

		for (String url : urls) {
			System.out.println(urls);
			try {
				urlData = link.getMetaData(url);
				urlDatas.add(urlData);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return urlDatas;
	}
	/**
	 * <p>
	 * This API is used get all Labeled Notes of particular labelId
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getlabeldnotes/{labelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<NoteResponseDto>> getLabeldNotes(@PathVariable("labelId") int labelId,HttpServletRequest request) {

        Response response = new Response();
   	    int userId = (int) request.getAttribute("userId");
		List<NoteResponseDto> notes = noteService.getLabeldNotes(labelId);
		
		response.setMsg("notes receieve successfully");
		response.setStatus(1);
        logger.info("notes receieve successfully");

		return new ResponseEntity<List<NoteResponseDto>>(notes, HttpStatus.OK);

	}

}
