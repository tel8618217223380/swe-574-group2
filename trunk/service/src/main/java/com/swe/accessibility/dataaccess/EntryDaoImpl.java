package com.swe.accessibility.dataaccess;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swe.accessibility.domain.Entry;

@Component
public class EntryDaoImpl implements EntryDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public int insert(Entry entry) {
		
		return (Integer) sessionFactory.getCurrentSession().save(entry); 
	}

	@Override
	public void delete(Entry entry) {
		
		sessionFactory.getCurrentSession().delete(entry);

	}

	@Override
	public void update(Entry entry) {
	
		sessionFactory.getCurrentSession().update(entry);
	}

	@Override
	public Entry getById(int id) {
		
		return (Entry) sessionFactory.getCurrentSession().get(Entry.class, id);
	}

	@Override
	public List<Entry> get(BigDecimal coordX, BigDecimal coordY) {
		
		String queryStr = null;
		Query query = null;
		
		if (coordX == null && coordY != null){
			queryStr = "from Entry where coordY =:y";
			query = sessionFactory.getCurrentSession().createQuery(queryStr);
			query.setParameter("y", coordY);
		}
			
		if (coordX != null && coordY == null){
			queryStr = "from Entry where coordX = :x";
			query = sessionFactory.getCurrentSession().createQuery(queryStr);
			query.setParameter("x", coordX);
			
		}
			
		if (coordX != null && coordY != null){
			queryStr = "from Entry where coordX = :x and coordY =:y";
			query = sessionFactory.getCurrentSession().createQuery(queryStr);
			query.setParameter("x", coordX);
			query.setParameter("y", coordY);
		}
	    
		
		
		return query.list();
		
	}
	
	@Override
	public List<Entry> getAll() {
		
		Query query = sessionFactory.getCurrentSession().createQuery("from Entry");
		return query.list();
		
	}
	
	@Override
	public List<Entry> getEntriesByType(String id){
		
		String queryStr = "SELECT e.id,e.comment,e.coordX,e.coordY,e.upVoteCount,e.downVoteCount from ViolationType v join ReasonViolationType rv on rv.ViolationTypeId = v.id join Reason r on rv.ReasonId = r.id join EntryReason er on er.ReasonId = r.id join Entry e on er.EntryId = e.id " +
				"where v.id = :id";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
		query.setParameter("id", id);
		
		Iterator<Object[]> iter = query.list().iterator();
		System.out.println(query.list().size());
		List<Entry> entries = new ArrayList<Entry>();
		while (iter.hasNext()){
			Object[] arr = iter.next();
			Entry entr = new Entry();
			entr.setId((Integer) arr[0]);
			entr.setComment((String) arr[1]);
			entr.setCoordX((BigDecimal) arr[2]);
			entr.setCoordY((BigDecimal) arr[3]);
			entr.setUpVoteCount((Integer) arr[4]);
			entr.setDownVoteCount((Integer) arr[5]);
			
			entries.add(entr);
		}
		return entries;
	}
	
	@Override
	public List<Entry> getEntriesByCategory(String id){
		
		String queryStr = "SELECT e.id,e.comment,e.coordX,e.coordY,e.upVoteCount,e.downVoteCount from Reason r join EntryReason er on er.ReasonId = r.id join Entry e on er.EntryID = e.id  " +
				"where r.id = :id";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
		query.setParameter("id", id);
		Iterator<Object[]> iter = query.list().iterator();
		
		List<Entry> entries = new ArrayList<Entry>();
		while (iter.hasNext()){
			Object[] arr = iter.next();
			Entry entr = new Entry();
			entr.setId((Integer) arr[0]);
			entr.setComment((String) arr[1]);
			entr.setCoordX((BigDecimal) arr[2]);
			entr.setCoordY((BigDecimal) arr[3]);
			entr.setUpVoteCount((Integer) arr[4]);
			entr.setDownVoteCount((Integer) arr[5]);
			
			entries.add(entr);
		}
		return entries;
	}

}
