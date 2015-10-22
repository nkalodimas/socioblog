package gr.uoa.di.scan.thesis;

import gr.uoa.di.scan.thesis.service.SocialUserService;
import gr.uoa.di.scan.thesis.social.FacebookUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.MetaBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;

@Controller
@RequestMapping("/chat")
public class AtmosphereController {

	private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>(10);
	private static final Logger logger = LoggerFactory.getLogger(AtmosphereController.class);

	@Autowired
	Gson gson;

	@Autowired
	SocialUserService socialUserService;

	@Autowired
	FacebookUtils fbUtils;
	
	public class ChatMessage {

		private String author;
		private String body;
		private long time;

		public ChatMessage(String author, String body, long time) {
			this.author = author;
			this.body = body;
			this.time = time;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}
		
		
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8;")
	@ResponseStatus(HttpStatus.OK)
	public void subscribe(final AtmosphereResource event) throws IOException {
		logger.info("Responding on endpoint request /chat/subscribe from " + event.uuid());
		AtmosphereUtils.suspendResource(event);
		BroadcasterFactory.getDefault().lookup("/welcome/" + event.uuid(), true).addAtmosphereResource(event);
		
		MetaBroadcaster mbc = MetaBroadcaster.getDefault();
		if (mbc == null) {
			return;
		}
		
		mbc.broadcastTo("/welcome/" + event.uuid(), gson.toJson(chatMessages));
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8;")
	@ResponseStatus(HttpStatus.OK)
	public void postSomething(final AtmosphereResource event,@RequestBody String message) throws IOException {
		System.out.println("Arrived post " + event.uuid() + " message: " + message);
		ChatMessage msg = gson.fromJson(message, ChatMessage.class);

		MetaBroadcaster mbc = MetaBroadcaster.getDefault();
		if (mbc == null) {
			return;
		}
		chatMessages.add(0, msg);
		mbc.broadcastTo("/welcome/*", gson.toJson(msg));
	}

}