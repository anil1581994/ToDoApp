package com.bridgelabz.socialLogin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.note.service.INoteService;
import com.bridgelabz.user.service.UserService;

@RestController
public class FaceBookLoginController {
	@Autowired
	private FaceBookConnection fbConnection;

	@Autowired
	private UserService userService;

	@Autowired
	private INoteService noteService;

	@RequestMapping(value = "logInWithFaceBook", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void loginwithFB(HttpServletRequest req, HttpServletResponse res) {
		System.out.println("in fb controller");
		String fbUrl = fbConnection.getFbAuthUrl();

		try {
			res.sendRedirect(fbUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/facebookconnection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void connectwithFB(HttpServletRequest req, HttpServletResponse res) {
		System.out.println("Response come from fb");

	}
}
