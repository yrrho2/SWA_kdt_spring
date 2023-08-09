package org.prgrms.kdtspringdemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcCustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    public List<String> findNames(String name){
        var SELECT_SQL = "select * from customers WHERE name = ?";
        List<String> names = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt","root","root1234!");
                var statement = connection.prepareStatement(SELECT_SQL);
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
                    logger.info("customer id->{}, name -> {}, createdAt -> {}", id, name, createdAt);
                    names.add(customerName);
                }
            }
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
        }
        return names;
    }
    public static void main(String[] args) {
        // 이것이 SQL injection 이라고 함. or으로 싹다 가져오는거 그거
        var names = new JdbcCustomerRepository().findNames("tester01");
        names.forEach(v->logger.info("Found name : {}",v));


    }
}
