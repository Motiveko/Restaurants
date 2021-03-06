package com.motiveko.restaurants.applications;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.motiveko.restaurants.domains.MenuItem;
import com.motiveko.restaurants.domains.MenuItemDto;
import com.motiveko.restaurants.domains.MenuRepository;

@Service
public class MenuService {

	private MenuRepository menuRepository;
	
	public MenuService(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	@Transactional
	public boolean createMenuItems(List<MenuItemDto> resources,Long restaurantId)  {

		int menuCount = resources.size();
		for( MenuItemDto dto : resources) {
			MenuItem menuItem = menuRepository.save(MenuItem.builder()
										.restaurantId(restaurantId)
										.name(dto.getName())
										.price(dto.getPrice())
										.build());
			if( menuItem.getId() != null) menuCount--;
		}
		
		// TODO : 2. MenuItem에 Validation 관련 min(1000)등의 어노테이션이 제대로 작동하지 않는다. 4도들어가고 empty도 들어가고 난리났다.

		
		return menuCount==0;
	}
	
	
}
