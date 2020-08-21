package com.motiveko.restaurants.interfaces;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
	
	@Test
	public void createRestaurant() throws Exception {
		
		String name = "까치식당";
		String desc = "당산동소재 식당";
		Double lat = 32.1234;
		Double lng = 33.1234;
		Integer category = 1;
		
		Restaurant mockRestaurant = Restaurant.builder()
										.name(name)
										.description(desc)
										.lat(lat).lng(lng)
										.category(category).build();
		
		given(restaurantService.createRestaurant(name, desc, lat, lng, category))
					.willReturn(mockRestaurant);
		
		mvc.perform(post("/createRestaurant")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\""+name+"\",\"description\":\""+desc+"\","
						+ "\"lat\":\""+lat+"\",\"lng\":\""+lng+"\","
						+ "\"category\":\""+category+"\"}"))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("restaurantId")));
		
		
	}


}
