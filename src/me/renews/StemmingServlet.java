package me.renews;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.renews.data.PMF;
import me.renews.data.TextByUrl;
import me.renews.data.WordByUrl;

import org.apache.commons.lang3.math.NumberUtils;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class StemmingServlet extends HttpServlet {
	private static final String ID = "id";
	private static final String WORD = "word";

	private static final Logger logger = Logger.getLogger(StemmingServlet.class
			.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		String id = req.getParameter(ID);
		if (id == "") {
			return;
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "SELECT FROM " + TextByUrl.class.getName()
				+ " WHERE id == " + id;
		List<TextByUrl> list = (List<TextByUrl>) pm.newQuery(query).execute();
		TextByUrl textByUrl = list.get(0);
		String text = textByUrl.getText().getValue();
		text = text.replaceAll("\\u00A0", " ");
		text = text.replaceAll("\\u2019", "'");
		text = text.replaceAll("\\u2014", " ");
		text = text.replaceAll("\\u201C", " ");
		text = text.replaceAll("\\u201D", " ");
		text = text.replaceAll("\\u2018", " ");
		text = text.replaceAll("\\u2026", " ");

		String[] words = text.split("[ ,\\.\\n()-:]");
		// String text = req.getParameter("text");
		// String[] words = text.split("[ ,\\.\\n()-:]");

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		HashMap<String, Integer> hashCount = new HashMap<String, Integer>();
		for (String word : words) {
			if (word == "") {
				continue;
			}
			if (word.startsWith("'") || word.endsWith("'")
					|| word.endsWith("?")) {
				continue;
			}
			if (NumberUtils.isDigits(word)) {
				continue;
			}
			// if(StringUtils.isAllUpperCase(word)) {
			// continue;
			// }

			logger.info("word = " + word);
			Synset[] synsets = database.getSynsets(word);
			for (Synset synset : synsets) {
				String word2 = synsets[0].getWordForms()[0];
				if (word2.contains(" ")) {
					continue;
				}
				// resp.getWriter().println(synsets[0].getWordForms()[0]);
				Integer count = hashCount.get(word);
				if (count == null) {
					count = 0;
				}
				count++;
				hashCount.put(word, count);
				break;
			}
		}
		for (String word : hashCount.keySet()) {
			WordByUrl wordByUrl = new WordByUrl(textByUrl.getUrl(), word,
					hashCount.get(word), 0);
			try {
				pm.makePersistent(wordByUrl);
			} finally {
				pm.close();
			}

		}
	}
}
