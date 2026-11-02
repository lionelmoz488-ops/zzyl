package com.zzyl.nursing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.nursing.domain.NursingProject;
import com.zzyl.nursing.vo.NursingProjectVo;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface NursingProjectMapper extends BaseMapper<NursingProject>
{
    @Select("SELECT name label, id value FROM nursing_project WHERE status = 1")
    List<NursingProjectVo> getAll();
}
