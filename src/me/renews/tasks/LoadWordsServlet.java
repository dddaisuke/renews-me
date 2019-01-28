package me.renews.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.renews.data.WordRank;

public class LoadWordsServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		PersistenceManagerFactory pmfInstance = JDOHelper
				.getPersistenceManagerFactory("transactions-optional");
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		FileInputStream fis = new FileInputStream(new File("./list.txt"));
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		int count = 0;
		String start = req.getParameter("start");
		if (start != null) {
			count = new Integer(start);
		}
		System.out.println("[start] " + count);
		for (int i = 0; i < count; i++) {
			br.readLine();
		}

		try {
			for (int i = 0; i < 10; i++) {
				String line = br.readLine();
				if (line == null) {
					System.err.println("break!!!");
					break;
				}
				String[] words = line.split("\t");
				String rank = words[0];
				String word = words[1];
				System.out.println(rank + " " + word);

				WordRank wordRank = new WordRank(Integer.parseInt(rank), word);
				pm.makePersistent(wordRank);
			}
		} finally {
			pm.close();
		}
		resp.getWriter().println("save success!");
	}
}
