package org.prgrms.kdtspringdemo.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
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
    private final JdbcTemplate jdbcTemplate;
    private static RowMapper<Customer> customerRowMapper = (resultSet, i)->{
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var cutomerId = toUUID(resultSet.getBytes("customerid"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at")!=null?
                resultSet.getTimestamp("last_login_at").toLocalDateTime():null;
        var createdAt = resultSet.getTimestamp("create_at").toLocalDateTime();
        return new Customer(cutomerId, customerName, email, createdAt, lastLoginAt);
    };

    public CustomerJdbcRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Customer insert(Customer customer) {
        var update = jdbcTemplate.update("INSERT INTO customers(customerId, name, email, create_at) VALUES (UNHEX(REPLACE(?,'-','')), ?, ?, ?)",
                customer.getCustomerId().toString().getBytes(),
                customer.getName(),
                customer.getEmail(),
                Timestamp.valueOf(customer.getCreated_at()));
        if(update!=1){
            throw new RuntimeException("Noting was inserted");
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update("UPDATE customers SET name = ?, email = ?, last_login_at = ? WHERE customerID = UNHEX(REPLACE(?,'-',''))",
                customer.getName(),
                customer.getEmail(),
                customer.getLast_login_at()!=null ? Timestamp.valueOf(customer.getLast_login_at()) : null,
                customer.getCustomerId().toString().getBytes());
        if(update!=1){
            throw new RuntimeException("Noting was inserted");
        }
        return customer;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from customers", Integer.class);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers",customerRowMapper);
    }
    @Override
    public Optional<Customer> findById(UUID customerID) {
        try{
            return Optional.of(jdbcTemplate.queryForObject("select * from customers WHERE customerID = UNHEX(REPLACE(?,'-',''))", customerRowMapper, customerID.toString().getBytes()));
        }catch (EmptyResultDataAccessException e){
            logger.error("Got empty result",e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try{
            return Optional.of(jdbcTemplate.queryForObject("select * from customers WHERE name = ?", customerRowMapper, name));
        }catch (EmptyResultDataAccessException e){
            logger.error("Got empty result",e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try{
            return Optional.of(jdbcTemplate.queryForObject("select * from customers WHERE email = ?", customerRowMapper, email));
        }catch (EmptyResultDataAccessException e){
            logger.error("Got empty result",e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteALL() {
        jdbcTemplate.update("DELETE FROM customers");
    }
    static UUID toUUID(byte[] bytes){
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
