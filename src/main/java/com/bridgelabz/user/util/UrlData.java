package com.bridgelabz.user.util;

import org.springframework.stereotype.Component;

@Component
public class UrlData {
	

	private String urlTitle;
	
	private String urlImage;
	
	private String urlDomain;
	
	public UrlData() {
		// TODO Auto-generated constructor stub
		
	}
	
	public UrlData(String urlTitle,String urlImage, String urlDomain)
	{
		this.urlTitle=urlTitle;
		this.urlImage=urlImage;
		this.urlDomain = urlDomain;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getUrlDomain() {
		return urlDomain;
	}

	public void setUrlDomain(String urlDomain) {
		this.urlDomain = urlDomain;
	}
	

}
