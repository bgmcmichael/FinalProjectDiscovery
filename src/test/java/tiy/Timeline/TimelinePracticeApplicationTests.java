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
			User user = new User("john", "doe", "john@doe.email");
			user = users.save(user);
			Event event1;
			OffsetDateTime date1;
			String zoneId = "GMT";
			ZoneOffset zoneOffset = ZoneOffset.of("GMT");
			ZoneId zoneId1 = ZoneId.of("GMT");
			LocalDateTime now = LocalDateTime.now();

			date1 = OffsetDateTime.of(now, zoneOffset);
			event1 = new Event("event1", date1, date1.plusHours(1), zoneId, "details1", user);
			ArrayList<Failable> eventList = testController.addEvent(event1);
			assertEquals(event1.name,((Event)eventList.get(0)).getName());

		}catch (Exception ex){

		}finally {
			events.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void getEventsTest() throws Exception {
		try {
			User user = new User("john", "doe", "john@doe.email");

			String zoneId = "GMT";
			ZoneId zoneId1 = ZoneId.of("GMT");
			ZoneOffset zoneOffset = ZoneOffset.of("GMT");
			OffsetDateTime date1, date2, date3;
			Event event1, event2, event3;
			LocalDateTime now = LocalDateTime.now();

			date1 = OffsetDateTime.of(now, zoneOffset);
			date2 = OffsetDateTime.of(now.plusHours(1), zoneOffset);
			date3 = OffsetDateTime.of(now.plusHours(2), zoneOffset);

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

}
