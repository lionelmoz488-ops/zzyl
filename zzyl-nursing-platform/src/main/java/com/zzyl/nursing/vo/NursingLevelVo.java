package com.zzyl.nursing.vo;

import com.zzyl.nursing.domain.NursingLevel;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "护理等级列表信息")
public class NursingLevelVo extends NursingLevel {

    @Schema(description = "护理计划名称")
    private String planName;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}