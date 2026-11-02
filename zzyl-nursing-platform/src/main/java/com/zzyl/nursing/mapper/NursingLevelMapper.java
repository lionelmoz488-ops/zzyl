package com.zzyl.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.nursing.domain.NursingLevel;
import com.zzyl.nursing.vo.NursingLevelVo;

import java.util.List;

public interface NursingLevelMapper extends BaseMapper<NursingLevel> {

    List<NursingLevelVo> selectNursingLevelVoList(NursingLevel nursingLevel);
}