package org.zerock.myapp.entity;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data



@Entity
@Table(name = "USER2")	// 실제 테이블명
public class Member2 implements Serializable {
	@Serial private static final long serialVersionUID = 1L;

	
	// 키속성에 @Id 어노테이션만 붙이면, 반드시 개발자가 수동으로 키값을 제공해야함
	@Id
	
	// @Id + @GeneratedValue 어노테이션을 키속성에 붙이면,
	// 개발자가 더이상 키값을 제공하지 않아도 된다!
	// 대신, @GenratedValue 의 생성전략속성(strategy)에 적용가능한 상수별로
	// 실제 사용할 데이터베이스에 대해서 테스트 해보시고, 해당 데이터베이스가
	// 어디까지 지원하지를 테스트 해보시기를 권고 드립니다.
	
	@GeneratedValue
//	@GeneratedValue(strategy = GenerationType.AUTO)		// 1, Default
//	@GeneratedValue(strategy = GenerationType.IDENTITY)	// 2
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)	// 3
//	@GeneratedValue(strategy = GenerationType.TABLE)	// 4

	/**
	 * JPA 표준과의 일관성을 유지하기 위해, 이전에 Hibernate 이 별도로 제공했엇던
	 * 아래와 같은 @SequenceGenerator, @TableGenerator 어노테이션은 지금 싯점에서는
	 * 사용하지 말라!!! 라고 권고 드립니다.
	 * 
		@SequenceGenerator(
			name = "MySequenceGenerator",
			sequenceName = "MEMBER_SEQ",
			initialValue = 10,
			allocationSize = 20
		)
		
		@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "MySequenceGenerator"
		)
	*/
	private Long id;

	private String name;
	private Integer age;
	
	
   
	
} // end class


