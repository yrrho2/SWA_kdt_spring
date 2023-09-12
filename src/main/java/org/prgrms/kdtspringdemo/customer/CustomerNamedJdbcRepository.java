package org.prgrms.kdtspringdemo.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class CustomerNamedJdbcRepository implements CustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(CustomerNamedJdbcRepository.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static RowMapper<Customer> customerRowMapper = (resultSet, i)->{
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var cutomerId = toUUID(resultSet.getBytes("customerid"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at")!=null?
                resultSet.getTimestamp("last_login_at").toLocalDateTime():null;
        var createdAt = resultSet.getTimestamp("create_at").toLocalDateTime();
        return new Customer(cutomerId, customerName, email, createdAt, lastLoginAt);
    };

    public CustomerNamedJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String,Object> toparamMap(Customer customer){
    return new HashMap<String, Object>(){{
        put("customerId", customer.getCustomerId().toString().getBytes());
        put("name", customer.getName());
        put("email", customer.getEmail());
        put("createdAt", Timestamp.valueOf(customer.getCreated_at()));
    }};
    }

    @Override
    public Customer insert(Customer customer) {
        var update = jdbcTemplate.update("INSERT INTO customers(customerId, name, email, create_at) VALUES (UNHEX(REPLACE(:custinerId,'-','')), :name, :email, :createdAt)",
                toparamMap(customer)
                );
//        var update = jdbcTemplate.update("INSERT INTO customers(customerId, name, email, create_at) VALUES (UNHEX(REPLACE(?,'-','')), ?, ?, ?)",
//                customer.getCustomerId().toString().getBytes(),
//                customer.getName(),
//                customer.getEmail(),
//                Timestamp.valueOf(customer.getCreated_at()));
        if(update!=1){
            throw new RuntimeException("Noting was inserted");
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update("UPDATE customers SET name = :name, email = :email, last_login_at = :lastLoginAt WHERE customerID = UNHEX(REPLACE(:customerId,'-',''))",
                toparamMap(customer));
        if(update!=1){
            throw new RuntimeException("Noting was inserted");
        }
        return customer;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from customers", Collections.emptyMap(),Integer.class);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers",customerRowMapper);
    }
    @Override
    public Optional<Customer> findById(UUID customerID) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE customerID = UNHEX(REPLACE(:customerID,'-',''))"
                    ,Collections.singletonMap("customerId", customerID.toString().getBytes())
                    ,customerRowMapper));
        }catch (EmptyResultDataAccessException e){
            logger.error("Got empty result",e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try{
            return Optional.of(jdbcTemplate.queryForObject("select * from customers WHERE name = :name",
                    Collections.singletonMap("name", name),
                    customerRowMapper));
        }catch (EmptyResultDataAccessException e){
            logger.error("Got empty result",e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try{
            return Optional.of(jdbcTemplate.queryForObject("select * from customers WHERE email = :email",
                    Collections.singletonMap("email", email),
                    customerRowMapper));
        }catch (EmptyResultDataAccessException e){
            logger.error("Got empty result",e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteALL() {
        jdbcTemplate.update("DELETE FROM customers",Collections.emptyMap());
    }
    static UUID toUUID(byte[] bytes){
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
