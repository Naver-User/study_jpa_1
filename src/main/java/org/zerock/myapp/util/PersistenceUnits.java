package org.zerock.myapp.util;


// 영속성 유닛(Persistence Unit)의 이름이 반복적으로 사용되기 때문에
// 아래에 static final 상수로 선언해서, 재사용하도록 하겠습니다.
public interface PersistenceUnits {
	
	public static final String H2 = "H2-2.2.224";
	public static final String ORACLE = "ORACLE19CR3";
	public static final String MYSQL = "MYSQL-8.4.1";
	
} // end class
