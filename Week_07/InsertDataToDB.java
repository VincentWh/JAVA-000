
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.*;

@Slf4j
public class InsertDataToDB {
    private static HikariPool pool;
    public static void main(String[] args)  {
        //配置 Hikari 连接池。
        try(Connection connection = getHikariPoolConnection()) {
            connection.setAutoCommit(false);
            long startTime = System.currentTimeMillis();
            // 使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
            insertStudentInBatch(connection);
            long endTime = System.currentTimeMillis();
            log.info("Insert cost total time:{}", endTime - startTime);
            connection.commit();
            // After running, it takes 142840ms to insert
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != pool) {
                try {
                    pool.shutdown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Connection getHikariPoolConnection() throws SQLException {
        if (null == pool) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/myshop?useSSL=false&&useUnicode=true&characterEncoding=UTF-8");
            config.setUsername("root");
            config.setPassword("");
            config.setMinimumIdle(3);
            config.setMaximumPoolSize(5);
            config.setPoolName("pool");
            pool = new HikariPool(config);
        }
        return pool.getConnection();
    }

    private static void insertStudentInBatch(Connection connection) throws SQLException {
        String insert = "insert into orders (account_id, product_id, product_counts, order_amount, ship_fee, delivery_info, order_remark, pay_status, pay_time, refund_time, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insert)) {
            for(int i = 1; i<=1; i++){
                insertStatement.setInt(1, i);
                insertStatement.setInt(2, i);
                insertStatement.setInt(3, 5);
                insertStatement.setDouble(4, 30);
                insertStatement.setDouble(5, 6);
                insertStatement.setString(6, "deliveryInfo");
                insertStatement.setString(7, "test_remark_test");
                insertStatement.setInt(8, 1);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                insertStatement.setTimestamp(9, timestamp);
                insertStatement.setTimestamp(10, null);
                insertStatement.setTimestamp(11, timestamp);
                insertStatement.setTimestamp(12, timestamp);
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        }
    }
}
