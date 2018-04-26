package com.bridgelabz.user.util;

import org.springframework.stereotype.Component;

@Component
public class UrlData {
	

	private String urlTitle;
	
	private String ulrImage;
	
	private String urlDomain;
	
	public UrlData() {
		// TODO Auto-generated constructor stub
	}
	
	public UrlData(String urlTitle,String urlImage, String urlDomain)
	{
		this.urlTitle=urlTitle;
		this.ulrImage=urlImage;
		this.urlDomain = urlDomain;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public String getUlrImage() {
		return ulrImage;
	}

	public void setUlrImage(String ulrImage) {
		this.ulrImage = ulrImage;
	}

	public String getUrlDomain() {
		return urlDomain;
	}

	public void setUrlDomain(String urlDomain) {
		this.urlDomain = urlDomain;
	}
	

}
