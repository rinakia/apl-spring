package com.example.demo.ineedlist.repository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.ineedlist.entity.Ineed;

@Repository
public interface IneedRepository extends JpaRepository<Ineed, Integer>{
	List<Ineed> findByTitleLike(String title);
	List<Ineed> findByImportance(Integer importance);
	List<Ineed> findByUrgency(Integer urgency);
	List<Ineed> findByDeadlineBetweenOrderByDeadlineAsc(Date from, Date to);
	List<Ineed> findByDeadlineGreaterThanEqualOrderByDeadlineAsc(Date from);
	List<Ineed> findByDeadlineLessThanEqualOrderByDeadlineAsc(Date to);
	List<Ineed> findByDone(String done);
}
