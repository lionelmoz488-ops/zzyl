package com.zzyl.nursing.service;

import java.util.List;
import com.baomidou.mybatisplus.spring.service.IService;
import com.zzyl.nursing.domain.NursingPlan;
import com.zzyl.nursing.dto.NursingPlanDto;
import com.zzyl.nursing.vo.NursingPlanVo;

/**
 * 护理计划Service接口
 * 
 * @author pcj
 * @date 2026-10-30
 */
public interface INursingPlanService extends IService<NursingPlan>
{
    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划集合
     */
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan);

    /**
     * 新增护理计划
     * 
     * @param nursingPlanDto 护理计划
     * @return 结果
     */
    public int insertNursingPlanDto(NursingPlanDto nursingPlanDto);

    NursingPlanVo selectNursingPlanById(Long id);

    int updateNursingPlan(NursingPlanDto nursingPlanDto);

    int deleteNursingPlanById(Long id);
}
