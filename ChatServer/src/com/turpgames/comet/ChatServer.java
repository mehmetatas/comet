package com.turpgames.comet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

/**
 * Servlet implementation class ChatServer
 */
@WebServlet("/Comet")
public class ChatServer extends HttpServlet implements CometProcessor {
	private static final long serialVersionUID = 1L;
	protected ArrayList<HttpServletResponse> connections = new ArrayList<HttpServletResponse>();
	protected MessageSender messageSender = null;

	@Override
	public void init() throws ServletException {
		messageSender = new MessageSender();
		Thread messageSenderThread = new Thread(messageSender, "MessageSender["
				+ getServletContext().getContextPath() + "]");
		messageSenderThread.setDaemon(true);
		messageSenderThread.start();
	}

	@Override
	public void destroy() {
		connections.clear();
		messageSender.stop();
		messageSender = null;
	}

	@Override
	public void event(CometEvent event) throws IOException, ServletException {
		HttpServletRequest request = event.getHttpServletRequest();
		HttpServletResponse response = event.getHttpServletResponse();
		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			log("Begin for session: " + request.getSession(true).getId());
			PrintWriter writer = response.getWriter();
			writer.println("Wellcome!");
			writer.flush();
			synchronized (connections) {
				connections.add(response);
			}
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			log("Error for session: " + request.getSession(true).getId());
			synchronized (connections) {
				connections.remove(response);
			}
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.END) {
			log("End for session: " + request.getSession(true).getId());
			synchronized (connections) {
				connections.remove(response);
			}
			PrintWriter writer = response.getWriter();
			writer.println("</body></html>");
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.READ) {
			String user = request.getParameter("user");
			String message = request.getParameter("message");
			messageSender.send(user, message);
		}
	}

	public class MessageSender implements Runnable {

		protected volatile boolean running = true;
		protected ArrayList<String> messages = new ArrayList<String>();

		public MessageSender() {
		}

		public void stop() {
			running = false;
		}

		/**
		 * Add message for sending.
		 */
		public void send(String user, String message) {
			synchronized (messages) {
				messages.add("[" + user + "]: " + message);
				messages.notify();
			}
		}

		@Override
		public void run() {
			while (running) {
				if (messages.size() == 0) {
					try {
						synchronized (messages) {
							messages.wait();
						}
					} catch (InterruptedException e) {
						// Ignore
					}
				}

				synchronized (connections) {
					String[] pendingMessages = null;
					synchronized (messages) {
						pendingMessages = messages.toArray(new String[0]);
						messages.clear();
					}
					// Send any pending message on all the open connections
					for (int i = 0; i < connections.size(); i++) {
						try {
							PrintWriter writer = connections.get(i).getWriter();
							for (int j = 0; j < pendingMessages.length; j++) {
								writer.println(pendingMessages[j] + "<br>");
							}
							writer.flush();
						} catch (IOException e) {
							log("IOExeption sending message", e);
						}
					}
				}
			}
		}
	}
}