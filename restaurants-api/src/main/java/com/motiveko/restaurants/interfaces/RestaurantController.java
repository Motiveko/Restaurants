package com.motiveko.restaurants.interfaces;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<?> createRestaurant( @RequestBody Restaurant resource,
												HttpSession session) throws URISyntaxException, JsonProcessingException {
		
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<>();
		String name = resource.getName();
		String desc = resource.getDescription();
		String address = resource.getAddress();
		Double lat = resource.getLat();
		Double lng = resource.getLng();
		Integer category = resource.getCategory();
		Integer startTime = resource.getStartTime();
		Integer endTime = resource.getEndTime();
		String holiday = resource.getHoliday();
		
		String userName = (String) session.getAttribute("sName");
		
		Restaurant restaurant = restaurantService
							.createRestaurant(name, desc, address, lat, lng, category, 
												startTime, endTime, holiday, userName);
				
		modelMap.put("result", "SUCCESS");
		modelMap.put("restaurantId",restaurant.getId());
		
		return  ResponseEntity.status(201).body(mapper.writeValueAsString(modelMap));
	}
	
	// 현재 범위 내 레스토랑 총 갯수
	@GetMapping("/getRestaurantCount")
	public String getRestaurantCount( @RequestParam HashMap<String, String> resources) throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<>();
		long totalCount = restaurantService.getRestaurantCount(resources);


		if( totalCount == 0 ) {
			// 레스토랑 없음
			modelMap.put("result", "FAILED");
		} else {
			long lastPage = (totalCount%10 ==0) ? totalCount/10 : (totalCount/10) +1;
			modelMap.put("result","SUCCESS");
			modelMap.put("lastPage",lastPage);
		}

		return mapper.writeValueAsString(modelMap);
	}
	
	// 조회(그냥), 조회(검색)
	@GetMapping("/getRestaurantList")
	public String getRestaurantList( @RequestParam HashMap<String, String> resources) throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		List<Restaurant> list = restaurantService.getRestaurantList(resources);
		modelMap.put("list", list);
		
		return mapper.writeValueAsString(modelMap);
	}

	// 상제 조회
	@GetMapping("/getRestaurant/{id}")
	public String getRestaurant(@PathVariable("id") Long restaurantId) throws JsonProcessingException {
		
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		Restaurant restaurant = restaurantService.getRestaurant(restaurantId);

		if(restaurant!=null) {
			modelMap.put("restaurant", restaurant);
			modelMap.put("result", "SUCCESS");
		} else {
			modelMap.put("result","FAILED");
		}
		
		return mapper.writeValueAsString(modelMap);
		
	}
	// 삭제(deactive) 
	
	
	
	// 수정
	

	
	
	
	
}
