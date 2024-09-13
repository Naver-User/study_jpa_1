package org.zerock.myapp.entity;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;


@Data

@Entity
//@Entity(name)
//@Table(name)
public class Member3 implements Serializable {	
	@Serial private static final long serialVersionUID = 1L;
	
	// 1. PK 선언
	@Id @GeneratedValue
	@Column(name = "SEQ")
	private Long id;
	
	// 2. 일반속성
	
	// (1) 일반속성에 아무런 JPA 어노테이션도 붙이지 않으면,
	//     기본은 필드의 이름 == 컬럼의 이름으로 매핑됩니다.
	// (2) 위 (1) 의 규칙과 다르게 즉, 필드명 != 컬럼명이고
	//     기타 다른 제약조건도 설정하고 싶다면, 아래와 같이
	//     @Column 어노테이션을 사용합니다.
	
	// (1) name 속성의 값이 실제 컬럼의 이름이된다.
	// 
	@Column(name = "MyName", length = 50)
	private String name;
	
	private Integer age;
	
	// (2) scale, precision
	//		H2 데이터베이스에서는, Precision과 Scale을 정해도,
	//     그냥 double 컬럼타입으로 지정된다.
	@Column(name = "weight", scale = 7, precision = 3)
	private Double weight;	// 57.9 -> ????
	
	// (3) table : 두 테이블간의 관계를 매핑할 때에는 의미가 있지만,
	//     지금 이 필드를 포함하고 있는 엔티티에 대한 한개의 테이블을
	//     매핑할 때에는 지정할 의미가 없다!
	
	// (4) nullable: 이 속성에 매핑될 컬럼이 null 제약조건임을 표시함.
	//               즉, NULL (결측치)를 값으로 가질 수 있음을 의미함.
	//     - If nullable = true  -> NULL 제약조건 의미
	//     - If nullable = false -> NOT NULL 제약조건 의미
	
	// (5) insertable: 이 속성은 INSERT 문의 컬럼 선언부에, 이 컬럼을
	//                 표시할지 말지를 결정합니다. (***)
	
	@Column(
		table = "Member3", 
		nullable = false, 
		insertable = true)
	private Double height;
   
	// (6) updateable 속성: 자동생성되는 UPDATE SQL 문장의 SET 절에서,
	//     아래 필드를 수정할 수 있게 표시할 것인가? 말것인가?를 결정하는 속성입니다.
	//     If updateable = true,  -> UPDATE 문의 SET 절에 포함시킴 -> 수정가능!
	//     If updateable = false, -> UPDATE 문의 SET 절에 미포함 -> 수정불가능!
	
	@Column(updatable = false)
	private Boolean isMale = true;
	
	// 이 필드에 매핑되는 컬럼에 UNIQUE 제약조건을 설정합니다.
	@Column(unique = true)
	private Long memberNo;		// 회원번호
	
	// 이 속성은 이 필드에 매핑될 컬럼의 명세를 직접 지정합니다.
	// 더 쉽게 말씀드리면, 마치 개발자가 처음 테이블을 생성할 때,
	// CREATE TABLE <table> (
	//   ...
	//   address VARCHAR(300) NOT NULL,
	//   ...
	// ); 하듯이, 직접 컬럼의 도메인을 지정하는 것입니다.
	//
	// *주의사항1*: 이미 필드의 이름이 디폴트로 컬럼명으로 매핑되기 때문에,
	//              이 속성에 값을 넣을때에는, 컬럼명은 넣지 않습니다.
	// *주의사항2*: 이 속성을 통해, 직접 컬럼을 정의하게 되면, 특정 DB에 종속적인
	//              컬럼의 정의(예: 타입, 길이, 제약조건 등)가 되어버리기 때문에,
	//              JPA의 장점 중 하나인, 쉬운 데이터베이스의 교체가 안되게 됩니다.
	
	@Column(columnDefinition = "VARCHAR(300) NOT NULL")
	private String address;		// 주소
	
	
	// @Column 어노테이션을 딱히 붙여서, 특정 조건을 만들만한 사유가 없을 때에는
	// 아래와 같이 2가지로 선언하시면 됩니다:
	//
	//    (1) 아무런 어노테이션도 붙이지 않습니다.
	//    (2) @Basic 이란 어노테이션을 붙입니다.   <--- ***: 권고합니다.
	
	@Basic
	private String job;			// 직업
	
	
} // end clas
