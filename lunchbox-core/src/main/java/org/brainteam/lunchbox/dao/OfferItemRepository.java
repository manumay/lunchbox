package org.brainteam.lunchbox.dao;

import java.util.Date;

import org.brainteam.lunchbox.domain.OfferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OfferItemRepository extends JpaRepository<OfferItem, Long> {

	@Query("SELECT oi FROM OfferItem oi "
		+ "JOIN oi.meal m JOIN oi.offer o "
		+ "WHERE o.date>=:from AND o.date<:till AND m.id=:mealId")
	OfferItem findByDateAndMealId(@Param("from") Date from, @Param("till") Date till, @Param("mealId") Long mealId);	
	
	@Query("SELECT COUNT(oi) FROM OfferItem oi "
			+ "JOIN oi.meal m "
			+ "WHERE m.id=:mealId")
	Long findOfferItemCount(@Param("mealId") Long mealId);
	
}
