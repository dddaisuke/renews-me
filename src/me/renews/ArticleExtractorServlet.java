package me.renews;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.net.URL;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

import me.renews.data.TextByUrl;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class ArticleExtractorServlet extends HttpServlet {
	private static final String URL = "url";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		String strUrl = req.getParameter(URL);
		if (strUrl == "" || strUrl == null) {
			return;
		}
		System.out.println(strUrl);

		URL url = new URL(strUrl);
		try {
			String text = ArticleExtractor.INSTANCE.getText(url);
			PersistenceManagerFactory pmfInstance = JDOHelper
					.getPersistenceManagerFactory("transactions-optional");

			TextByUrl textByUrl = new TextByUrl(strUrl, new Text(text));

			PersistenceManager pm = pmfInstance.getPersistenceManager();
			try {
				pm.makePersistent(textByUrl);

				Queue queue = QueueFactory.getQueue("text-stemming-queue");
				System.out.println("ID = " + textByUrl.getId());
				queue.add(withUrl("/tasks/text_stemming")
						.param("id", textByUrl.getId().toString())
						.method(Method.POST)
						.header("Host",
								BackendServiceFactory.getBackendService()
										.getBackendAddress("small")));

				resp.getWriter().println("save success! " + textByUrl.getId());
			} finally {
				pm.close();
			}
		} catch (BoilerpipeProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
