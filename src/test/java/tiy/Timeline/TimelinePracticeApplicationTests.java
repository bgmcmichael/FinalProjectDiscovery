package tiy.Timeline;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.*;
import java.util.ArrayList;
import java.util.Collection;

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
			event1 = new EventPlaceholder("event1", "2016-05-06T02:15:00Z", "2016-05-06T03:15:00Z", zoneId, "details1", true, userBox);
			Failable dbEvent = testController.addEvent(event1);
			assertEquals(event1.name,((EventPlaceholder)dbEvent).getName());
			int dbId = ((EventPlaceholder) dbEvent).id;
			dbEvent = new EventPlaceholder("event2", "2016-05-06T03:15:00Z", "2016-05-06T04:15:00Z", zoneId, "details2", true, userBox);
			((EventPlaceholder)dbEvent).id = dbId;
			Failable dbEvent2 = testController.editEvent((EventPlaceholder)dbEvent);

			assertEquals(((EventPlaceholder) dbEvent).id, ((EventPlaceholder)dbEvent2).id);
			assertEquals("2016-05-06T03:15:00Z", ((EventPlaceholder) dbEvent2).startDate);
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

			event1 = new Event("event1", date1, date1.plusHours(1), zoneId, "details1", true, user);
			event2 = new Event("event2", date2, date2.plusHours(1), zoneId, "details2", true, user);
			event3 = new Event("event3", date3, date3.plusHours(1), zoneId, "details3", true, user);

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
			ZonedDateTime date1Start, date2Start, date3Start;
			ZonedDateTime date1End, date2End, date3End;
			Event event1, event2, event3;
			LocalDateTime now = LocalDateTime.now();

			date1Start = ZonedDateTime.of(now, zoneId1);
			date1End = ZonedDateTime.of(now.plusHours(1), zoneId1);
			date2Start = ZonedDateTime.of(now.plusHours(2), zoneId1);
			date2End = ZonedDateTime.of(now.plusHours(3), zoneId1);
			date3Start = ZonedDateTime.of(now.plusHours(4), zoneId1);
			date3End = ZonedDateTime.of(now.plusHours(5), zoneId1);

			user = users.save(user);

			event1 = new Event("event1", date1Start, date1End, zoneId, "details1", true, user);
			event2 = new Event("event2", date2Start, date2End, zoneId, "details2", true, user);
			event3 = new Event("event3", date3Start, date3End, zoneId, "details3", true, user);

			events.save(event1);
			events.save(event3);
			events.save(event2);

			ArrayList<Failable> orderedArray = testController.getEvents(userBox);

			int eventCounter = 1;
			boolean everyOtherFlag = true;
			for (Failable event : orderedArray) {
				if(everyOtherFlag) {
					assertEquals(("event" + eventCounter), ((EventPlaceholder) event).name);
					eventCounter++;
					everyOtherFlag = false;
				} else {
					assertEquals("timeblock", ((EventPlaceholder) event).name);
					everyOtherFlag = true;
				}
			}

		}catch (Exception ex){
			fail();
		} finally {
			events.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void mergeTimelinesTest() throws Exception {
		try {
			User user1 = new User("john", "doe", "john@doe.email");
			User user2 = new User("jane", "doe", "jane@doe.email");
			UserPlaceholder userBox1 = new UserPlaceholder(user1.username);
			UserPlaceholder userBox2 = new UserPlaceholder(user2.username);

			ArrayList<UserPlaceholder> userBoxList = new ArrayList<>();
			userBoxList.add(userBox1);
			userBoxList.add(userBox2);

			String zoneId = "GMT";
			ZoneId zoneId1 = ZoneId.of("GMT");
			ZonedDateTime date1Start, date2Start, date3Start, date4Start;
			ZonedDateTime date1End, date2End, date3End, date4End;
			Event event1, event2, event3, event4;
			LocalDateTime now = LocalDateTime.now();

			date1Start = ZonedDateTime.of(now, zoneId1);
			date1End = ZonedDateTime.of(now.plusHours(1), zoneId1);
			date2Start = ZonedDateTime.of(now.plusHours(2), zoneId1);
			date2End = ZonedDateTime.of(now.plusHours(3), zoneId1);
			date3Start = date2Start.plusMinutes(20);
			date3End = date2End.minusMinutes(20);
			date4Start = ZonedDateTime.of(now.plusHours(4), zoneId1);
			date4End = ZonedDateTime.of(now.plusHours(5), zoneId1);

			user1 = users.save(user1);
			user2 = users.save(user2);

			event1 = new Event("event1", date1Start, date1End, zoneId, "details1", true, user1);
			//assumed timeblock
			event2 = new Event("event2", date2Start, date2End, zoneId, "details2", true, user2);
			event3 = new Event("event3", date3Start, date3End, zoneId, "details3", true, user1);
			//assumed timeblock
			event4 = new Event("event4", date4Start, date4End, zoneId, "details4", true, user1);


			events.save(event4);
			events.save(event1);
			events.save(event3);
			events.save(event2);

			UserPlaceholderContainer container = new UserPlaceholderContainer();
			container.UserPlaceholderList = userBoxList;

			ArrayList<Failable> orderedArray = testController.mergeTimelines(userBoxList);

			int eventCounter = 1;
			boolean everyOtherFlag = true;
			for (Failable event : orderedArray) {
				if (((EventPlaceholder)event).name == "event2"){
					assertEquals(user2.username, ((EventPlaceholder) event).owner.username);
				}
				if(everyOtherFlag) {
					assertEquals(("event" + eventCounter), ((EventPlaceholder) event).name);
					eventCounter++;
					if (!((EventPlaceholder)event).name.equals("event2")){
						everyOtherFlag = false;
					}
				} else {
					assertEquals("timeblock", ((EventPlaceholder) event).name);
					everyOtherFlag = true;
				}
			}

		}catch (Exception ex){
			ex.printStackTrace();
			fail();
		} finally {
			events.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void getContactsTest() throws Exception {
		try {
			User user1 = new User("john", "doe", "john@doe.email");
			User user2 = new User("jane", "doe", "jane@doe.email");
			User user3 = new User("jack", "black", "jack@black.email");
			user1 = users.save(user1);
			user2 = users.save(user2);
			user3 = users.save(user3);
			Contact testContact1 = new Contact(user1, user2, true);
			Contact testContact2 = new Contact(user2, user1, true);
			Contact testContact3 = new Contact(user1, user3, false);

			contacts.save(testContact1);
			contacts.save(testContact2);
			contacts.save(testContact3);

			ArrayList<Failable> contactList = testController.getContacts(new UserPlaceholder(user1.username));
			assertEquals(1, contactList.size());
			assertEquals("jane", ((ContactPlaceholder) contactList.get(0)).receiver.username);
		}catch (Exception ex){
			ex.printStackTrace();
			fail();
		} finally {
			contacts.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void getContactRequestsTest() throws Exception {
		try {
			User user1 = new User("john", "doe", "john@doe.email");
			User user2 = new User("jane", "doe", "jane@doe.email");
			User user3 = new User("jack", "black", "jack@black.email");
			user1 = users.save(user1);
			user2 = users.save(user2);
			user3 = users.save(user3);
			Contact testContact1 = new Contact(user1, user2, true);
			Contact testContact2 = new Contact(user2, user1, true);
			Contact testContact3 = new Contact(user1, user3, false);

			contacts.save(testContact1);
			contacts.save(testContact2);
			contacts.save(testContact3);

			ArrayList<Failable> contactList = testController.getContactRequests(new UserPlaceholder(user3.username));
			assertEquals(1, contactList.size());
			assertEquals("jack", ((ContactPlaceholder) contactList.get(0)).receiver.username);
		}catch (Exception ex){
			ex.printStackTrace();
			fail();
		} finally {
			contacts.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void getPendingContactsTest() throws Exception {
		try {
			User user1 = new User("john", "doe", "john@doe.email");
			User user2 = new User("jane", "doe", "jane@doe.email");
			User user3 = new User("jack", "black", "jack@black.email");
			user1 = users.save(user1);
			user2 = users.save(user2);
			user3 = users.save(user3);
			Contact testContact1 = new Contact(user1, user2, true);
			Contact testContact2 = new Contact(user2, user1, true);
			Contact testContact3 = new Contact(user1, user3, false);

			contacts.save(testContact1);
			contacts.save(testContact2);
			contacts.save(testContact3);

			ArrayList<Failable> contactList = testController.getPendingContacts(new UserPlaceholder(user1.username));
			assertEquals(1, contactList.size());
			assertEquals("jack", ((ContactPlaceholder) contactList.get(0)).receiver.username);
		}catch (Exception ex){
			ex.printStackTrace();
			fail();
		} finally {
			contacts.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void requestContactTestPass() throws Exception {
		try {
			User user1 = new User("john", "doe", "john@doe.email");
			User user2 = new User("jane", "doe", "jane@doe.email");
			user1 = users.save(user1);
			user2 = users.save(user2);
			Contact testContact1 = new Contact(user1, user2);
			ContactPlaceholder contactBox = new ContactPlaceholder(testContact1);
			Failable message = testController.requestContact(contactBox);

			Iterable<Contact> temp = contacts.findAll();
			ArrayList<Contact> contactList = new ArrayList<Contact>();
			for (Contact contact : temp) {
				contactList.add(contact);
			}
			assertEquals("request sent", ((Error) message).errorMessage);
			assertEquals(1, contactList.size());
			assertEquals("john", contactList.get(0).sender.username);
		}catch (Exception ex){
			ex.printStackTrace();
			fail();
		}finally {
			contacts.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void requestContactTestFail() throws Exception {
		try {
			User user1 = new User("allyson", "fox", "allyson@fox.email");
			User user2 = new User("jane", "doe", "jane@doe.email");


			Contact testContact1 = new Contact(user1, user2);
			ContactPlaceholder contactBox = new ContactPlaceholder(testContact1);
			Failable message = testController.requestContact(contactBox);

			Iterable<Contact> temp = contacts.findAll();
			ArrayList<Contact> contactList = new ArrayList<Contact>();
			for (Contact contact : temp) {
				contactList.add(contact);
			}
			assertEquals(0, contactList.size());
			assertEquals("One or more Users could not be found", ((Error) message).errorMessage);

			user1 = users.save(user1);
			user2 = users.save(user2);
			testContact1 = new Contact(user1, user2, false);
			contactBox = new ContactPlaceholder(testContact1);
			contacts.save(testContact1);
			message = testController.requestContact(contactBox);

			temp = contacts.findAll();
			contactList = new ArrayList<Contact>();
			for (Contact contact : temp) {
				contactList.add(contact);
			}

			assertEquals(1, contactList.size());
			assertEquals("Request has already been sent", ((Error) message).errorMessage);
		}catch (Exception ex){
			ex.printStackTrace();
			fail();
		}finally {
			contacts.deleteAll();
			users.deleteAll();
		}
	}

	@Test
	public void confirmContactTest() throws Exception {
		try {
			User user1 = new User("john", "doe", "john@doe.email");
			User user2 = new User("jane", "doe", "jane@doe.email");

			user1 = users.save(user1);
			user2 = users.save(user2);
			Contact testContact1 = new Contact(user1, user2, false);
			ContactPlaceholder contactBox = new ContactPlaceholder(testContact1);

			testContact1 = contacts.save(testContact1);
			ContactPlaceholder accepted, declined;
			accepted = new ContactPlaceholder(testContact1.id, null, null, true);
			testController.confirmContact(accepted);

			Iterable<Contact> contactIterable = contacts.findAll();
			ArrayList<Contact> contactList = new ArrayList<>();
			for (Contact contact : contactIterable) {
				contactList.add(contact);
			}

			assertEquals(2, contactList.size());
			assertEquals("john", contactList.get(0).sender.username);
			assertEquals("jane", contactList.get(1).sender.username);

			contacts.deleteAll();

			testContact1 = new Contact(user1, user2, false);
			testContact1 = contacts.save(testContact1);
			declined = new ContactPlaceholder(testContact1.id, null, null, false);

			testController.confirmContact(declined);

			Iterable<Contact> contactIterable2 = contacts.findAll();
			ArrayList<Contact> contactList2 = new ArrayList<>();
			for (Contact contact : contactIterable2) {
				contactList2.add(contact);
			}

			assertEquals(0, contactList2.size());
		}catch (Exception ex){
			ex.printStackTrace();
			fail();
		}finally {
			contacts.deleteAll();
			users.deleteAll();
		}

	}
}
