package com.motiveko.restaurants.applications;

import org.springframework.stereotype.Service;

import com.motiveko.restaurants.domains.Restaurant;
import com.motiveko.restaurants.domains.RestaurantRepository;

@Service
public class RestaurantService {

	RestaurantRepository restaurantRepository;
	
	public RestaurantService(RestaurantRepository restaurantRepository){
		this.restaurantRepository = restaurantRepository;
	}

	public Restaurant createRestaurant(String name, String desc, Double lat, Double lng, Integer category) {
		
		Restaurant restaurant = Restaurant.builder()
									.name(name).description(desc)
									.lat(lat).lng(lng).category(category)
									.build();
		
		return restaurantRepository.save(restaurant);

	}

	
}
