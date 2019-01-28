package me.renews.tasks;

import java.io.IOException;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.renews.data.PMF;
import me.renews.data.PageRank;
import me.renews.data.WordByUrl;

public class CheckPageRankServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = req.getParameter("url");
		if (url == "") {
			return;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "SELECT FROM " + WordByUrl.class.getName()
				+ " WHERE url == " + url;
		List<WordByUrl> list = (List<WordByUrl>) pm.newQuery(query).execute();
		for (int i = 0; i < list.size(); i++) {
			WordByUrl wordByUrl = list.get(i);
			// FIXME アルゴリズムを考える
		}

		try {
			// FIXME pagerankをセットする
			PageRank pageRank = new PageRank(url, 1);
			pm.makePersistent(pageRank);
		} finally {
			pm.close();
		}

	}
}
