package com.zzyl.nursing.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.annotation.Excel;
import com.zzyl.common.core.domain.NursingEntity;

/**
 * 护理计划对象 nursing_plan
 *
 * @author pcj
 * @date 2026-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "护理计划")
@TableName("nursing_plan")
public class NursingPlan extends NursingEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Schema(description = "编号")
    private Long id;

    /** 排序号 */
    @Schema(description = "排序号")
    private Integer sortNo;

    /** 名称 */
    @Schema(description = "名称")
    @Excel(name = "名称")
    private String planName;

    /** 状态 0禁用 1启用 */
    @Schema(description = "状态 0禁用 1启用")
    @Excel(name = "状态 0禁用 1启用")
    private Integer status;

}
