package me.renews;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TextStemmingServlet extends HttpServlet {
	private static final String TEXT = "text";

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		String text = req.getParameter(TEXT);
		if (text == "") {
			return;
		}
		String[] words = text.split("[ ,\\.]");
		for (String word : words) {
			System.out.println(word);
			resp.getWriter().println(word);
		}
	}
}
