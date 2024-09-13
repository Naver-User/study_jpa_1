package org.zerock.myapp.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;
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

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SEQ3_Member3Tests {
	private EntityManagerFactory emf;
	
	
	@BeforeAll
	void beforeAll() {
		log.trace("beforeAll() invoked.");
		
		this.emf = Persistence.createEntityManagerFactory(PersistenceUnits.H2);
		assertNotNull(this.emf);
		log.info("\t+ this.emf: {}", this.emf);
	} // beforeAll
	
	
	@AfterAll
	void afterAll() {
		log.trace("afterAll() invoked.");
		
		Objects.requireNonNull(this.emf);
		this.emf.close();
	} // afterAll
	
	
//	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(1)
	@DisplayName("1. createTable")
	@Timeout(value=1L, unit=TimeUnit.MINUTES)
	void createTable() {
		log.trace("createTable() invoked.");
		
		EntityManager em = this.emf.createEntityManager();
		
		Objects.requireNonNull(em);
		log.info("\t+ em: {}", em);
	} // createTable
	
	
//	@Disabled
	@Order(2)
	@Test
//	@RepeatedTest(1)
	@DisplayName("2. createTuple")
	@Timeout(value=1L, unit=TimeUnit.MINUTES)
	void createTuple() {
		log.trace("createTuple() invoked.");
		
		EntityManager em = this.emf.createEntityManager();		
		Objects.requireNonNull(em);
		
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			// 새로운 튜플(= 엔티티클래스의 인스턴스 생성)을 생성/저장(persist)
			Member3 m3 = new Member3();
			
			m3.setName("NAME");
			m3.setAge(23);
			m3.setWeight(57.9);
			
			// ***: @Column(nullable, insertable) 일때
			m3.setHeight(172.5);		
			
			em.persist(m3); 		// 테이블에 저장
			
			tx.commit();
		} catch(Exception _origin) {
			tx.rollback();
			
			throw new RuntimeException(_origin);
		} // try-catch
	} // createTuple
	
	
//	@Disabled
	@Order(3)
	@Test
//	@RepeatedTest(1)
	@DisplayName("3. findAndUpdate")
	@Timeout(value=1L, unit=TimeUnit.MINUTES)
	void findAndUpdate() {
		log.trace("findAndUpdate() invoked.");
		
		EntityManager em = this.emf.createEntityManager();		
		Objects.requireNonNull(em);
		
		EntityTransaction tx = em.getTransaction();
		
		Long id = 1L;
		Member3 foundMember = em.<Member3>find(Member3.class, id);
		
		assert foundMember != null;
		
		try {
			tx.begin();
			
			// 찾아낸 엔티티 인스턴스의 데이터 수정
			// 특히, isMale 이란 boolean 타입의 필드 수정시,
			// @Column(updateable) 속성값의 의미를 파악하라!!!
			foundMember.setIsMale(false);	// 남자 -> 여자로 변경
			
			// *참고: 찾아낸 엔티티의 수정만으로, 실제 수정발생
			//        JPA는 별도로 수정메소드를 제공하지 않는다!!!! (어제 여러번 강조)
						
			tx.commit();
		} catch(Exception _origin) {
			tx.rollback();
			
			throw new RuntimeException(_origin);
		} // try-catch
	} // findAndUpdate
	
	
//	@Disabled
	@Order(4)
	@Test
//	@RepeatedTest(1)
	@DisplayName("4. createUniqueMember")
	@Timeout(value=1L, unit=TimeUnit.MINUTES)
	void createUniqueMember() {
		log.trace("createUniqueMember() invoked.");
		
		// ------------------------
		// 이 단위 테스트를 수행하시기 전에, 영속성 설정파일의
		// ddl.auto 속성값을 "create"로 설정하시고 하세요!
		// ------------------------
		
		EntityManager em = this.emf.createEntityManager();		
		Objects.requireNonNull(em);
		
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			// 고유한 회원번호를 가지는 여러 회원을 저장합니다.
			IntStream.rangeClosed(1, 3).forEach(seqNo -> {
				Member3 m3 = new Member3();
				
				m3.setMemberNo(Long.valueOf(seqNo));
				m3.setAge(23);
				m3.setHeight(172.5);
				m3.setName("Yoseph");
				m3.setWeight(57.9);
				m3.setIsMale(true);
				
				em.persist(m3);
			});	// .forEach
			
			// 중복된 회원번호를 가지는 4번째 회원을 만들어 저장합니다.
			// 이때, memberNo 필드에는 @Column(unique) 속성으로,
			// UNIQUE 제약조건을 설정할지 말지를 설정하게 됩니다.
			Member3 m = new Member3();
			
			m.setMemberNo(3L);		// 3L은 이미 있는 회원의 번호(즉, 중복)
			m.setAge(23);
			m.setHeight(172.5);
			m.setName("Yoseph");
			m.setWeight(57.9);
			m.setIsMale(true);
			
			em.persist(m);
						
			tx.commit();
		} catch(Exception _origin) {
			tx.rollback();
			
			throw new RuntimeException(_origin);
		} // try-catch
	} // createUniqueMember
	
	

} // end class

