package org.prgrms.kdtspringdemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

public class JdbcCustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    public static void main(String[] args) {
        try (
            var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt","root","root1234!");
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("select * from customers");

        ){
            while(resultSet.next()){
            var name = resultSet.getString("name");
            var id = UUID.nameUUIDFromBytes(resultSet.getBytes("customerid"));
            logger.info("customer id->{}, name -> {}", id, name);
            }
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
        }

    }
}
