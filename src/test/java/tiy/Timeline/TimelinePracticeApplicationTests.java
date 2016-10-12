package tiy.Timeline;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimelinePracticeApplicationTests {

	@Autowired
	UserRepository users;

	@Autowired
	private RESTController testController;

	@Test
	public void contextLoads() {
	}

	@Test
	public void registerTest() throws Exception {
		User testUser = new User("john doe", "password");
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
		User testUser = new User("john doe", "password");
		User errorTestUser = new User("IdontExist", "password");
		Failable dbResponse = null;
		try {
			dbResponse = testController.register(testUser);
			dbResponse = testController.login(((User)dbResponse));
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

}
