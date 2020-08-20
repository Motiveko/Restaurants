package com.motiveko.restaurants.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private String email;
	
	@NotNull
	private String password;
	
	@NotNull
	private String name;
	
	private Integer level; // 1: 일반, 2: admin
	
	private Integer isDeleted; // 0: not deleted , 1: deleted

	public Object isActive() {
		return isDeleted==1;
	}
	
}
