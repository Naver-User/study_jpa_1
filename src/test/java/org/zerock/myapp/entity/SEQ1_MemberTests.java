package org.zerock.myapp.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
public class SEQ1_MemberTests {
	private EntityManagerFactory emf;
	
	
	@BeforeAll
	void beforeAll() {	// 1회성 전처리
		log.trace("beforeAll() invoked.");
		
		this.emf = Persistence.createEntityManagerFactory(PersistenceUnits.H2);
		
		assertNotNull(emf);
		log.info("\t+ this.emf: {}", this.emf);
	} // beforeAll
	
	
	@AfterAll
	void afterAll() {	// 1회성 후처리
		log.trace("afterAll() invoked.");
		
		assert this.emf != null;
		
		// 참고로, emf 는 자원객체이기는 하지만, 자바언어에서 말하는
		// 자원객체가 되려면 AutoCloseable 해야 하는데요. 이 emf는 그렇지 않기 때문에
		// try-with-resource block을 사용할 수 없습니다. 때문에, 직접 close 메소드를
		// 호출해 주셔야 합니다.
//		try (this.emf) {}	// XX
		
		this.emf.close();
	} // afterAll
	
	
//	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(1)
	@DisplayName("1. createTable")
	@Timeout(value=5L, unit=TimeUnit.SECONDS)
	void createTable() {
		log.trace("createTable() invoked.");
				
		
		@Cleanup EntityManager em = this.emf.createEntityManager();
		
		// 데이터베이스에 엔티티 클래스에 1:1 매핑된 테이블 생성시 잘 되었으면,
		// 이후 테스트를 위해, 반드시 영속성 설정파일의 ddl.auto 설정을 create -> update로
		// 변경하시기 바랍니다.
		
		Objects.requireNonNull(em);
		log.info("\t+ Done.");
	} // createTable
	
	
//	@Disabled
	@Order(2)
	@Test
//	@RepeatedTest(1)
	@DisplayName("2. createEntities")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void createEntities() {
		log.trace("createEntities() invoked.");
		
		@Cleanup EntityManager em = this.emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
//			em.getTransaction().begin();
			
			// 총 5개의 튜플 생성 => 엔티티 클래스로부터 총 5개의 인스턴스 생성,
			// EntityManager.persist(instance) 수행하여 테이블에 저장합니다.
			IntStream.rangeClosed(1, 5).forEach(seq -> {
				Member m = new Member();
				
				m.setId("ID_"+seq);
				m.setUserName("NAME_"+seq);
				m.setAge(seq);
				
				em.persist(m);	// 엔티티 인스턴스 저장(영속화) with Entity Manager
//				em.flush();
			});	// .forEach
		
			tx.commit();	// TX 이 정상 종료되면, 자동으로 em.flush() 가 호출되어
							// 최종적으로 모든 변경(테이블의 생성/변경과 CRUD)가 최종반영
//			em.getTransaction().commit();
		} catch(Exception _original) {
			tx.rollback();
//			em.getTransaction().rollback();
			
			throw new IllegalStateException(_original);
		} // try-catch
	} // createEntities
	
	
//	@Disabled
	@Order(3)
	@Test
//	@RepeatedTest(1)
	@DisplayName("3-1. readEntity1 : Using EntityManager.find method")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void readEntity1() {
		log.trace("readEntity1() invoked.");
		
		// JPA EntityManager 에는, CRUD 에 해당되는 메소드가 있는데요..
		// 유일하게, READ 메소드는 따로 제공하지 않습니다.
				
		@Cleanup EntityManager em = this.emf.createEntityManager();
		
		// ------------
		// 1st. method - EntityManager.find() 메소드
		// ------------
//		Member foundMember = em.<Member>find(Member.class, "ID_1");
//		assertNotNull(foundMember);
//		log.info("\t+ foundMember: {}", foundMember);
		
		IntStream.rangeClosed(1, 5).forEach(seq -> {
			Member foundMember = em.<Member>find(Member.class, "ID_"+seq);
			log.info("\t+ foundMember with ID_{}: {}", seq, foundMember);
		});	// .forEach
	} // readEntity1
	
	
//	@Disabled
	@Order(4)
	@Test
//	@RepeatedTest(1)
	@DisplayName("3-2. readEntity2 : Using EntityManager.find method")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void readEntity2() {
		log.trace("readEntity2() invoked.");
		
		// JPA EntityManager 에는, CRUD 에 해당되는 메소드가 있는데요..
		// 유일하게, READ 메소드는 따로 제공하지 않습니다.
				
		@Cleanup EntityManager em = this.emf.createEntityManager();

		// ------------
		// 2nd. method - JPQL를 이용하여 N개의 엔티티를 한번에 조회
		// ------------
		// * JPQL : Java Persistence Query Language
		//          실제 SQL문법과도 상당히 유사!!! -> 장점
		//          실제 SQL (JPA에서는, Native SQL이라고 부른다)
		// * JPQL의 장점인, SQL문법과 유사도입니다. 대신, 다른점은
		//   (1) Native SQL은 실제 테이블을 조회하는 언어라면,
		//   (2) JPQL은 엔티티를 대상으로 조회하는 언어입니다.
		
		// 아래에서 FROM 절에 나오는 Member는 테이블명이 아니라,
		// 엔티티 클래스명입니다!. 즉, 엔티티를 조회하는 것이죠.
		String jpql = "SELECT m FROM Member m ORDER BY m.id ASC";
		TypedQuery<Member> jpqlQuery = em.<Member>createQuery(jpql, Member.class);
		
		List<Member> list = jpqlQuery.getResultList();
		list.forEach(log::info);
	} // readEntity2
	
	
