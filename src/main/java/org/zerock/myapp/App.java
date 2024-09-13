package org.zerock.myapp;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.zerock.myapp.entity.Member4;
import org.zerock.myapp.util.PersistenceUnits;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;


@Log4j2
//@Slf42
public class App {

	
	public static void main(String[] args) {
		log.trace("main({}) invoked.", Arrays.toString(args));

		// -- 엔티티 클래스의 매핑정보대로, DB에 테이블을 어떻게 누가 생성해주는걸까?
		
		// Step1. 엔티티관리자를 생성할 수 있는 공장객체를 획득
		@Cleanup EntityManagerFactory emf =	// 자원객체 
			Persistence.createEntityManagerFactory(PersistenceUnits.ORACLE);
		
		// Step2. Step1 에서 얻어낸 공장(factory)으로부터, 엔티티관리자 획득
		@Cleanup EntityManager em = emf.createEntityManager();	// 자원객체
		
		// Step3. Step2 에서 얻어낸 엔티티관리자로부터 트랜잭션관리 객체를 획득
		EntityTransaction tx = em.getTransaction();	
		
		// Step4. Step3 에서 얻어낸 엔티티 트랜잭션을 통해,
		//        트랜잭션을 시작합니다.
		tx.begin();			// 트랜잭션 시작
		
		App.businessLogic(em);
		
		// StepN. Step4 에서 시작한 트랜잭션을 종료
		tx.commit();		// 트랜잭션 종료 with Commit
//		tx.rollback();		// 트랜잭션 종료 with Rollback		
	} // main
	
	// 테스트용으로, 수동/자동이든 생성된 테이블에 데이터(튜플)를 CRUD 해보기 위한 메소드
	static void businessLogic(EntityManager em) {
		log.trace("businessLogic({}) invoked.", em);
		
		// TEST1. 엔티티에 새로운 튜플 생성
		//        즉, 엔티티 클래스의 새로운 인스턴스 생성을 의미합니다.
		
		// CREATE TEST
		// Entity Manager 를 통해서, CRUD합니다.
		Member4 newMember1 = new Member4();
		
		newMember1.setUserName("NAME_1");
		newMember1.setAge(23);
		
		em.persist(newMember1);
		
		// ----------
		Member4 newMember2 = new Member4();
		
		newMember2.setUserName("NAME_2");
		newMember2.setAge(23);
		
		em.persist(newMember2);
		
		// ----------
		Member4 newMember3 = new Member4();
		
		newMember3.setUserName("NAME_3");
		newMember3.setAge(23);
		
		em.persist(newMember3);
		
		// ----------
		Member4 newMember4 = new Member4();
		
		newMember4.setUserName("NAME_4");
		newMember4.setAge(23);
		
		em.persist(newMember4);
		
		// ----------
		// -> 각 인스턴스가 테이블의 각 행과 1:1 매핑되는 것이다!!
		Member4 newMember5 = new Member4();	
		
		newMember5.setUserName("NAME_5");
		newMember5.setAge(23);
		
		em.persist(newMember5);		
	} // businessLogic

} // end class


