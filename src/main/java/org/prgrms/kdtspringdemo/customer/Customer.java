package org.prgrms.kdtspringdemo.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    private final UUID custiner_id;
    private String name;
    private final String email;
    private LocalDateTime last_login_at;
    private final LocalDateTime created_at;
    public Customer(UUID custiner_id,String name, String email, LocalDateTime created_at) {
        validateName(name);
        this.custiner_id = custiner_id;
        this.email = email;
        this.name=name;
        this.created_at = created_at;
    }
    private void validateName(String name){
            if(name.isBlank()){
            throw new RuntimeException("Name should not be black");
        }
    }

    public void login(){
        this.last_login_at = LocalDateTime.now();
    }
    public void changeName(String name){
        validateName(name);
        this.name = name;
    }

    public UUID getCustomerId() {
        return custiner_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLast_login_at() {
        return last_login_at;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public Customer(UUID custiner_id, String name, String email, LocalDateTime last_login_at, LocalDateTime created_at) {
        validateName(name);
        this.custiner_id = custiner_id;
        this.name = name;
        this.email = email;
        this.last_login_at = last_login_at;
        this.created_at = created_at;
    }
}
