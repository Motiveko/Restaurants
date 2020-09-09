package com.motiveko.restaurants.interfaces;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.motiveko.restaurants.applications.MenuService;
import com.motiveko.restaurants.domains.MenuItem;
import com.motiveko.restaurants.domains.MenuItemDto;

@RestController
public class MenuController {

	private MenuService menuService;
	
	public MenuController(MenuService menuService) {
		this.menuService = menuService;
	}
	
	// 메뉴 등
	@PostMapping("/menuItems/{id}")
	public ResponseEntity<?> createMenuItems(@PathVariable Long id,
										@RequestBody List<MenuItemDto> resources) throws URISyntaxException{
		// front에서 [ {},{}] 형태로 넘겨주면 된다. 
		System.out.println(resources);
		
		try {
			boolean isCreated = menuService.createMenuItems(resources,id);
		} catch (Throwable e) {
			return ResponseEntity.status(500).body("메뉴 등록에 실패하였습니다.");
		}
		
		
		return ResponseEntity.created(new URI("url")).body("");
	}
	
	
	@GetMapping("/menuItems/{id}")
	public ResponseEntity<?> getMenuItems(){
		
		return ResponseEntity.ok("");
	}
	
	
	
}
