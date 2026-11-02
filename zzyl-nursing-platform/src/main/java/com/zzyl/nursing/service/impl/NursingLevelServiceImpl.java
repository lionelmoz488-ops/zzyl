package com.zzyl.nursing.service.impl;

import java.util.List;
import com.zzyl.common.utils.DateUtils;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zzyl.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import com.zzyl.nursing.mapper.NursingLevelMapper;
import com.zzyl.nursing.domain.NursingLevel;
import com.zzyl.nursing.service.INursingLevelService;
import com.zzyl.nursing.vo.NursingLevelVo;
/**
 * 护理等级Service业务层处理
 * 
 * @author pcj
 * @date 2026-10-30
 */
@Service
public class NursingLevelServiceImpl extends ServiceImpl<NursingLevelMapper, NursingLevel> implements INursingLevelService
{
    /**
     * 查询护理等级列表
     * 
     * @param nursingLevel 护理等级
     * @return 护理等级
     */
    @Override
    public List<NursingLevel> selectNursingLevelList(NursingLevel nursingLevel)
    {
        return baseMapper.selectList(Wrappers.<NursingLevel>lambdaQuery()
                .like(StringUtils.isNotEmpty(nursingLevel.getName()),
                        NursingLevel::getName, nursingLevel.getName())
                .eq(nursingLevel.getPlanId() != null,
                        NursingLevel::getPlanId, nursingLevel.getPlanId())
                .eq(nursingLevel.getStatus() != null,
                        NursingLevel::getStatus, nursingLevel.getStatus()));
    }

    /**
     * 新增护理等级
     * 
     * @param nursingLevel 护理等级
     * @return 结果
     */
    @Override
    public int insertNursingLevel(NursingLevel nursingLevel)
    {
        nursingLevel.setId(null);
        nursingLevel.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(nursingLevel);
    }

    /**
     * 修改护理等级
     * 
     * @param nursingLevel 护理等级
     * @return 结果
     */
    @Override
    public int updateNursingLevel(NursingLevel nursingLevel)
    {
        nursingLevel.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(nursingLevel);
    }

    @Override
    public List<NursingLevelVo> selectNursingLevelVoList(NursingLevel nursingLevel) {
        return baseMapper.selectNursingLevelVoList(nursingLevel);
    }

}
