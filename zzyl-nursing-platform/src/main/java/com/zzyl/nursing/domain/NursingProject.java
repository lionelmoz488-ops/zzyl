package com.zzyl.nursing.domain;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzyl.common.annotation.Excel;
import com.zzyl.common.core.domain.NursingEntity;

/**
 * 护理项目对象 nursing_project
 *
 * @author pcj
 * @date 2026-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "护理项目")
@TableName("nursing_project")
public class NursingProject extends NursingEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Schema(description = "编号")
    private Long id;

    /** 名称 */
    @Schema(description = "名称")
    @Excel(name = "名称")
    private String name;

    /** 排序号 */
    @Schema(description = "排序号")
    @Excel(name = "排序号")
    private Integer orderNo;

    /** 单位 */
    @Schema(description = "单位")
    @Excel(name = "单位")
    private String unit;

    /** 价格 */
    @Schema(description = "价格")
    @Excel(name = "价格")
    private BigDecimal price;

    /** 图片 */
    @Schema(description = "图片")
    @Excel(name = "图片")
    private String image;

    /** 护理要求 */
    @Schema(description = "护理要求")
    @Excel(name = "护理要求")
    private String nursingRequirement;

    /** 状态（0：禁用，1：启用） */
    @Schema(description = "状态（0：禁用，1：启用）")
    @Excel(name = "状态", readConverterExp = "0=：禁用，1：启用")
    private Integer status;

}
