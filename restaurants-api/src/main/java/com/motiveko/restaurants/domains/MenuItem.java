package com.motiveko.restaurants.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotEmpty
	private Long restaurantId;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	@Min(99)
	private Integer price;
	
}
