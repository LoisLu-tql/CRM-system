package com.loix.crm.workbench.service;

import com.loix.crm.workbench.domain.FunnelVO;
import com.loix.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    void saveCreateTran(Map<String, Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();

    List<Tran> queryAllTran();
}
