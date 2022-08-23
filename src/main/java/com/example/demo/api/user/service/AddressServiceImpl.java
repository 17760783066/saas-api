package com.example.demo.api.user.service;

import java.util.List;

import com.example.demo.api.user.authority.UserContexts;
import com.example.demo.api.user.entity.UserConstants;
import com.example.demo.api.user.model.Address;
import com.example.demo.api.user.repository.AddressRepository;
import com.example.demo.common.model.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends UserConstants implements AddressService, ErrorCode {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> addresses() {
        Integer userId = UserContexts.requestUserId();
        return addressRepository.findByUserId(userId);
    }

    public Address getById(Integer id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAddress(Address address) {
        Integer id = address.getId();
        if (address.getIsDefault().equals(ADDRESS_IS_DEFAULT)) {

            addressRepository.updateIsDefault(UserContexts.requestUserId(), ADDRESS_NOT_DEFAULT);
        }
        if (id == null || id == 0) {
            address.setIsDefault(ADDRESS_IS_DEFAULT);
            address.setUserId(UserContexts.requestUserId());
            address.setCreatedAt(System.currentTimeMillis());
            address.setUpdateAt(System.currentTimeMillis());
            addressRepository.save(address);
        } else {
            Address exist = getById(id);
            exist.setIsDefault(address.getIsDefault());
            exist.copy(address);
            exist.setUpdateAt(System.currentTimeMillis());
            addressRepository.save(exist);
        }

    }

    @Override
    public Address address(Integer id) {
        return getById(id);
    }

    @Override
    public void removeOne(Integer id) {
        Address address = getById(id);
        addressRepository.delete(address);
    }

}
