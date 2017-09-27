package org.demo.credit.dao;

import org.demo.credit.bean.CustomerCredit;
import org.demo.credit.util.DatabaseUtil;
import org.demo.credit.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreditDAO {

    private static final Logger logger = LoggerFactory.getLogger(CreditDAO.class);

    /**
     * Get customer's total credit amount
     *
     * @param customerId - Customer Id
     * @return - Total credit amount
     */
    public double getTotalCreditAmount(String customerId) {
        Connection dbConnection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet = null;
        double totalCreditAmount = 0;
        try {
            dbConnection = DatabaseUtil.getDBConnection();
            prepStmt = dbConnection.prepareStatement(SQLQueries.QUERY_GET_CUSTOMER_CREDIT_AMOUNT);
            prepStmt.setString(1, customerId);
            resultSet = prepStmt.executeQuery();
            while (resultSet.next()) {
                totalCreditAmount += resultSet.getDouble("AMOUNT");
            }
            return totalCreditAmount;
        } catch (SQLException e) {
            String errorMessage = "Error occurred while getting customer's total credit amount";
            logger.error(errorMessage, e);
        } finally {
            DatabaseUtil.closeAllConnections(dbConnection, resultSet, prepStmt);
        }
        return totalCreditAmount;
    }

    /**
     * Create customer credit record
     *
     * @param customerCredit - Customer credit
     * @return id
     */
    public void createCustomerCredit(CustomerCredit customerCredit) throws SQLException {
        Connection dbConnection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet = null;
        try {
            dbConnection = DatabaseUtil.getDBConnection();
            prepStmt = dbConnection.prepareStatement(SQLQueries.QUERY_INSERT_CUSTOMER_CREDIT, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, customerCredit.getCustomerId());
            prepStmt.setString(2, customerCredit.getReferenceNumber());
            prepStmt.setDouble(3, customerCredit.getAmount());
            prepStmt.executeUpdate();
            ResultSet rs = prepStmt.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            customerCredit.setId(id);
            dbConnection.commit();
        } catch (SQLException e) {
            try {
                if (dbConnection != null) {
                    dbConnection.rollback();
                }
            } catch (SQLException e1) {
                logger.error("Error occurred while rolling back customer credit create transaction");
            }
            String errorMessage = "Error occurred while creating customer credit";
            logger.error(errorMessage, e);
            throw e;
        } finally {
            DatabaseUtil.closeAllConnections(dbConnection, resultSet, prepStmt);
        }
    }
}
