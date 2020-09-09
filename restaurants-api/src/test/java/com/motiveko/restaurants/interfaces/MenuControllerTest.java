package com.motiveko.restaurants.interfaces;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.motiveko.restaurants.applications.MenuService;

@RunWith(SpringRunner.class)
@WebMvcTest(MenuController.class)
public class MenuControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MenuService menuService;
	
	// 올바른 메뉴 생성
	@Test
	public void createWithValidMenuItems() throws Exception {
//		[{"name":"국밥","price":"1000"},
//		 {"name":"김치","price":"4000"}]
		
		Long id = 1L;
		given(menuService.createMenuItems(any(),any())).willReturn(true);
		mvc.perform(post("/menuItems/"+id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("[{\"name\":\"국밥\",\"price\":\"1000\"}," + 
						"{\"name\":\"김치\",\"price\":\"4000\"}]"))
			.andExpect(status().isCreated());
	}
	
	public void createWithInvalidMenuItems() throws Exception {
		Long id = 1L;
		given(menuService.createMenuItems(any(),any())).willThrow(new SQLException());
		mvc.perform(post("/menuItems/"+id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("[{\"name\":\"국밥\",\"price\":\"1000\"}," + 
						"{\"name\":\"김치\",\"price\":\"4000\"}]"))
			.andExpect(status().isInternalServerError());
	}
	
}
