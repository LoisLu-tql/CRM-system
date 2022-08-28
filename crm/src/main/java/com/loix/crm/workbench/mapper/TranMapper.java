package com.loix.crm.workbench.mapper;

import com.loix.crm.workbench.domain.FunnelVO;
import com.loix.crm.workbench.domain.Tran;

import java.util.List;

public interface TranMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Fri Jun 24 13:36:17 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Fri Jun 24 13:36:17 CST 2022
     */
    int insert(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Fri Jun 24 13:36:17 CST 2022
     */
    int insertSelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Fri Jun 24 13:36:17 CST 2022
     */
    Tran selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Fri Jun 24 13:36:17 CST 2022
     */
    int updateByPrimaryKeySelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Fri Jun 24 13:36:17 CST 2022
     */
    int updateByPrimaryKey(Tran record);

    int insertTran(Tran tran);

    Tran selectTranForDetailById(String id);

    List<FunnelVO> selectCountOfTranGroupByStage();

    List<Tran> selectAllTran();
}