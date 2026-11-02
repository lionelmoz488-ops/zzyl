package com.zzyl.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.nursing.domain.NursingProjectPlan;
import com.zzyl.nursing.vo.NursingProjectPlanVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 护理计划和项目关联Mapper接口
 *
 * @author ruoyi
 * @date 2026-11-01
 */
@Mapper
public interface NursingProjectPlanMapper extends BaseMapper<NursingProjectPlan>
{
    /**
     * 按计划、项目及执行规则查询关联列表。
     */
    List<NursingProjectPlan> selectNursingProjectPlanList(NursingProjectPlan nursingProjectPlan);

    /**
     * 批量保存指定护理计划的项目关联，计划ID使用主表保存后生成的ID。
     */

    int batchInsert(@Param("projectPlans") List<NursingProjectPlan> projectPlans, @Param("planId") Long id);

    /**
     * 根据护理计划ID查询关联护理项目列表
     * @param planId 护理计划ID
      * @return 护理项目列表
     */

    List<NursingProjectPlanVo> selectByNursingPlanId(@Param("planId")Long planId);

    /**
     * 根据护理计划ID删除关联
     * @param planId 护理计划ID
     * @return 结果
     */
    @Delete("DELETE FROM nursing_project_plan WHERE plan_id = #{planId}")
    void deleteByNursingPlanId(@Param("planId")Long planId);
}
