package com.bridgelabz.user.util;

import java.io.IOException;


import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LinkScrapper {

	public UrlData getMetaData(String url) throws IOException 
	{

		String urlTitle=null;
		String urlImage=null;
       

		Document document = Jsoup.connect(url).get();

		Elements metaOgTitle = document.select("meta[property=og:title]");
		
				if (metaOgTitle != null) 
				{
					urlTitle = metaOgTitle.attr("content");
					System.out.println(urlTitle);

					if (urlTitle == null) 
					{
						urlTitle = document.title();
						System.out.println(urlTitle);
					}
				}
           
				Elements metaOgImage = document.select("meta[property=og:image]");

				if (metaOgImage != null) 
				{
					urlImage = metaOgImage.attr("content");	
					System.out.println(urlImage);
				}
				
				return new UrlData(urlTitle, urlImage);
		 
	}
}
