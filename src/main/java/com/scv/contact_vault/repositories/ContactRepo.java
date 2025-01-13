package com.scv.contact_vault.repositories;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.scv.contact_vault.entity.Contact;
import com.scv.contact_vault.entity.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    // custom finder method
    List<Contact> findByUser(User user);

    // custom query method
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

}