//	@Disabled
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Order(5)
	@Test
//	@RepeatedTest(1)
	@DisplayName("3-3. readEntities3 : Using Native SQL")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void readEntities3() {
		log.trace("readEntities3() invoked.");
		
		// JPA로 Native SQL(진짜 SQL문장)를 수행하는 방법
				
		@Cleanup EntityManager em = this.emf.createEntityManager();

		// ------------
		// 3rd. method - 진짜 SQL인 Native SQL를 이용하여 N개의 엔티티를 한번에 조회
		// ------------
//		em.createNativeQuery("진짜SQL문", 엔티티타입에대한Clazz객체);
		String sql = "SELECT * FROM member ORDER BY id DESC";
		Query nativeQuery = em.createNativeQuery(sql, Member.class);
		List list = nativeQuery.getResultList();
		list.forEach(log::info);
	} // readEntities3
	
	
//	@Disabled
	@Order(6)
	@Test
//	@RepeatedTest(1)
	@DisplayName("4. updateEntities")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void updateEntities() {
		log.trace("updateEntities() invoked.");
		
		@Cleanup EntityManager em = this.emf.createEntityManager();
		
		// JPA 에서는, 수정을 위한 메소드를 직접적으로 제공하지 않습니다.
		// 때문에,
		//   (1) 우선, find 메소드로 수정할 엔티티 인스턴스를 끌어올리고(= 찾아서 얻고)
		//   (2) (1)에서 얻은 엔티티 인스턴스의 Setter 메소드로 필드값 수정
		//   (3) 필드가 수정된 엔티티 인스턴스를 다시 persist 메소드로 저장하지 않습니다?
		//		 (***************)
		// 아니, 그럼 어떻게 수정된 엔티티를 저장할 수가 있습니까!???...
		// 그냥 저장안하셔도 수정됩니다!! (***)
		
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			
			final String idToFind = "ID_3";
			Member foundMemberWithID3 = em.<Member>find(Member.class, idToFind);
			
			Objects.requireNonNull(foundMemberWithID3);
			log.info("\t+ foundMemberWithID3 before update: {}", foundMemberWithID3);
			
			// 데이터 수정 - 강사의 실제 이름과 실제 나이로 수정
			foundMemberWithID3.setUserName("Yoseph");
			foundMemberWithID3.setAge(23);
			log.info("\t+ foundMemberWithID3 afer update: {}", foundMemberWithID3);
			
			tx.commit();
		} catch(Exception _origin) {
			tx.rollback();
			
			throw new IllegalStateException(_origin);
		} // try-catch
		
		// 수정된 엔티티를 다시 테이블에 저장해야 하는게 맞는거 같은데요...
		// 그냥 트랜잭션이 끝나면 됩니다!?????
		// H2 Console 에서 수정이 잘 되었는지 확인해 보세요.
	} // updateEntities
	
	
//	@Disabled
	@Order(7)
	@Test
//	@RepeatedTest(1)
	@DisplayName("5. removeEntities")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void removeEntities() {
		log.trace("removeEntities() invoked.");
		
		// 삭제처리는 상대적으로 쉽습니다.
		// 왜냐하면, EntityManager가 remove 메소드를 제공하기 때문입니다.
		// 물론, removeAll 같은, 전체를 한번에 삭제하는 메소드는 제공하지 않습니다.
		
		@Cleanup EntityManager em = this.emf.createEntityManager();
		
		try {
			em.getTransaction().begin();
			
			// 전체 회원을 모두 삭제하세요
			// JPA는 CRUD 중에, C(CREATE, INSERT) 빼놓고는,
			// 메모리(VM)에 엔티티 인스턴스를 쌓아놓고 하는게 아니기 때문에,
			// 특정 엔티티를 UPDATE OR REMOVE 하시고자 하실 때에는,
			// 수정 또는 삭제할 인스턴스를 조회(READ, find 메소드)를 통해서
			// 테이블로부터 우선 얻어낸 다음에 하는 것이랍니다.
			
			IntStream.rangeClosed(1, 5).forEach(seq -> {
				String pk = "ID_" + seq;
				Member foundMember = em.<Member>find(Member.class, pk);
				
				if(foundMember != null) {
					em.remove(foundMember);
				} // if
			});	// .forEach
			
			em.getTransaction().commit();
		} catch(Exception _origin) {
			em.getTransaction().rollback();
			
			throw new RuntimeException(_origin);
		} // try-catch		
	} // removeEntities
	
	
//	@Disabled
	@Order(8)
	@Test
