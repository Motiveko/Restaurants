package com.motiveko.restaurants.applications;

import java.util.HashMap;
import java.util.List;

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

	public List<Restaurant> getRestaurantList(HashMap<String, String> resources) {
		
		
		
		Double maxLat = Double.parseDouble(resources.get("maxLat"));
		Double minLat = Double.parseDouble(resources.get("minLat"));
		Double maxLng = Double.parseDouble(resources.get("maxLng"));
		Double minLng = Double.parseDouble(resources.get("minLng"));
		Integer page = Integer.parseInt(resources.get("page"));
		Integer start = (page-1)*10 + 1;
		Integer end = start + 9;

		List<Restaurant> restaurants = restaurantRepository.findAll(maxLat,minLat,maxLng,minLng,start, end);
		
		return restaurants;
	}

	
}
