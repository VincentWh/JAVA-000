package dbframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

/**
 * Week07 作业题目（周六）：
 * 3.（必做）读写分离 - 数据库框架版本 2.0
 */
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class DbFrameworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(DbFrameworkApplication.class, args);
    }
}