//	@RepeatedTest(1)
	@DisplayName("6. testDetachEntitiy")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testDetachEntitiy() {
		log.trace("testDetachEntitiy() invoked.");
	
		// -- 1 --------------
		@Cleanup EntityManagerFactory emf = 
				Persistence.createEntityManagerFactory(PersistenceUnits.MYSQL);

		// -- 2 --------------
		Objects.requireNonNull(emf);
		@Cleanup EntityManager em = emf.createEntityManager();

		// -- 3 --------------
//		IntStream.of(1, 2, 3).forEachOrdered(seq -> {
//			log.trace("\t+ forEachOrdered({}) invoked.", seq);
//
//			// Step1. 테스트에 사용할 엔티티 생성
//			Member transientMember = new Member();
//			transientMember.setId("ID_"+seq);
//			transientMember.setUserName("NAME_"+seq);
//			transientMember.setAge(seq);
//			
//			// Step2. Persistence Context (= Entity Manager) 에 저장
//			//        (TRANSIENT -> MANAGED)
//			try {
//				em.getTransaction().begin();				
//				em.persist(transientMember);
//				em.getTransaction().commit();
//		
//				log.info("\t+ isContains ? {}", em.contains(transientMember));
//			} catch(Exception e) {
//				em.getTransaction().rollback();
//				throw e;
//			} // try-catch
//		});	// .forEachOrdered

		// -- 4 --------------
		final String id = "ID_3";
		Member foundMember = em.<Member>find(Member.class, id);
		
		assertNotNull(foundMember);
		log.info("\t+ foundMember: {}, isContains? {}", foundMember, em.contains(foundMember));

		// -- 5 --------------
		// MANAGED -> DETACHED
		// 번아웃된 사원을 강제로 휴직계(1년) 내서, 집으로 보내버리자!!
		// 하지만, 퇴사가 아니다. 언제든 휴직이 끝나거나, 중간이라도 복귀가 가능하다!.
		
		em.detach(foundMember);		// MANAGED -> DETACHED
		log.info("\t+ is contains after detached: {}", em.contains(foundMember));
		
		// -- 6 --------------
		// DETACHED 된 엔티티의 효과를 확인하자!!!
		// 휴직계 내고, 집으로 간 사원에게는, 회사(EntityManager)에서
		// 어떤 일이 생겨도, 내 알바 아니다!!!
		
		try {	// 엔티티에 대한 수정을 반영시켜보자!!!
			em.getTransaction().begin();
			
			foundMember.setAge(23);
			foundMember.setUserName("Yoseph");
			
			em.flush();
			em.getTransaction().commit();		// 엔티티의 수정이 결국DB반영되기를 희망?!!!
		} catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
		} // try-catch
		
		// 이건 마치, List 같은 컬렉션을 다 사용한 후에,
		// 모든 요소를 clear 하고 close 해주는것과 같다!
		em.clear(); 	
	} // testDetachEntitiy
	
	
//	@Disabled
	@Order(9)
	@Test
//	@RepeatedTest(1)
	@DisplayName("7. testMergeEntitiy")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testMergeEntitiy() {
		log.trace("testMergeEntitiy() invoked.");
	
		// -- 1 --------------
		@Cleanup EntityManagerFactory emf = 
				Persistence.createEntityManagerFactory(PersistenceUnits.MYSQL);

		// -- 2 --------------
		Objects.requireNonNull(emf);
		@Cleanup EntityManager em = emf.createEntityManager();

		// -- 3 --------------
		final String id = "ID_3";
		Member foundMember = em.<Member>find(Member.class, id);
		
		assertNotNull(foundMember);
		log.info("\t+ foundMember: {}, isContains? {}", foundMember, em.contains(foundMember));

		// -- 4 --------------
		// MANAGED -> DETACHED
		// 번아웃된 사원을 강제로 휴직계(1년) 내서, 집으로 보내버리자!!
		// 하지만, 퇴사가 아니다. 언제든 휴직이 끝나거나, 중간이라도 복귀가 가능하다!.
		
		em.detach(foundMember);		// MANAGED -> DETACHED
		log.info("\t+ is contains after detached: {}", em.contains(foundMember));
		
		// -- 5 --------------
		// DETACHED -> MANAGED
		
		Member mergedMember = em.merge(foundMember);		// DETACHED -> MANAGED 
		log.info("\t+ is contains after merged: {}", em.contains(mergedMember));
		

		// -- 6 --------------
		// MERGED 된 엔티티의 효과를 확인하자!!!
		// 다시 회사(EntityManager)로 복귀했으니, 회사의 관리를 받아야죠!
		
		try {	// 엔티티에 대한 수정을 반영시켜보자!!!
			em.getTransaction().begin();
			
			mergedMember.setAge(23);
			mergedMember.setUserName("Yoseph");
			
			em.getTransaction().commit();		// 엔티티의 수정이 결국DB반영되기를 희망?!!!
		} catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
		} // try-catch
		
		// 이건 마치, List 같은 컬렉션을 다 사용한 후에,
		// 모든 요소를 clear 하고 close 해주는것과 같다!
		em.clear(); 	
	} // testMergeEntitiy

} // end class

