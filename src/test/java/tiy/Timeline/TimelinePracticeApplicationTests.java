package tiy.Timeline;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelinePracticeApplicationTests {

	@Autowired
	UserRepository users;

	@Autowired
	EventRepository events;

	@Autowired
	ContactRepository contacts;

	@Autowired
	private RESTController testController;

	@Test
	public void contextLoads() {
	}

	@Test
	public void registerTest() throws Exception {
		UserPlaceholder testUser = new UserPlaceholder("john doe", "password");
		Failable dbResponse = null;
		try {
			dbResponse = testController.register(testUser);
			dbResponse = users.findOne(((User)dbResponse).getId());

			assertNotNull(dbResponse);
			assertEquals("john doe", ((User)dbResponse).username);
		} catch (Exception ex){

		} finally {
			users.deleteAll();
		}
	}

	@Test
	public void loginTest() throws Exception {
		UserPlaceholder testUser = new UserPlaceholder("john doe", "password");
		UserPlaceholder errorTestUser = new UserPlaceholder("IdontExist", "password");
		Failable dbResponse = null;
		try {
			dbResponse = testController.register(testUser);
			dbResponse = testController.login(((UserPlaceholder) dbResponse));
			dbResponse = users.findOne(((User)dbResponse).getId());

			assertNotNull(dbResponse);
			assertEquals("john doe", ((User)dbResponse).username);

			dbResponse = null;
			dbResponse = testController.login(errorTestUser);

			assertNotNull(dbResponse);
			assertEquals("Invalid credentials, please try again",((Error)dbResponse).errorMessage);
		} catch (Exception ex){

		} finally {
			users.deleteAll();
		}
	}

	@Test
	public void addEventTest() throws Exception {
		try {
			User user = new User("john", "doe", "john@doe.com");
			user = users.save(user);
			UserPlaceholder userBox = new UserPlaceholder(user.username);
			EventPlaceholder event1;
			ZonedDateTime date1;
			String zoneId = "GMT";
			ZoneId zoneId1 = ZoneId.of("GMT");
			LocalDateTime now = LocalDateTime.now();

			date1 = ZonedDateTime.of(now, zoneId1);
			event1 = new EventPlaceholder("event1", "2016-05-06T02:15:00Z", "2016-05-06T03:15:00Z", zoneId, "details1", userBox);
			Failable dbEvent = testController.addEvent(event1);
			assertEquals(event1.name,((EventPlaceholder)dbEvent).getName());

		}catch (Exception ex){
			fail();
		}finally {
			events.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void eventsDbTest() throws Exception {
		try {
			User user = new User("john", "doe", "john@doe.email");

			String zoneId = "GMT";
			ZoneId zoneId1 = ZoneId.of("GMT");
			ZonedDateTime date1, date2, date3;
			Event event1, event2, event3;
			LocalDateTime now = LocalDateTime.now();

			date1 = ZonedDateTime.of(now, zoneId1);
			date2 = ZonedDateTime.of(now.plusHours(1), zoneId1);
			date3 = ZonedDateTime.of(now.plusHours(2), zoneId1);

			user = users.save(user);

			event1 = new Event("event1", date1, date1.plusHours(1), zoneId, "details1", user);
			event2 = new Event("event2", date2, date2.plusHours(1), zoneId, "details2", user);
			event3 = new Event("event3", date3, date3.plusHours(1), zoneId, "details3", user);

			events.save(event1);
			events.save(event3);
			events.save(event2);

			ArrayList<Event> unorderedArray = events.findByOwner(user);
			ArrayList<Event> dorderedArray = events.findByOwnerOrderByStartDateAsc(user);
			for (Event event : unorderedArray) {
				System.out.println(event.getName());
			}
			for (Event event : dorderedArray) {
				System.out.println(event.getName());
			}

		}catch (Exception ex){

		} finally {
			events.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void getEventsTest() throws Exception {
		try {
			User user = new User("john", "doe", "john@doe.email");
			UserPlaceholder userBox = new UserPlaceholder(user.username);

			String zoneId = "GMT";
			ZoneId zoneId1 = ZoneId.of("GMT");
			ZonedDateTime date1, date2, date3;
			Event event1, event2, event3;
			LocalDateTime now = LocalDateTime.now();

			date1 = ZonedDateTime.of(now, zoneId1);
			date2 = ZonedDateTime.of(now.plusHours(1), zoneId1);
			date3 = ZonedDateTime.of(now.plusHours(2), zoneId1);

			user = users.save(user);

			event1 = new Event("event1", date1, date1.plusHours(1), zoneId, "details1", user);
			event2 = new Event("event2", date2, date2.plusHours(1), zoneId, "details2", user);
			event3 = new Event("event3", date3, date3.plusHours(1), zoneId, "details3", user);

			events.save(event1);
			events.save(event3);
			events.save(event2);

			ArrayList<Failable> orderedArray = testController.getEvents(userBox);

			int eventCounter = 1;
			for (Failable event : orderedArray) {
				assertEquals(("event" + eventCounter), ((Event)event).name);
				eventCounter++;
			}

		}catch (Exception ex){

		} finally {
			events.deleteAll();
			users.deleteAll();
		}
	}

//	@Test
//	public void requestContactTest() {
//		try{
//			User user1 = new User("john", "doe", "john@doe.com");
//			User user2 = new User("Jane", "doe", "Jane@doe.com");
//
//			UserPlaceholder user1Box = new UserPlaceholder(user1.username);
//			UserPlaceholder user2Box = new UserPlaceholder(user2.username);
//
//			user1 = users.save(user1);
//			user2 = users.save(user2);
//
//			Contact testContact = new Contact(user1Box, user2Box);
//			Contact dbContact = contacts
//		}catch (Exception ex) {
//
//		}finally {
//
//		}
//	}

}
