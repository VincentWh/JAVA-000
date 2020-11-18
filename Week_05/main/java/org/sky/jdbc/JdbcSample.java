package org.sky.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.*;

public class JdbcSample {
    private static HikariPool pool;
    public static void main(String[] args)  {
        //6.1 使用 JDBC 原生接口，实现数据库的增删改查操作。
//        try(Connection connection = getOriginalJdbcConnection()) {
        //6.3 配置 Hikari 连接池，改进上述操作。提交代码到 Github。
        try(Connection connection = getHikariPoolConnection()) {
            connection.setAutoCommit(true);
            //6.1 使用 JDBC 原生接口，实现数据库的增删改查操作。
            queryStudent(connection);
            //6.1 使用 JDBC 原生接口，实现数据库的增删改查操作。
            insertStudent(connection);

            //6.2 使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
            insertStudentInBatch(connection);
            connection.setAutoCommit(false);
            //6.1 使用 JDBC 原生接口，实现数据库的增删改查操作。
            updateStudent(connection);
            //6.1 使用 JDBC 原生接口，实现数据库的增删改查操作。
            deleteStudent(connection);
            //6.2 使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //6.3 配置 Hikari 连接池，改进上述操作。提交代码到 Github。
            if (null != pool) {
                try {
                    pool.shutdown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Connection getOriginalJdbcConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test", "root", "");
    }

    private static Connection getHikariPoolConnection() throws SQLException {
        if (null == pool) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test");
            config.setUsername("root");
            config.setPassword("");
            config.setMinimumIdle(3);
            config.setMaximumPoolSize(5);
            config.setPoolName("pool");
            pool = new HikariPool(config);
        }
        return pool.getConnection();
    }

    private static void queryStudent(Connection connection) throws SQLException {
        String query = "select * from student";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String id = resultSet.getString("studentId");
                String name = resultSet.getString("studentName");
                System.out.println("studentId:" + id + ",studentName:" + name);
            }
        }
    }

    private static void insertStudent(Connection connection) throws SQLException {
        String insert = "insert into student (studentId, studentName, class) VALUES ('1', 'sky', 'classA')";
        try (Statement insertStatement = connection.createStatement()) {
            insertStatement.execute(insert);
        }
    }

    private static void insertStudentInBatch(Connection connection) throws SQLException {
        String insert = "insert into student (studentId, studentName, class) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insert)) {
            insertStatement.setString(1, "2");
            insertStatement.setString(2, "sky2");
            insertStatement.setString(3, "classD");
            insertStatement.addBatch();
            insertStatement.setString(1, "3");
            insertStatement.setString(2, "sky3");
            insertStatement.setString(3, "classB");
            insertStatement.addBatch();
            insertStatement.setString(1, "4");
            insertStatement.setString(2, "sky4");
            insertStatement.setString(3, "classC");
            insertStatement.addBatch();
            insertStatement.executeBatch();
        }
    }

    private static void updateStudent(Connection connection) throws SQLException {
        String update = "update student set class = 'classB' where studentId = '1'";
        try (Statement insertStatement = connection.createStatement()) {
            insertStatement.execute(update);
        }
    }

    private static void deleteStudent(Connection connection) throws SQLException {
        String delete = "delete from student where studentName = 'sky'";
        try (Statement deleteStatement = connection.createStatement()) {
            deleteStatement.executeUpdate(delete);
        }
    }
}
