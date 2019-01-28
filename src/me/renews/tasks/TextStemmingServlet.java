package me.renews.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.renews.data.PMF;
import me.renews.data.TextByUrl;
import me.renews.data.WordByUrl;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class TextStemmingServlet extends HttpServlet {
	private static final String ID = "id";

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
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

			Synset[] synsets = database.getSynsets(word);
			for (Synset synset : synsets) {
				String word2 = synsets[0].getWordForms()[0];
				if (word2.contains(" ")) {
					continue;
				}

				// 出現回数も記録する
				Integer intCount = hashCount.get(word2);
				if (intCount == null) {
					intCount = 0;
				}
				intCount++;
				hashCount.put(word2, intCount);
				break;
			}
		}

		try {
			Set<String> keys = hashCount.keySet();
			for (String key : keys) {
				Integer count = hashCount.get(key);
				// FIXME ratingを追加
				WordByUrl wordByUrl = new WordByUrl(textByUrl.getUrl(), key,
						count, 1);
				pm.makePersistent(wordByUrl);
			}
		} finally {
			pm.close();
		}

		// ALCでレベルをチェックする
		Queue queue = QueueFactory.getQueue("check-page-rank-queue");
		queue.add(withUrl("/tasks/check_page_rank").param("url", textByUrl.getUrl())
				.method(Method.POST));

		resp.getWriter().println("success!");
	}
}
