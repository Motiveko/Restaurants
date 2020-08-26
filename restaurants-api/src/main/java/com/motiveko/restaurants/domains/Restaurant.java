package com.motiveko.restaurants.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;

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
	
	@NotEmpty
	private String name;

	@NotEmpty
	private String description;
	
	// 위치정보
	@NotEmpty
	private String address;
	
	@NotEmpty
	private Double lat;
	
	@NotEmpty
	private Double lng;
	
	@NotEmpty
	private Integer category;
	
	@NotEmpty
	private Integer startTime;
	
	@NotEmpty
	private Integer endTime;
	
	@NotEmpty
	private String userName;
	
	private String holiday;
	private Integer isDeleted; // 0: active, 1: deleted
	

	// businessHour, holiday, userName 추가해야함
	
	

}
