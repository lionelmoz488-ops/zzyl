package com.zzyl.nursing.service;

import java.util.List;
import com.baomidou.mybatisplus.spring.service.IService;
import com.zzyl.nursing.domain.NursingLevel;

/**
 * 护理等级Service接口
 * 
 * @author pcj
 * @date 2026-10-30
 */
public interface INursingLevelService extends IService<NursingLevel>
{
    /**
     * 查询护理等级列表
     * 
     * @param nursingLevel 护理等级
     * @return 护理等级集合
     */
    public List<NursingLevel> selectNursingLevelList(NursingLevel nursingLevel);

    /**
     * 新增护理等级
     * 
     * @param nursingLevel 护理等级
     * @return 结果
     */
    public int insertNursingLevel(NursingLevel nursingLevel);

    /**
     * 修改护理等级
     * 
     * @param nursingLevel 护理等级
     * @return 结果
     */
    public int updateNursingLevel(NursingLevel nursingLevel);

}
