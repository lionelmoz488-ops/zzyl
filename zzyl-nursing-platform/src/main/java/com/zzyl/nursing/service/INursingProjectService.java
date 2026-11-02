package com.zzyl.nursing.service;

import com.zzyl.nursing.domain.NursingProject;
import com.zzyl.nursing.vo.NursingProjectVo;

import java.util.List;
import com.baomidou.mybatisplus.spring.service.IService;

/**
 * 护理项目Service接口
 * 
 * @author pcj
 * @date 2026-10-30
 */
public interface INursingProjectService extends IService<NursingProject>
{
    /**
     * 查询护理项目列表
     * 
     * @param nursingProject 护理项目
     * @return 护理项目集合
     */
    public List<NursingProject> selectNursingProjectList(NursingProject nursingProject);

    /**
     * 新增护理项目
     * 
     * @param nursingProject 护理项目
     * @return 结果
     */
    public int insertNursingProject(NursingProject nursingProject);

    /**
     * 修改护理项目
     * 
     * @param nursingProject 护理项目
     * @return 结果
     */
    public int updateNursingProject(NursingProject nursingProject);

    /**
     * 查询所有护理项目列表
     *
     */
    List<NursingProjectVo> getAll();
}
