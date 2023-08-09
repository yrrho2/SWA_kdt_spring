package org.prgrms.kdtspringdemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcCustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    private final String SELECT_BY_NAME_SQL = "select * from customers WHERE name = ?";
    private final String SELECT_ALL_SQL = "select * from customers";
    private final String INSERT_SQL = "INSERT INTO customers(customerId, name, email) VALUES (UUID_TO_BIN(?), ?, ?)";
    private final String UPDATE_BY_ID_SQL = "UPDATE customers SET name = ? WHERE customerID = UUID_TO_BIN(?)";
    private final String DELETE_ALL_SQL = "DELETE FROM customers";

    public List<String> findNames(String name){
        List<String> names = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt","root","root1234!");
                var statement = connection.prepareStatement(SELECT_BY_NAME_SQL);
        ){
            statement.setString(1, name);
            try(
                    var resultSet = statement.executeQuery();
            ){
                    while(resultSet.next()){
                    var customerName = resultSet.getString("name");
                    var id = UUID.nameUUIDFromBytes(resultSet.getBytes("customerid"));

                    // 타임스탬프를 그냥 쓰지말고 로컬데이트 타임으로 바꿔쓰는게 요즘 방식이레
                    var createdAt = resultSet.getTimestamp("create_at").toLocalDateTime();
                    names.add(customerName);
                }
            }
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
        }
        return names;
    }
    public List<String> findAllNames(){
        List<String> names = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt","root","root1234!");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                var customerName = resultSet.getString("name");
                var id = UUID.nameUUIDFromBytes(resultSet.getBytes("customerid"));
                var name = UUID.nameUUIDFromBytes(resultSet.getBytes("name"));

                // 타임스탬프를 그냥 쓰지말고 로컬데이트 타임으로 바꿔쓰는게 요즘 방식이레
                var createdAt = resultSet.getTimestamp("create_at").toLocalDateTime();
                //.info("customer id->{}, name -> {}, createdAt -> {}", id, name, createdAt);
                names.add(customerName);
            }

        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
        }
        return names;
    }
    public int insertCustomer(UUID customerId, String name, String email){
        try (
            var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt","root","root1234!");
            var statement = connection.prepareStatement(INSERT_SQL);
        ){
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);

            return statement.executeUpdate();
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
        }
        return 0;
    }
    public int deleteAllCustomer(){
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt","root","root1234!");
                var statement = connection.prepareStatement(DELETE_ALL_SQL);
        ){
            return statement.executeUpdate();
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
        }
        return 0;
    }
    public int updateCustomerName(UUID customerId, String name){
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt","root","root1234!");
                var statement = connection.prepareStatement(UPDATE_BY_ID_SQL);
        ){
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());
            return statement.executeUpdate();
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
        }
        return 0;
    }
    public static void main(String[] args) {
        var customerRepository = new JdbcCustomerRepository();

        var count = customerRepository.deleteAllCustomer();
        logger.info("deleted count -> {}", count);

        customerRepository.insertCustomer(UUID.randomUUID(), "new-user", "new-user@gmail.com");

        var customer2 = UUID.randomUUID();
        customerRepository.insertCustomer(customer2, "new-user2", "new-user2@gmail.com");
        customerRepository.findAllNames().forEach(v->logger.info("Found name : {}",v));

        customerRepository.updateCustomerName(customer2, "updated_user2");
        customerRepository.findAllNames().forEach(v->logger.info("Found name : {}",v));


    }
}
