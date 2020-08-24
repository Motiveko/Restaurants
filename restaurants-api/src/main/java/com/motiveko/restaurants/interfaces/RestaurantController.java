package com.motiveko.restaurants.interfaces;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.motiveko.restaurants.applications.RestaurantService;
import com.motiveko.restaurants.domains.Restaurant;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class RestaurantController {

	@Autowired
	RestaurantService restaurantService;
	
	// 생성
	@PostMapping("/createRestaurant")
	public ResponseEntity<?> createRestaurant( @RequestBody Restaurant resource) throws URISyntaxException, JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<>();
		
		String name = resource.getName();
		String desc = resource.getDescription();
		Double lat = resource.getLat();
		Double lng = resource.getLng();
		Integer category = resource.getCategory();
		
		Restaurant restaurant = restaurantService
							.createRestaurant(name, desc, lat, lng, category);
				
		modelMap.put("result", "SUCCESS");
		modelMap.put("restaurantId",restaurant.getId());
		
		return  ResponseEntity.status(201).body(mapper.writeValueAsString(modelMap));
	}
	
	// 조회(그냥), 조회(검색)
	@GetMapping("/getRestaurantList")
	public String getRestaurantList( @RequestParam HashMap<String, String> resources) throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		System.out.println(resources);
		
		List<Restaurant> list = restaurantService.getRestaurantList(resources);
		modelMap.put("list", list);
		
		System.out.println(mapper.writeValueAsString(modelMap));
		return mapper.writeValueAsString(modelMap);
	}
	// 삭제(deactive) 
	
	// 수정
	

	
	
	
	
}
