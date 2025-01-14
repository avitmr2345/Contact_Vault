package com.scv.contact_vault.services;

import java.util.List;
import org.springframework.data.domain.Page;
import com.scv.contact_vault.entity.Contact;
import com.scv.contact_vault.entity.User;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAll();

    Contact getById(String id);

    void delete(String id);

    Page<Contact> searchByName(String name, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByEmail(String email, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByPhoneNumber(String phoneNumber, int size, int page, String sortBy, String order,
            User user);

    List<Contact> getByUserId(String userId);

    Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction);
}