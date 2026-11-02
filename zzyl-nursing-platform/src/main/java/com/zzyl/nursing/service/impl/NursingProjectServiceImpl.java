package com.zzyl.nursing.service.impl;

import com.zzyl.common.utils.DateUtils;
import com.zzyl.nursing.domain.NursingProject;
import com.zzyl.nursing.mapper.NursingProjectMapper;
import com.zzyl.nursing.service.INursingProjectService;
import com.zzyl.nursing.vo.NursingProjectVo;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zzyl.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 护理项目Service业务层处理
 * 
 * @author pcj
 * @date 2026-10-30
 */
@Service
public class NursingProjectServiceImpl extends ServiceImpl<NursingProjectMapper, NursingProject> implements INursingProjectService
{
    /**
     * 查询护理项目列表
     * 
     * @param nursingProject 护理项目
     * @return 护理项目
     */
    @Override
    public List<NursingProject> selectNursingProjectList(NursingProject nursingProject)
    {
        return baseMapper.selectList(Wrappers.<NursingProject>lambdaQuery()
                .like(StringUtils.isNotEmpty(nursingProject.getName()), NursingProject::getName, nursingProject.getName())
                .eq(nursingProject.getStatus() != null, NursingProject::getStatus, nursingProject.getStatus()));
    }

    /**
     * 新增护理项目
     * 
     * @param nursingProject 护理项目
     * @return 结果
     */
    @Override
    public int insertNursingProject(NursingProject nursingProject)
    {
        nursingProject.setId(null);
        nursingProject.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(nursingProject);
    }

    /**
     * 修改护理项目
     * 
     * @param nursingProject 护理项目
     * @return 结果
     */
    @Override
    public int updateNursingProject(NursingProject nursingProject)
    {
        nursingProject.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(nursingProject);
    }

    /**
     * 查询所有护理项目列表
     *
     *
     */
    @Override
    public List<NursingProjectVo> getAll() {
        return baseMapper.getAll();
    }
}
