package org.demo.credit.util;

public final class SQLQueries {

    public final static String QUERY_GET_CUSTOMER_CREDIT_AMOUNT =
            "SELECT AMOUNT FROM CUSTOMER_CREDIT WHERE CUSTOMER_ID = ?";
    public final static String QUERY_INSERT_CUSTOMER_CREDIT =
            "INSERT INTO CUSTOMER_CREDIT (CUSTOMER_ID, REFERENCE_NUMBER, AMOUNT) " +
                    "VALUES(?,?,?)";

}
