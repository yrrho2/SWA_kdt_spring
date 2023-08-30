package org.prgrms.kdtspringdemo.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerJdbcRepository implements CustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(CustomerJdbcRepository.class);
    private final DataSource dataSource;

    public CustomerJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Customer insert(Customer customer) {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("INSERT INTO customers(customerId, name, email, create_at) VALUES (UUID_TO_BIN(?), ?, ?, ?)");
        ){
            statement.setBytes(1, customer.getCustomerId().toString().getBytes());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setTimestamp(4, Timestamp.valueOf(customer.getCreated_at()));
            var excuteupdate = statement.executeUpdate();
            if(excuteupdate != 1){
                throw new RuntimeException("Nothing was inserted");
            }
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
            throw new RuntimeException(throwables);
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> allCustomer = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers");
                var resultSet = statement.executeQuery();
        ){
            while(resultSet.next()){
                mapToCustomer(allCustomer, resultSet);
            }
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
            throw new RuntimeException(throwables);
        }
        return allCustomer;
    }
    @Override
    public Optional<Customer> findById(UUID customerID) {
        List<Customer> allCustomer = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE customerID = UUID_TO_BIN(?)");
        ){
            statement.setBytes(1,customerID.toString().getBytes());
            try(var resultSet = statement.executeQuery();){
                while(resultSet.next()){
                    mapToCustomer(allCustomer, resultSet);
                }
            }
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
            throw new RuntimeException(throwables);
        }
        logger.info(allCustomer.get(0).getCreated_at().toString());
        return allCustomer.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByName(String name) {
        List<Customer> allCustomer = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE name = ?");

        ){
            statement.setString(1,name);
            try(var resultSet = statement.executeQuery();){
                while(resultSet.next()){
                    mapToCustomer(allCustomer, resultSet);
                }
            }
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
            throw new RuntimeException(throwables);
        }
        return allCustomer.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        List<Customer> allCustomer = new ArrayList<>();
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE email = ?");

        ){
            statement.setString(1,email);
            try(var resultSet = statement.executeQuery();){
                while(resultSet.next()){
                    mapToCustomer(allCustomer, resultSet);
                }
            }
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
            throw new RuntimeException(throwables);
        }
        return allCustomer.stream().findFirst();
    }

    @Override
    public void deleteALL() {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement("DELETE FROM customers");
        ){
            statement.executeUpdate();
        }catch (SQLException throwables) {
            logger.error("Got error while closing conneciton", throwables);
            throw new RuntimeException((throwables));
        }
    }
    private void mapToCustomer(@org.jetbrains.annotations.NotNull List<Customer> allCustomers, java.sql.ResultSet resultSet) throws SQLException{
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var cutomerId = toUUID(resultSet.getBytes("customerid"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at")!=null?
                resultSet.getTimestamp("last_login_at").toLocalDateTime():null;
        var createdAt = resultSet.getTimestamp("create_at").toLocalDateTime();
        allCustomers.add(new Customer(cutomerId, customerName, email, createdAt, lastLoginAt));
    }

    static UUID toUUID(byte[] bytes){
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
