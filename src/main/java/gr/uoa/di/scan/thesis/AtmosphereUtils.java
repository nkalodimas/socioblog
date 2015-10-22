package gr.uoa.di.scan.thesis;

import java.util.concurrent.CountDownLatch;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtmosphereUtils {

	private static final Logger logger = LoggerFactory.getLogger(AtmosphereUtils.class);

	public static void suspendResource(final AtmosphereResource atmosphereResource) {
		logger.info("suspendResource");
		
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		atmosphereResource.addEventListener(new AtmosphereResourceEventListenerAdapter() {

			@Override
			public void onSuspend(AtmosphereResourceEvent event) {
				logger.info("suspendResource:countdown()");
				countDownLatch.countDown();
				atmosphereResource.removeEventListener(this);
			}

			@Override
			public void onDisconnect(AtmosphereResourceEvent event) {
				logger.info("suspendResource:onDisconnect()");
				super.onDisconnect(event);
			}

			@Override
			public void onBroadcast(AtmosphereResourceEvent event) {
				logger.info("suspendResource:onBroadcast()");
				super.onBroadcast(event);
			}
		});

		if (AtmosphereResource.TRANSPORT.LONG_POLLING.equals(atmosphereResource.transport())) {
			atmosphereResource.resumeOnBroadcast(true).suspend(-1);
		} else {
			atmosphereResource.suspend(-1);
		}
		logger.info("suspendResource:latch_await()");

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			logger.error("Interrupted while trying to suspend resource {}", atmosphereResource);
		}
	}
}