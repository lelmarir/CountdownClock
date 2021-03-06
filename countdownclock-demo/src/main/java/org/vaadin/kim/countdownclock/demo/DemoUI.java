package org.vaadin.kim.countdownclock.demo;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.annotation.WebServlet;

import org.vaadin.kim.countdownclock.CountdownClock;
import org.vaadin.kim.countdownclock.CountdownClock.EndEventListener;
import org.vaadin.kim.countdownclock.client.ui.CountdownClockState.Direction;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class DemoUI extends UI {

	private static final long serialVersionUID = -2474563921376269949L;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		setContent(layout);

		Label label = new Label("Just specify the format of the count down and "
				+ "the date to which to count and you're set to go! " + "Here is an example:");
		layout.addComponent(label);

		layout.addComponent(new Label("One example:"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);
		cal.add(Calendar.HOUR, 2);
		CountdownClock clock2 = CountdownClock.createCountdownTo(cal.getTime(), "%m : %s.%ts");
		layout.addComponent(clock2);
		clock2.start();

		layout.addComponent(new Label("...or two.. :"));

		Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR) + 1, 0, 1, 0, 0, 0);
		CountdownClock clock1 = CountdownClock.createCountdownTo(c.getTime(),
				"<span style='font: bold 13px Arial; margin: 10px'>"
						+ "Time until new year: %d days, %h hours, %m minutes and %s seconds</span>");
		clock1.setHeight("40px");
		clock1.start();
		layout.addComponent(clock1);
		
		layout.addComponent(new Label("...or tre.. :"));

		CountdownClock clock4 = CountdownClock.createTimer(200, "%s.%ts");
		clock4.setTargetTime(10000L);
		clock4.setContinueAfterEnd(true);
		clock4.start();
		layout.addComponent(clock4);
		layout.addComponent(new Button("Reset", event -> {
			clock4.setTime(0);
		}));
		
		layout.addComponent(new Label("-------"));
		CountdownClock clock3 = CountdownClock.createCountdown(50000, "JS: %s %js{ %s / 2 }");
		clock3.start();
		layout.addComponent(clock3);

		final CountdownClock clock = new CountdownClock();
		Button button = new Button("Don't click on me", new ClickListener() {
			private static final long serialVersionUID = -3301865196296699922L;

			public void buttonClick(ClickEvent event) {
				event.getButton().setEnabled(false);
				Calendar c = Calendar.getInstance();
				c.add(Calendar.SECOND, 10);
				clock.setTime(10*1000);
				clock.setTargetTime(0L);
				clock.continueAfterEnd(true);
				clock.setFormat("<span style='font: bold 25px Arial; margin: 10px'>"
						+ "This page will self-destruct in %s.%ts seconds.</span>");

				clock.addEndEventListener(new EndEventListener() {
					public void countDownEnded(CountdownClock clock) {
						Notification.show(
								"Ok, implementing the page destruction was"
										+ " kinda hard, so could you please just imagine" + " it happening?",
								Notification.Type.ERROR_MESSAGE);
						clock.setFormat("<span style='font: bold 25px Arial; margin: 10px'>"
								+ "This page has self-destructed %s.%ts seconds ago.</span>");
					}
				});
				layout.addComponent(clock);
			}
		});
		layout.addComponent(button);
	}
}
