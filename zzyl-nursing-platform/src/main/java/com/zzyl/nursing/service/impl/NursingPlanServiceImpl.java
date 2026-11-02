package com.zzyl.nursing.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.zzyl.common.utils.DateUtils;
import com.zzyl.nursing.domain.NursingProjectPlan;
import com.zzyl.nursing.mapper.NursingProjectPlanMapper;
import com.zzyl.nursing.vo.NursingPlanVo;
import com.zzyl.nursing.vo.NursingProjectPlanVo;
import org.springframework.beans.BeanUtils;
import com.zzyl.nursing.dto.NursingPlanDto;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zzyl.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zzyl.nursing.mapper.NursingPlanMapper;
import com.zzyl.nursing.domain.NursingPlan;
import com.zzyl.nursing.service.INursingPlanService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 护理计划Service业务层处理
 * 
 * @author pcj
 * @date 2026-10-30
 */
@Service
public class NursingPlanServiceImpl extends ServiceImpl<NursingPlanMapper, NursingPlan> implements INursingPlanService
{
    @Autowired
    private NursingPlanMapper nursingPlanMapper;

    @Autowired
    private NursingProjectPlanMapper nursingProjectPlanMapper;

    @Override
    public NursingPlanVo selectNursingPlanById(Long id) {
        // 查询护理计划基本信息
        NursingPlan nursingPlan = baseMapper.selectById(id);

        //查询护理计划关联的护理项目集合
        List<NursingProjectPlanVo> projectPlans = nursingProjectPlanMapper.selectByNursingPlanId(id);

        //将两部分信息合并到一个对象中返回
        NursingPlanVo nursingPlanVo = new NursingPlanVo();
        BeanUtils.copyProperties(nursingPlan, nursingPlanVo);
        nursingPlanVo.setProjectPlans(projectPlans);
        return nursingPlanVo;
    }

    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划
     */


    @Override
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan)
    {
        return baseMapper.selectList(Wrappers.<NursingPlan>lambdaQuery()
                .like(StringUtils.isNotEmpty(nursingPlan.getPlanName()), NursingPlan::getPlanName, nursingPlan.getPlanName())
                .eq(nursingPlan.getStatus() != null, NursingPlan::getStatus, nursingPlan.getStatus()));
    }

    /**
     * 新增护理计划
     * 
      * @param nursingPlanDto 护理计划
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNursingPlanDto(NursingPlanDto nursingPlanDto)
    {
        //1.保存护理计划
        NursingPlan nursingPlan = new NursingPlan();
        BeanUtils.copyProperties(nursingPlanDto, nursingPlan);
        nursingPlan.setCreateTime(DateUtils.getNowDate());

        nursingPlanMapper.insert(nursingPlan);

        //2.批量保存护理计划和护理项目的对应关系
        int count = nursingProjectPlanMapper.batchInsert(nursingPlanDto.getProjectPlans(), nursingPlan.getId());

        return count == 0 ? 0 : 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNursingPlan(NursingPlanDto nursingPlanDto) {
        //判断dto中的护理项目是否为空，如果不为空，先删除该护理计划关联的所有护理项目，再重新批量保存最新修改的项目
        if (nursingPlanDto.getProjectPlans() != null && nursingPlanDto.getProjectPlans().size() > 0) {
            nursingProjectPlanMapper.deleteByNursingPlanId(nursingPlanDto.getId());
            nursingProjectPlanMapper.batchInsert(nursingPlanDto.getProjectPlans(), nursingPlanDto.getId());
        }
        NursingPlan nursingPlan = new NursingPlan();
        BeanUtils.copyProperties(nursingPlanDto, nursingPlan);
        return nursingPlanMapper.updateById(nursingPlan);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNursingPlanById(Long id) {
        //删除护理计划及其关联的护理项目
        nursingProjectPlanMapper.deleteByNursingPlanId(id);
        //删除护理计划
        return removeById(id) ? 1 : 0;
    }
}
