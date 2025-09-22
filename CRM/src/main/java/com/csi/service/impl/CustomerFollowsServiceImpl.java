package com.csi.service.impl;

import com.csi.domain.CustomerFollows;
import com.csi.mapper.CustomerFollowsMapper;
import com.csi.mapper.CustomerMapper;
import com.csi.service.CustomerFollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerFollowsServiceImpl implements CustomerFollowsService {

    @Autowired
    private CustomerFollowsMapper customerFollowsMapper;

    @Override
    public List<CustomerFollows> getAllCustomerFollows() {
        List<CustomerFollows> allCustomerFollows = customerFollowsMapper.getAllCustomerFollows();
        return allCustomerFollows;
    }

    @Override
    public List<CustomerFollows> getPersonalCustomerByTime(String startTime, String endTime) {
        List<CustomerFollows> customer = customerFollowsMapper.getPersonalCustomerByTime(startTime, endTime);
        return customer;
    }

    @Override
    public int addCustomerFollows(CustomerFollows customerFollows) {
        int i = customerFollowsMapper.addCustomerFollows(customerFollows);
        return i;
    }

    @Override
    public int updateCustomerFollows(CustomerFollows customerFollows) {
        int i = customerFollowsMapper.updateCustomerFollows(customerFollows);
        return i;
    }

    @Override
    public List<CustomerFollows> getCustomerFollowsByCustomerId(Integer customerId) {
        return customerFollowsMapper.getCustomerFollowsByCustomerId(customerId);
    }
}
