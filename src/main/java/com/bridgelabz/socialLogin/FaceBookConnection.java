package com.bridgelabz.socialLogin;

import java.io.UnsupportedEncodingException;import java.net.URLEncoder;
import org.springframework.stereotype.Component;

@Component
public class FaceBookConnection {

	public static final String applicationId = "188389985217960";
	public static final String secretId = "2ae50e0df2f0ed669b23c14f404c66d0";
	public static final String redirectUrl = "http://localhost:8080/ToDo/facebookconnection";

	public String getFbAuthUrl() {
		String fbLoginUrl = "";
		try {

			fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id=" + applicationId + "&redirect_uri="
					+ URLEncoder.encode(redirectUrl, "UTF-8") + "&state=123&response_type=code"
					+ "&scope=public_profile,email";

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return fbLoginUrl;
	}

}
