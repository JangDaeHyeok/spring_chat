package jdh.example.chat;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ChatInitializer implements ApplicationRunner{
private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired SqlSessionFactory sf;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("*********************************************************************");
		log.info("[Spring Chat] 서버 기동 시작 및 기본 세팅 시작...");
		
		// 연결 DB 정보 확인
		String ds = sf.getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData().getURL();
		String dbmsUrl =  ds.replaceAll("jdbc:mysql://", "");
		dbmsUrl = dbmsUrl.substring(0, dbmsUrl.indexOf("?") > -1 ? dbmsUrl.indexOf("?") : dbmsUrl.length());
		log.info("[Spring Chat] 사용 DB : " + dbmsUrl); 
		
		log.info("*********************************************************************");
	}
}
