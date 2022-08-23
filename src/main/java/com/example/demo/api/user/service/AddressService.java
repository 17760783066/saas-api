package com.example.demo.api.user.service;

import java.util.List;

import com.example.demo.api.user.model.Address;

public interface AddressService {

    List<Address> addresses();

    void saveAddress(Address address);

    Address address(Integer id);

    void removeOne(Integer id);
    
}
