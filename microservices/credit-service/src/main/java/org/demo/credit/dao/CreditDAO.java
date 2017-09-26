package org.demo.credit.dao;

import org.demo.credit.util.DatabaseUtil;
import org.demo.credit.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditDAO {

    private static final Logger logger = LoggerFactory.getLogger(CreditDAO.class);

    /**
     * Get customer's outstnading balance
     * @param id - Customer Id
     * @return - Outstanding Balance
     */
    public double getCustomerOutstandingBalance(String id) {
        Connection dbConnection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet = null;
        double outstandingBalance = 0;
        try {
            dbConnection = DatabaseUtil.getDBConnection();
            prepStmt = dbConnection.prepareStatement(SQLQueries.QUERY_GET_CUSTOMER_OUTSTANDING_BALANCE);
            prepStmt.setString(1, id);
            resultSet = prepStmt.executeQuery();
            while (resultSet.next()) {
                outstandingBalance += resultSet.getDouble("OUTSTANDING_BALANCE");
            }
            return outstandingBalance;
        } catch (SQLException e) {
            String errorMessage = "Error occurred while getting customer outstanding balance";
            logger.error(errorMessage, e);
        } finally {
            DatabaseUtil.closeAllConnections(dbConnection, resultSet, prepStmt);
        }
        return outstandingBalance;
    }
}
