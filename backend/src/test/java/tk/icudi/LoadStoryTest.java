package tk.icudi;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class LoadStoryTest extends AbstractDBTest {

	@Before
	public void provideDatabase() throws Exception {
		dropTable("players");
	}
	
	@Test
	public void shouldLoadStartlocationWhenNoDataIsPresent() throws Exception {
		String postBody = "uuid:1234567889";
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContent(postBody.getBytes());
		
		LoadStory loadStory = new LoadStory();
		loadStory.dataSource = getDataSource();
		PlayerData playerData = loadStory.loadStory(request).player;
		
		assertThat(playerData.location, is("/dorfeingang/index"));
	}
	
	@Test
	public void shouldLoadIntroductionTest() throws Exception {
		String postBody = "uuid:1234567889";
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContent(postBody.getBytes());
		
		LoadStory loadStory = new LoadStory();
		loadStory.dataSource = getDataSource();
		LocationData locationData = loadStory.loadStory(request).location;
		
		assertThat(locationData.markup, containsString("willkommen"));
	}
		
}
