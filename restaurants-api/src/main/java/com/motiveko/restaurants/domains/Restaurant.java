package com.motiveko.restaurants.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant {
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private String name;

	@NotNull
	private String description;
	
	// 위치정보
	@NotNull
	private Double lat;
	
	@NotNull
	private Double lng;
	
	@NotNull
	private Integer category;
	
	private Integer isDeleted; // 0: active, 1: deleted
	
	
	
	

}
