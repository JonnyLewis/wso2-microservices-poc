package org.demo.loan.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Database utility
 */
public class DatabaseUtil {

    private static Log logger = LogFactory.getLog(DatabaseUtil.class);
    private static DataSource jdbcds = loadUserStoreSpacificDataSoruce();

    /**
     * Get database connection.
     * @return SQL connection
     * @throws java.sql.SQLException
     */
    public static Connection getDBConnection() throws SQLException {
        Connection dbConnection = getJDBCDataSource().getConnection();
        dbConnection.setAutoCommit(false);
        if (dbConnection.getTransactionIsolation() != Connection.TRANSACTION_READ_COMMITTED) {
            dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        }
        return dbConnection;
    }

    /**
     * Get JDBC data source.
     * @return datasource
     */
    private static DataSource getJDBCDataSource() {
        if (jdbcds == null) {
            jdbcds = loadUserStoreSpacificDataSoruce();
        }
        return jdbcds;
    }

    /**
     * Load user store properties from config and create datasource.
     * @return datasource
     */
    private static DataSource loadUserStoreSpacificDataSoruce() {

        String jdbcdriver = System.getenv("JDBC_DRIVER");
        String jdbcurl = System.getenv("JDBC_URL");
        String dbuser = System.getenv("DB_USER");
        String dbpassword = System.getenv("DB_PASSWORD");

        logger.info("jdbcdriver: " + jdbcdriver);
        logger.info("jdbcurl: " + jdbcurl);
        logger.info("dbuser: " + dbuser);
        logger.info("dbpassword: " + dbpassword);

        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setDriverClassName(jdbcdriver);
        poolProperties.setUrl(jdbcurl);
        poolProperties.setUsername(dbuser);
        poolProperties.setPassword(dbpassword);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setValidationQuery("SELECT 1");

//        PoolProperties poolProperties = new PoolProperties();
//        poolProperties.setDriverClassName("com.mysql.jdbc.Driver");
//        poolProperties.setUrl("jdbc:mysql://localhost:3306/loandb");
//        poolProperties.setUsername("root");
//        poolProperties.setPassword("root");
//        poolProperties.setTestOnBorrow(true);
//        poolProperties.setValidationQuery("SELECT 1");

        return new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
    }

    /**
     * Close DB connection
     * @param dbConnection sql connection
     */
    public static void closeConnection(Connection dbConnection) {

        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                logger.error("Database error. Could not close statement. Continuing with others. - " + e.getMessage(), e);
            }
        }
    }

    /**
     * Close resultset.
     * @param rs SQL resultset
     */
    private static void closeResultSet(ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Database error. Could not close result set  - " + e.getMessage(), e);
            }
        }
    }

    /**
     * Close prepaedstatement.
     * @param preparedStatement SQL preparedstatememt
     */
    private static void closeStatement(PreparedStatement preparedStatement) {

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error("Database error. Could not close statement. Continuing with others. - " + e.getMessage(), e);
            }
        }
    }

    /**
     * Close number of prepared statement.
     * @param prepStmts all prepaired statements
     */
    private static void closeStatements(PreparedStatement... prepStmts) {

        if (prepStmts != null && prepStmts.length > 0) {
            for (PreparedStatement stmt : prepStmts) {
                closeStatement(stmt);
            }
        }
    }

    /**
     * Close all sql connections and prepared statements
     * @param dbConnection sql connection
     * @param prepStmts prepairedstatements
     */
    public static void closeAllConnections(Connection dbConnection, PreparedStatement... prepStmts) {

        closeStatements(prepStmts);
        closeConnection(dbConnection);
    }

    /**
     * Close all sql connections, resultset and prepared statements
     * @param dbConnection sql connection
     * @param rs resultset
     * @param prepStmts all prepaired statements
     */
    public static void closeAllConnections(Connection dbConnection, ResultSet rs, PreparedStatement... prepStmts) {

        closeResultSet(rs);
        closeStatements(prepStmts);
        closeConnection(dbConnection);
    }

    public static void closeAllConnections(Connection dbConnection, ResultSet rs1, ResultSet rs2,
            PreparedStatement... prepStmts) {
        closeResultSet(rs1);
        closeResultSet(rs1);
        closeStatements(prepStmts);
        closeConnection(dbConnection);
    }

}