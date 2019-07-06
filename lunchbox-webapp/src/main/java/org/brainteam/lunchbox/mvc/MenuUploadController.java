package org.brainteam.lunchbox.mvc;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.brainteam.lunchbox.core.AppContext;
import org.brainteam.lunchbox.core.Directories;
import org.brainteam.lunchbox.in.MenuParser;
import org.brainteam.lunchbox.in.OffersDefinition;
import org.brainteam.lunchbox.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MenuUploadController {
	
	@Autowired
	private MenuParser menuParser;

	@RequestMapping("/uploadmenu")
	@ResponseStatus(HttpStatus.OK)
	public void upload(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		parseMenuFromRequest(req);
		resp.getWriter().write("<script type=\"text/javascript\">window.parent.uploadComplete();</script>");
	}
	
	protected void parseMenuFromRequest(HttpServletRequest req) throws Exception {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		@SuppressWarnings("unchecked")
		List<FileItem> fileItems = upload.parseRequest(req);
		
		MenuParser parser = getParser();
		for (FileItem fileItem : fileItems) {
			if (!fileItem.isFormField()) {
				OffersDefinition result = processFile(parser, fileItem);
				OfferService offerService = AppContext.getBean(OfferService.class);
				offerService.importDefinition(result);
			}
		}
	}
	
	protected OffersDefinition processFile(MenuParser parser, FileItem fileItem) throws Exception {
		File tempFile = new File(AppContext.getBean(Directories.class).getTempDir(), fileItem.getName());
		FileUtils.forceDeleteOnExit(tempFile);
		fileItem.write(tempFile);
		return parser.parse(tempFile);
	}
	
	private MenuParser getParser() {
		return menuParser;
	}
	
}
