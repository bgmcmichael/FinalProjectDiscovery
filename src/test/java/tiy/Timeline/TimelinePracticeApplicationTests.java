package tiy.Timeline;

import org.junit.Test;
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
		try {
			testUser = testController.register(testUser);
			testUser = users.findOne(testUser.getId());

			assertNotNull(testUser);
			assertEquals("john doe", testUser.username);
		} catch (Exception ex){

		} finally {
			users.deleteAll();
		}
	}

}
