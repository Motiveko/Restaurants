package com.motiveko.restaurants.applications;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.motiveko.restaurants.domains.MenuItem;
import com.motiveko.restaurants.domains.MenuItemDto;
import com.motiveko.restaurants.domains.MenuRepository;

public class MenuServiceTest {

	private MenuService menuService;
	
	@Mock
	private MenuRepository menuRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		menuService = new MenuService(menuRepository);
	}

	@Test
	public void createWithValidMenuItems() throws Exception {
		MenuItem mockItem = MenuItem.builder()
									.id(1L)
									.restaurantId(1L)
									.name("국밥")
									.price(1000)
									.build();
		given(menuRepository.save(any())).willReturn(mockItem);
		
		List<MenuItemDto> resources = new ArrayList();
		MenuItemDto dto = MenuItemDto.builder()
									.name("국밥")
									.price(1234)
									.build();
		resources.add(dto);
		resources.add(dto);
		resources.add(dto);
		
		boolean res = menuService.createMenuItems(resources, any());
		assertThat(res,is(true));
	}
	
	@Test(expected = Exception.class)
	public void createWithInvalidMenuItems() throws Exception {
		given(menuRepository.save(any())).willThrow(new Exception());
		List<MenuItemDto> resources = new ArrayList();
		MenuItemDto dto = MenuItemDto.builder()
									.name("국밥")
									.price(1234)
									.build();
		resources.add(dto);
		resources.add(dto);
		resources.add(dto);		

		boolean res = menuService.createMenuItems(resources, any());
		assertThat(res,is(false));	
	}
	
	
}
