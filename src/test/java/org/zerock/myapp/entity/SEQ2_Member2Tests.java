package org.zerock.myapp.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.util.PersistenceUnits;

import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//@Log4j2
@Slf4j
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SEQ2_Member2Tests {
	private EntityManagerFactory emf;
	
	
	@BeforeAll
	void beforeAll() {
		log.trace("beforeAll() invoked.");
		
		this.emf = Persistence.createEntityManagerFactory(PersistenceUnits.H2);
		Objects.requireNonNull(this.emf);
	} // beforeAll
	
	
	@AfterAll
	void afterAll() {
		log.trace("afterAll() invoked.");
		
		assert this.emf != null;
		this.emf.close();
	} // afterAll
	
	
//	@Disabled
	@Order(1)
//	@Test
	@RepeatedTest(1)
	@DisplayName("1. createTable")
	@Timeout(value=5L, unit=TimeUnit.SECONDS)
	void createTable() {
		log.trace("createTable() invoked.");
		
		@Cleanup EntityManager em = this.emf.createEntityManager();
		assertNotNull(em);
	} // createTable
	
	
//	@Disabled
	@Order(2)
//	@Test
	@RepeatedTest(1)
	@DisplayName("2. createTupleWithGenerationType")
	@Timeout(value=5L, unit=TimeUnit.SECONDS)
	void createTupleWithGenerationType() {
		log.trace("createTupleWithGenerationType() invoked.");
		
		@Cleanup EntityManager em = this.emf.createEntityManager();
		assertNotNull(em);
		
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			// 오직 한개의 엔티티 인스턴스만 생성하시되,
			// 키속성의 값은 지정하지 마시고 저장하세요!!
			
			Member2 newMember = new Member2();
			
			// 키 속성의 값을 제공해야 될 때와 제공하지 않아도 될 때를 아는게 중요합니다.
//			newMember.setId(1);	
			
			newMember.setName("Yoseph");
			newMember.setAge(23);
			
			// For H2 Database,
			// strategy = GenerationType.AUTO 		// OK, Using sequence
			// strategy = GenerationType.IDENTITY	// OK, Using identity like Oracle Identity
			// strategy = GenerationType.SEQUENCE	// OK, Using sequence
			// strategy = GenerationType.TABLE		// OK, ????
			em.persist(newMember);	// 저장
			
			tx.commit();
		} catch(Exception original) {
			tx.rollback();
			throw new RuntimeException(original);
		} // try-catch
	} // createTupleWithGenerationType
	
	
//	@Disabled
	@Order(3)
	@Test
//	@RepeatedTest(3)
	@DisplayName("3. createTupleWithSequenceGenerator")
	@Timeout(value=5L, unit=TimeUnit.SECONDS)
	void createTupleWithSequenceGenerator() {
		log.trace("createTupleWithSequenceGenerator() invoked.");
		
		@Cleanup EntityManager em = this.emf.createEntityManager();
		assertNotNull(em);
		
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			// 아래의 엔티티 클래스에 붙인 @SequenceGenerator + @GeneratedValue
			// 어노테이션에 대한 테스트 입니다.
			
//			IntStream.rangeClosed(1, 7).forEach(seq -> {
				
				Member2 m = new Member2();
				
				m.setName("Yoseph");
				m.setAge(23);

				em.persist(m);	// 저장
				
//			});	// .forEach
			
			tx.commit();
		} catch(Exception original) {
			tx.rollback();
			throw new RuntimeException(original);
		} // try-catch
	} // createTupleWithSequenceGenerator
	
	
	

} // end class
