package org.brainteam.lunchbox.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.brainteam.lunchbox.jmx.StatisticsDAOConfiguration;
import org.brainteam.lunchbox.util.Pair;
import org.brainteam.lunchbox.util.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class StatisticsDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private StatisticsDAOConfiguration configuration;
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getGreediestUser() {
		List<Pair<String,Long>> result = getGreediestUsers(1);
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Pair<String,Long>> getGreediestUsers(int maxResults) {
		String jql = "SELECT NEW org.brainteam.lunchbox.util.Pair(u.fullName, SUM(ord.times)) FROM Order ord " +
				"JOIN ord.orderer u JOIN ord.item oi GROUP BY u.fullName ORDER BY SUM(ord.times) DESC, u.fullName ASC";
		Query q = getEntityManager().createQuery(jql);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Pair<String,Long>> getPopularMeals(int maxResults) {
		String jql = "SELECT NEW org.brainteam.lunchbox.util.Pair(m.headline, SUM(ord.times)) FROM Order ord " +
				"JOIN ord.item oi JOIN oi.meal m GROUP BY m.headline ORDER BY SUM(ord.times) DESC, m.headline ASC";
		Query q = getEntityManager().createQuery(jql);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Pair<String,Long>> getPopularMeals(Long userId, int maxResults) {
		String jql = "SELECT NEW org.brainteam.lunchbox.util.Pair(m.headline, SUM(ord.times)) FROM Order ord " +
				"JOIN ord.item oi JOIN oi.meal m JOIN ord.orderer u WHERE u.id=:userId " + 
				"GROUP BY m.headline ORDER BY SUM(ord.times) DESC, m.headline ASC";
		Query q = getEntityManager().createQuery(jql);
		q.setParameter("userId", userId);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}
	
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getChineseTopUser() {
		return getTopUser(getConfiguration().getChineseSearchWords());
	}
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getFishTopUser() {
		return getTopUser(getConfiguration().getFishSearchWords());
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getGourmetTopUser() {
		StringBuilder jql = new StringBuilder();
		jql.append("SELECT NEW org.brainteam.lunchbox.util.Pair(u.fullName, SUM(ord.times)) ");
		jql.append("FROM Order ord JOIN ord.item oi JOIN ord.orderer u ");
		jql.append("WHERE oi.priceInCents>=:gourmetMinPrice ");
		jql.append("GROUP BY u.fullName ORDER BY SUM(ord.times) DESC, u.fullName ASC");
		Query q = getEntityManager().createQuery(jql.toString());
		q.setParameter("gourmetMinPrice", getConfiguration().getGourmetMinPrice());
		q.setMaxResults(1);
		List<?> result = q.getResultList();
		if (result.size() == 1) {
			return (Pair<String, Long>) result.get(0);
		}
		return null;
	}
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getItalianTopUser() {
		return getTopUser(getConfiguration().getItalianSearchWords());
	}
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getMamaTopUser() {
		return getTopUser(getConfiguration().getMamaSearchWords());
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getSaladTopUser() {
		StringBuilder jql = new StringBuilder();
		jql.append("SELECT NEW org.brainteam.lunchbox.util.Pair(u.fullName, SUM(ord.times)) ");
		jql.append("FROM Order ord JOIN ord.item oi JOIN ord.orderer u JOIN oi.meal m ");
		jql.append("WHERE m.salad=:salad ");
		jql.append("GROUP BY u.fullName ORDER BY SUM(ord.times) DESC, u.fullName ASC");
		Query q = getEntityManager().createQuery(jql.toString());
		q.setParameter("salad", Boolean.TRUE);
		q.setMaxResults(1);
		List<?> result = q.getResultList();
		if (result.size() == 1) {
			return (Pair<String, Long>) result.get(0);
		}
		return null;
	}
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getSchnitzelTopUser() {
		return getTopUser(getConfiguration().getSchnitzelSearchWords());
	}

	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getSwabianTopUser() {
		return getTopUser(getConfiguration().getSwabianSearchWords());
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@PreAuthorize("hasRole('user')")
	public Pair<String,Long> getVeggieTopUser() {
		StringBuilder jql = new StringBuilder();
		jql.append("SELECT NEW org.brainteam.lunchbox.util.Pair(u.fullName, SUM(ord.times)) ");
		jql.append("FROM Order ord JOIN ord.item oi JOIN ord.orderer u JOIN oi.meal m ");
		jql.append("WHERE m.veggie=:veggie ");
		jql.append("GROUP BY u.fullName ORDER BY SUM(ord.times) DESC, u.fullName ASC");
		Query q = getEntityManager().createQuery(jql.toString());
		q.setParameter("veggie", Boolean.TRUE);
		q.setMaxResults(1);
		List<?> result = q.getResultList();
		if (result.size() == 1) {
			return (Pair<String, Long>) result.get(0);
		}
		return null;
	}
	
	protected Pair<String,Long> getTopUser(String[] mealLikes) {
		List<Pair<String,Long>> result = getTopUserForMeals(mealLikes, 2);
		if (!result.isEmpty() && (result.size() == 1 || !result.get(0).getSecond().equals(result.get(1).getSecond()))) {
			return result.get(0);
		}
		return null;
	}
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Pair<String,Long>> getTopUserForMeals(String[] mealLikes, int maxResults) {
		if (mealLikes == null || mealLikes.length == 0) {
			throw new IllegalArgumentException("mealLikes may not be null or empty");
		}
		StringBuilder jql = new StringBuilder("SELECT NEW org.brainteam.lunchbox.util.Pair(u.fullName, SUM(ord.times)) " +
				"FROM Order ord JOIN ord.item oi JOIN oi.meal m JOIN ord.orderer u WHERE ");
		for (int i = 0; i < mealLikes.length; i++) {
			if (i > 0) {
				jql.append(" OR ");
			}
			jql.append(" m.headline LIKE ?");
			jql.append(i + 1);
			jql.append(" ");
		}
		jql.append("GROUP BY u.fullName ORDER BY SUM(ord.times) DESC, u.fullName ASC");
		Query q = getEntityManager().createQuery(jql.toString());
		for (int i = 0; i < mealLikes.length; i++) {
			q.setParameter(i + 1, "%" + mealLikes[i] + "%");
		}
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Triplet<Integer,Integer,Long>> getSalesByMonth(int maxResults) {
		String jql = "SELECT NEW org.brainteam.lunchbox.util.Triplet(EXTRACT(YEAR FROM off.date), EXTRACT(MONTH FROM off.date), (SUM(ord.times*oi.priceInCents)) / 100.0) " +
				"FROM Order ord JOIN ord.item oi JOIN oi.offer off WHERE off.locked=true GROUP BY EXTRACT(YEAR FROM off.date), EXTRACT(MONTH FROM off.date) " +
				"ORDER BY EXTRACT(YEAR FROM off.date) DESC, EXTRACT(MONTH FROM off.date)";
		Query q = getEntityManager().createQuery(jql);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Triplet<Integer,Integer,Long>> getSalesByMonth(Long userId, int maxResults) {
		String jql = "SELECT NEW org.brainteam.lunchbox.util.Triplet(EXTRACT(YEAR FROM off.date), EXTRACT(MONTH FROM off.date), (SUM(ord.times*oi.priceInCents)) / 100.0) " +
				"FROM Order ord JOIN ord.item oi JOIN oi.offer off JOIN ord.orderer u WHERE u.id=:userId AND off.locked=true GROUP BY EXTRACT(YEAR FROM off.date), EXTRACT(MONTH FROM off.date) " +
				"ORDER BY EXTRACT(YEAR FROM off.date) DESC, EXTRACT(MONTH FROM off.date)";
		Query q = getEntityManager().createQuery(jql);
		q.setMaxResults(maxResults);
		q.setParameter("userId", userId);
		return q.getResultList();
	}

	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Triplet<Integer,Integer,Long>> getTopSalesByMonth(int maxResults) {
		String jql = "SELECT NEW org.brainteam.lunchbox.util.Triplet(EXTRACT(YEAR FROM off.date), EXTRACT(MONTH FROM off.date), COUNT(ord)) " +
				"FROM Order ord JOIN ord.item oi JOIN oi.offer off WHERE off.locked=true GROUP BY EXTRACT(YEAR FROM off.date), EXTRACT(MONTH FROM off.date) " +
				"ORDER BY COUNT(ord) DESC, EXTRACT(YEAR FROM off.date) DESC, EXTRACT(MONTH FROM off.date) DESC";
		Query q = getEntityManager().createQuery(jql);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Pair<String,Long>> getOrderDistributionByName() {
		String jql = "SELECT NEW org.brainteam.lunchbox.util.Pair(oi.name,COUNT(ord)) " +
				"FROM Order ord JOIN ord.item oi GROUP BY oi.name ORDER BY COUNT(ord) DESC, oi.name";
		Query q = getEntityManager().createQuery(jql);
		return q.getResultList();
	}
	
	@Transactional
	@PreAuthorize("hasRole('user')")
	@SuppressWarnings("unchecked")
	public List<Pair<String,Long>> getOrderDistributionByName(Long userId) {
		String jql = "SELECT NEW org.brainteam.lunchbox.util.Pair(oi.name,COUNT(ord)) " +
				"FROM Order ord JOIN ord.item oi JOIN ord.orderer u WHERE u.id=:userId " +
				"GROUP BY oi.name ORDER BY COUNT(ord) DESC, oi.name";
		Query q = getEntityManager().createQuery(jql);
		q.setParameter("userId", userId);
		return q.getResultList();
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public StatisticsDAOConfiguration getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(StatisticsDAOConfiguration configuration) {
		this.configuration = configuration;
	}

}
