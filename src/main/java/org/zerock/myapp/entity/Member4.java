package org.zerock.myapp.entity;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Data


@Entity
@Table(name = "MEMBER4")
@SequenceGenerator(
	name = "member4_sequence_generator",
	sequenceName = "seq_member4",
	allocationSize = 1,
	initialValue = 1)
public class Member4 implements Serializable {
	@Serial private static final long serialVersionUID = 1L;
	
	
	// 1. PK 선언
	@Id
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "member4_sequence_generator")
	private Long id;			// 키속성
	
	// 2. 일반속성 선언
	private String userName;
	
	@Transient
	private Integer age;			// Mapping 에서 제외시킴
	
	@Lob							// Large OBject 의 약어
	private String description;		// 대량의 문자메시지 저장
	
	// 열거타입의 이 속성에 열거상수가 설정되었을 때에,
	// 매핑된 테이블의 컬럼값으로 열거상수의 이름을 문자열로
	// 넣어주겠다는 의미가 됩니다.
	// 만일, 유형을 서수(ORDINAL)로 지정하면, 역시 매핑된 컬럼의
	// 값으로 열거상수가 선언된 순번(Ordinal No.)을 값으로 넣어주도록
	// 컬럼타입도 정수타입이 되고, 값도 정수로 들어가게 됩니다.
	@Enumerated(EnumType.STRING)
	private Gender gender;			// 남성, 여성만 있다 가정
	
	
	// 3. 정보통신망법에서 요구하는 Action Log 속성2개
	@CreationTimestamp
	@Basic(optional = false, fetch = FetchType.LAZY)	
	private Timestamp createDate;
	
	@UpdateTimestamp
	@Basic(optional = true, fetch = FetchType.LAZY)
	private LocalDateTime updateDate;

	
	
} // end class
