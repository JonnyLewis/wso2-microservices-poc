package org.demo.credit.util;

public final class SQLQueries {

    public final static String QUERY_GET_CUSTOMER_OUTSTANDING_BALANCE
            = "SELECT OUTSTANDING_BALANCE FROM CUSTOMER_CREDIT WHERE CUSTOMER_ID = ?";

}
