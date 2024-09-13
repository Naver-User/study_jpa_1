package org.zerock.myapp.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.util.PersistenceUnits;

import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SEQ4_Member4Tests {
	private EntityManagerFactory emf;
	
	
	@BeforeAll
	void beforeAll() {	// 1회성 전처리
		log.trace("beforeAll() invoked.");
		
		this.emf = Persistence.createEntityManagerFactory(PersistenceUnits.ORACLE);		
		assertNotNull(emf);
	} // beforeAll
	
	
	@AfterAll
	void afterAll() {	// 1회성 후처리
		log.trace("afterAll() invoked.");
		
		assert this.emf != null;
		this.emf.close();
	} // afterAll
	
	
//	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(5)
	@DisplayName("1. createTuplesWithSequenceGenerator")
	@Timeout(value=1L, unit=TimeUnit.MINUTES)
	void createTuplesWithSequenceGenerator() {
		log.trace("createTuplesWithSequenceGenerator() invoked.");
		
		@Cleanup EntityManager em = this.emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			IntStream.rangeClosed(1, 5).forEach(seq -> {
				Member4 m = new Member4();
				
				m.setUserName("NAME_"+seq);
				m.setAge(seq);
				
				em.persist(m);
//				em.flush();
			});	// .forEach
		
			tx.commit();
		} catch(Exception _original) {
			tx.rollback();			
			throw new IllegalStateException(_original);
		} // try-catch
	} // createTuplesWithSequenceGenerator
	

} // end class

