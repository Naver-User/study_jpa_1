package org.zerock.myapp.entity;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;


//@Value		// 읽기전용객체를 만들때 사용하는 자바빈즈규약생성
@Data		// 수정가능객체를 이하상동


//이 클래스가 JPA가 매핑할 관계형 테이블로 매핑될 클래스임
@Entity
//@Entity(name = "CUSTOMMEMBER")

// Important: 개발자가 지정한 테이블명과 컬럼명은, H2 데이터베이스에서는
//            모두 대문자로 관리(CRUD)됩니다.(물론, Oracle, MySQL, .....)
//@Table(name = "member")

// 엔티티 클래스는 우리가 배운대로 자바빈즈 스펙에 맞게 선언해야 합니다.
public class Member implements Serializable {
	@Serial private static final long serialVersionUID = 1L;
	
	@Id							// PK 선언
	private String id;			// 키속성
	
	private String userName;	// 일반속성
	private Integer age;		// 일반속성

	
	
} // end class
