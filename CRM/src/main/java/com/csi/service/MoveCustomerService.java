package com.csi.service;

import java.util.List;

public interface MoveCustomerService {

    void batchPush(List<Integer> customerIds, int targetManagerId, int operatorId ,String ip);

}
