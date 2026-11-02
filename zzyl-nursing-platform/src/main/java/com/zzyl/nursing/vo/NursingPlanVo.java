package com.zzyl.nursing.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "护理计划")
public class NursingPlanVo {

    /**
     * 护理计划id
     */
    private Long id;

    /**
     * 排序号
     */
    @Schema(description = "护理计划排序号")
    private Integer sortNo;

    @Schema(description = "护理计划名称")
    private String planName;

    @Schema(description = "状态（0：禁用，1：启用）")
    private Integer status;

    @Schema(description = "护理计划项目列表")
    List<NursingProjectPlanVo> projectPlans;

}