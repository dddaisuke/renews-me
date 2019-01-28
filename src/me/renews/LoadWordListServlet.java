package me.renews;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class LoadWordListServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		Queue queue = QueueFactory.getQueue("load-word-list-queue");
		for (int count = 0; count < 10; count++) {
			String startCount = new Integer(count).toString();
			System.out.println("[LoadWordList] " + startCount);
			queue.add(withUrl("/tasks/pre_load_words").param("start",
					startCount).method(Method.GET));
		}
		resp.getWriter().println("Under delayed execution!");
	}
}
