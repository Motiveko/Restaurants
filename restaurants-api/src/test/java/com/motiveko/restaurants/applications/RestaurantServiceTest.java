package com.motiveko.restaurants.applications;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.motiveko.restaurants.domains.Restaurant;
import com.motiveko.restaurants.domains.RestaurantRepository;

public class RestaurantServiceTest {

	RestaurantService restaurantService;
	
	@Mock
	RestaurantRepository restaurantRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		restaurantService = new RestaurantService(restaurantRepository);
	}
	
	@Test
	public void createRestaurant() {
		
		Long id = 1L;
		String name = "까치식당";
		String desc = "당산동소재 식당";
		String address = "영등포구 영신로 193";
		Double lat = 32.1234;
		Double lng = 33.1234;
		Integer category = 1;
		Integer startTime = 8;
		Integer endTime = 22;
		String holiday = "일요일";
		String userName = "motiveko";
		
		Restaurant mockRestaurant = Restaurant.builder()
										.id(id)
										.name(name)
										.description(desc)
										.address(address)
										.lat(lat).lng(lng)
										.category(category)
										.startTime(startTime).endTime(endTime)
										.holiday(holiday).userName(userName).build();
		
		given(restaurantRepository.save(any())).willReturn(mockRestaurant);
		
		Restaurant restaurant = restaurantService
									.createRestaurant(
										name, desc, address, lat, lng, category,
										startTime, endTime, holiday, userName);
		
		verify(restaurantRepository).save(any());
		assertThat(restaurant.getUserName(),is(userName));
		assertThat(restaurant.getLat(),is(lat));
	}
	
	
}
