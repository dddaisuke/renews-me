package me.renews;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class StemmingServlet extends HttpServlet {

	private static final String WORD = "word";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		String word = req.getParameter(WORD);
		if (word == "") {
			return;
		}
		WordNetDatabase database = WordNetDatabase.getFileInstance();

		Synset[] synsets = database.getSynsets(word);
		resp.getWriter().println(synsets[0].getWordForms()[0]);
	}
}
