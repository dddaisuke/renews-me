package me.renews.tasks;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.renews.data.PMF;
import me.renews.data.TextByUrl;
import me.renews.data.WordByUrl;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class CheckWordRank extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String word = req.getParameter("word");

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(word);
		for (Synset synset : synsets) {
			String word2 = synsets[0].getWordForms()[0];
			if (word2.contains(" ")) {
				continue;
			}
			PersistenceManager pm = PMF.get().getPersistenceManager();
			String query = "SELECT FROM " + WordByUrl.class.getName()
					+ " WHERE word == " + word;
			List<TextByUrl> list = (List<TextByUrl>) pm.newQuery(query).execute();
			TextByUrl textByUrl = list.get(0);
			
			resp.getWriter().println(synsets[0].getWordForms()[0]);
		}
	}
}
