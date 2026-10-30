package com.zzyl.nursing.controller;

import com.zzyl.common.annotation.Log;
import com.zzyl.common.core.controller.BaseController;
import com.zzyl.common.core.domain.AjaxResult;
import com.zzyl.common.core.page.TableDataInfo;
import com.zzyl.common.enums.BusinessType;
import com.zzyl.common.utils.poi.ExcelUtil;
import com.zzyl.nursing.domain.NursingProject;
import com.zzyl.nursing.service.INursingProjectService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 护理项目Controller
 * 负责接收护理项目的查询、导出和增删改请求，具体业务处理委托给 Service 层。
 * 各接口通过 @PreAuthorize 校验操作权限，导出和增删改通过 @Log 记录操作日志。
 * 
 * @author pcj
 * @date 2026-10-30
 */
@RestController
@RequestMapping("/nursing/project")
public class NursingProjectController extends BaseController
{
    @Autowired
    private INursingProjectService nursingProjectService;

    /**
     * 查询护理项目列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(NursingProject nursingProject)
    {
        // 从请求读取分页参数，必须在列表查询前调用，使紧随其后的查询按分页条件执行。
        startPage();
        // Spring 将请求中的查询条件绑定到 nursingProject，Service 据此筛选护理项目。
        List<NursingProject> list = nursingProjectService.selectNursingProjectList(nursingProject);
        // 将当前页记录和符合条件的总记录数封装为前端表格所需的响应。
        return getDataTable(list);
    }

    /**
     * 导出护理项目列表
     */
    @PreAuthorize("@ss.hasPermi('nursing:project:export')")
    @Log(title = "护理项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NursingProject nursingProject)
    {
        // 导出不调用 startPage()，查询符合筛选条件的全部记录。
        List<NursingProject> list = nursingProjectService.selectNursingProjectList(nursingProject);
        // 根据实体字段上的 @Excel 注解生成导出列，并将 Excel 文件写入 HTTP 响应供下载。
        ExcelUtil<NursingProject> util = new ExcelUtil<NursingProject>(NursingProject.class);
        util.exportExcel(response, list, "护理项目数据");
    }

    /**
     * 获取护理项目详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursing:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        // 从 URL 路径获取项目主键，查询结果放入统一成功响应的 data 字段。
        return success(nursingProjectService.selectNursingProjectById(id));
    }

    /**
     * 新增护理项目
     */
    @PreAuthorize("@ss.hasPermi('nursing:project:add')")
    @Log(title = "护理项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NursingProject nursingProject)
    {
        // @RequestBody 将 JSON 请求体转为实体；Service 补充创建时间并执行新增。
        // toAjax 根据受影响行数返回操作结果：大于 0 为成功，否则为失败。
        return toAjax(nursingProjectService.insertNursingProject(nursingProject));
    }

    /**
     * 修改护理项目
     */
    @PreAuthorize("@ss.hasPermi('nursing:project:edit')")
    @Log(title = "护理项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NursingProject nursingProject)
    {
        // 请求体携带项目主键和待修改字段，Service 补充更新时间后执行更新。
        // 更新影响行数通过 toAjax 转换为统一的成功或失败响应。
        return toAjax(nursingProjectService.updateNursingProject(nursingProject));
    }

    /**
     * 删除护理项目
     */
    @PreAuthorize("@ss.hasPermi('nursing:project:remove')")
    @Log(title = "护理项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 将路径中逗号分隔的主键（如 /1,2,3）绑定为数组，交给 Service 批量删除。
        // 只要受影响行数大于 0，toAjax 就返回成功，并不要求所有传入主键都存在。
        return toAjax(nursingProjectService.deleteNursingProjectByIds(ids));
    }
}
