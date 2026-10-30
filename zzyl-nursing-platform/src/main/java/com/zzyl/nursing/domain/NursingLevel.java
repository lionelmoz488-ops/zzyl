package com.zzyl.nursing.domain;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.annotation.Excel;
import com.zzyl.common.core.domain.NursingEntity;

/**
 * 护理等级对象 nursing_level
 *
 * @author pcj
 * @date 2026-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "护理等级")
@TableName("nursing_level")
public class NursingLevel extends NursingEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(description = "主键ID")
    private Long id;

    /** 等级名称 */
    @Schema(description = "等级名称")
    @Excel(name = "等级名称")
    private String name;

    /** 护理计划ID */
    @Schema(description = "护理计划ID")
    @Excel(name = "护理计划ID")
    private Long planId;

    /** 护理费用 */
    @Schema(description = "护理费用")
    @Excel(name = "护理费用")
    private BigDecimal fee;

    /** 状态（0：禁用，1：启用） */
    @Schema(description = "状态（0：禁用，1：启用）")
    @Excel(name = "状态", readConverterExp = "0=：禁用，1：启用")
    private Integer status;

    /** 等级说明 */
    @Schema(description = "等级说明")
    @Excel(name = "等级说明")
    private String description;

}
