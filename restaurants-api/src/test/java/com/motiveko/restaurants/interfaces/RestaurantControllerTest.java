package com.motiveko.restaurants.interfaces;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.motiveko.restaurants.applications.RestaurantService;
import com.motiveko.restaurants.domains.Restaurant;


@RunWith(SpringRunner.class)
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	RestaurantService restaurantService;
	
	private MockHttpSession session = new MockHttpSession();
	
	private static final String userName = "motiveko";
	@Before
	public void setUp() {
		 session.setAttribute("sName", userName);
	}
	@Test
	public void createRestaurant() throws Exception {
		
		Long id = 5L;
		String name = "까치식당";
		String desc = "당산동소재 식당";
		String address= "영등포구 영신로 193";
		Double lat = 32.1234;
		Double lng = 33.1234;
		Integer category = 1;
		Integer startTime = 8;
		Integer endTime = 22;
		String holiday = "일요일";
		
		
		Restaurant mockRestaurant = Restaurant.builder()
										.id(id)
										.name(name)
										.description(desc)
										.lat(lat).lng(lng)
										.category(category)
										.startTime(startTime).endTime(endTime)
										.holiday(holiday).userName(userName)
										.build();
		
		given(restaurantService
				.createRestaurant(name, desc, address, 
									lat, lng, category, startTime,
									endTime, holiday,userName))
			.willReturn(mockRestaurant);
		
		System.out.println(mockRestaurant.getId());
		
		mvc.perform(post("/createRestaurant")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\""+name+"\",\"description\":\""+desc+"\","
						+ "\"lat\":\""+lat+"\",\"lng\":\""+lng+"\", \"address\":\""+address+"\","
						+ "\"category\":\""+category+"\",\"startTime\":"+startTime+","
						+ "\"endTime\":"+endTime+",\"holiday\":\""+holiday+"\"}"))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("restaurantId")));
	}
	
	@Test
	public void getRestaurantCountWithoutRestaurant() throws Exception {
		
		given(restaurantService.getRestaurantCount(any())).willReturn(0L);
		mvc.perform(get("/getRestaurantCount")
					.content("{\"maxLat\":37.5343707,\"minLat\":37.5071406,\"maxLng\":126.9110697,\"minLng\":126.8896121,\"page\":1}"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("FAILED")));
		
		verify(restaurantService).getRestaurantCount(any());
		
	}
	@Test
	public void getRestaurantCountWithRestaurant() throws Exception {
		
		given(restaurantService.getRestaurantCount(any())).willReturn(100L);
		mvc.perform(get("/getRestaurantCount")
					.content("{\"maxLat\":37.5343707,\"minLat\":37.5071406,\"maxLng\":126.9110697,\"minLng\":126.8896121,\"page\":1}"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("SUCCESS")));
		
		verify(restaurantService).getRestaurantCount(any());
		
	}
	
	@Test
	public void getRestaurantList() throws Exception {
		mvc.perform(get("/getRestaurantList")
					.contentType(MediaType.APPLICATION_JSON) // GET Mapping으로 RequestParam으루 받는거는없어두된다.
					.content("{\"maxLat\":37.5343707,\"minLat\":37.5071406,\"maxLng\":126.9110697,\"minLng\":126.8896121,\"page\":1}"))
			.andExpect(status().isOk());
		
		verify(restaurantService).getRestaurantList(any());
	}

	@Test // 레스토랑 아이디 없음
	public void getRestaurantWithInvalidId() throws Exception {
		given(restaurantService.getRestaurant(1L)).willReturn(null);
		mvc.perform(get("/getRestaurant/1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("FAILED")));
	}
	
	@Test // 레스토랑 아이디 없음
	public void getRestaurantWithValidId() throws Exception {
		given(restaurantService.getRestaurant(1L)).willReturn(new Restaurant());
		mvc.perform(get("/getRestaurant/1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("SUCCESS")));
	}

}
