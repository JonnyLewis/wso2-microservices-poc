package org.demo.customer.dao;

import org.demo.customer.bean.CustomerBean;
import org.demo.customer.util.DatabaseUtil;
import org.demo.customer.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDAO.class);

    /**
     * Get Customer details
     * @param id - Customer ID
     * @return Customer
     */
    public CustomerBean getCustomer(String id) {
        Connection dbConnection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet = null;
        try {
            dbConnection = DatabaseUtil.getDBConnection();
            prepStmt = dbConnection.prepareStatement(SQLQueries.QUERY_GET_CUSTOMER);
            prepStmt.setString(1, id);
            resultSet = prepStmt.executeQuery();
            if (resultSet.next()) {
                CustomerBean customerBean = new CustomerBean();
                customerBean.setId(resultSet.getString("ID"));
                customerBean.setFname(resultSet.getString("FNAME"));
                customerBean.setLname(resultSet.getString("LNAME"));
                customerBean.setAddress(resultSet.getString("ADDRESS"));
                customerBean.setState(resultSet.getString("STATE"));
                customerBean.setPostalcode(resultSet.getString("POSTALCODE"));
                //customerBean.setCountry(resultSet.getString("COUNTRY"));
                return customerBean;
            }
        } catch (SQLException e) {
            String errorMessage = "Error occurred while getting customer information";
            logger.error(errorMessage, e);
        } finally {
            DatabaseUtil.closeAllConnections(dbConnection, resultSet, prepStmt);
        }
        return null;
    }
}
