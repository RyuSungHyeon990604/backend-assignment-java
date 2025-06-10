package com.example.beassignmentjava.doc;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocController {

	@GetMapping("/docs")
	public void redirectToDocs(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui/index.html");
	}
}
