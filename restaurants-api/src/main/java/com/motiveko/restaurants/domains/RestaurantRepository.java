package com.motiveko.restaurants.domains;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long>{

	@Query(value="select x.*\n" + 
			"from (\n" + 
			"select \n" + 
			"	@rownum\\:=@rownum+1 as no, \n" + 
			"	a.* \n" + 
			"from restaurant a, (select @rownum\\:=0) b\n" + 
			"where \n" + 
			"	lat between :minLat AND :maxLat AND lng BETWEEN :minLng AND :maxLng\n" + 
			") x\n" + 
			"where x.no between :start and :end", nativeQuery = true
			)
	List<Restaurant> findAll(@Param("maxLat")Double maxLat, 
							@Param("minLat")Double minLat, 
							@Param("maxLng")Double maxLng, 
							@Param("minLng")Double minLng, 
							@Param("start")Integer start,
							@Param("end")Integer end);

	@Query( value="select count(*) from restaurant\n" + 
			"where lat between :minLat AND :maxLat AND lng BETWEEN :minLng AND :maxLng"
			, nativeQuery = true)
	Long count(@Param("minLat")Double minLat, 
			@Param("maxLat")Double maxLat, 
			@Param("minLng")Double minLng, 
			@Param("maxLng")Double maxLng);


}