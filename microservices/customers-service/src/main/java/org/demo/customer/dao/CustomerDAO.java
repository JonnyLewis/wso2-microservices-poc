/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.demo.customer.dao;

import org.demo.customer.bean.Customer;
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
    public Customer getCustomer(String id) {
        Connection dbConnection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet = null;
        try {
            dbConnection = DatabaseUtil.getDBConnection();
            prepStmt = dbConnection.prepareStatement(SQLQueries.QUERY_GET_CUSTOMER);
            prepStmt.setString(1, id);
            resultSet = prepStmt.executeQuery();
            if (resultSet.next()) {
                Customer customerBean = new Customer();
                customerBean.setId(resultSet.getString("ID"));
                customerBean.setFname(resultSet.getString("FNAME"));
                customerBean.setLname(resultSet.getString("LNAME"));
                customerBean.setAddress(resultSet.getString("ADDRESS"));
                customerBean.setState(resultSet.getString("STATE"));
                customerBean.setPostalcode(resultSet.getString("POSTALCODE"));
                customerBean.setCountry(resultSet.getString("COUNTRY"));
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
