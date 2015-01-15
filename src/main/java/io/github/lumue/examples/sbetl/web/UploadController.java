package io.github.lumue.examples.sbetl.web;

import java.io.BufferedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

	@Autowired
	Path fileUploadPath;

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				
				String filename = UUID.randomUUID().toString();
				Path targetPath = Files.createFile(fileUploadPath.resolve(filename + ".complete"));
				Path tempPath = Files.createFile(fileUploadPath.resolve(filename + ".part"));
				
				BufferedOutputStream stream =
						new BufferedOutputStream(Files.newOutputStream(targetPath));
				stream.write(bytes);
				stream.close();
				
				Files.move(tempPath, targetPath);

				return "success";
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return "success";
	}
}
