package me.renews.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class PreLoadWordsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int countStart = 0;
		String start = req.getParameter("start");
		if (start != null) {
			countStart = new Integer(start);
		}

		Queue queue = QueueFactory.getQueue("load-word-list-queue");
		for (int count = 0; count < 10; count++) {
			String startCount = new Integer(count * countStart).toString();
			System.out.println("[PreLoadWord] " + startCount);
			queue.add(withUrl("/tasks/pre_load_words2").param("start",
					startCount).method(Method.GET));
		}
	}
}
