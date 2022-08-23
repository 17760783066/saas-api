package com.example.demo.api.renew.service;

import com.example.demo.api.renew.model.Renew;
import com.example.demo.api.renew.qo.RenewQo;
import com.example.demo.api.renew.qo.RenewWo;

import org.springframework.data.domain.Page;

public interface RenewService {

    Page<Renew> renews(RenewQo qo, RenewWo wo);

    void renewFail(Integer id);

    void renewPass(Integer id);

	void editStatus(Renew renew);

}
