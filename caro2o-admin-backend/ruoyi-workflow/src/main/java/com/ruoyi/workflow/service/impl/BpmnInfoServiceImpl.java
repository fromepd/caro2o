package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.workflow.domain.BpmnInfo;
import com.ruoyi.workflow.mapper.BpmnInfoMapper;
import com.ruoyi.workflow.service.IBpmnInfoService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程定义Service业务层处理
 *
 * @author wang
 * @date 2022-11-14
 */
@Service
public class BpmnInfoServiceImpl extends ServiceImpl<BpmnInfoMapper, BpmnInfo> implements IBpmnInfoService {

    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessDiagramGenerator processDiagramGenerator;

    /**
     * 查询流程定义列表
     *
     * @param bpmnInfo 流程定义
     * @return 流程定义
     */
    @Override
    public List<BpmnInfo> selectBpmnInfoList(BpmnInfo bpmnInfo) {
        return getBaseMapper().selectBpmnInfoList(bpmnInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deploy(BpmnInfo bpmnInfo, InputStream bpmnFile) {
        // 1. 获取流程定义的类型标签
        List<SysDictData> dictDataList = dictTypeService.selectDictDataByType("sys_audit_type");
        for (SysDictData data : dictDataList) {
            // 当前字典的值是否为当前流程类型的值
            if (data.getDictValue().equals(bpmnInfo.getBpmnType())) {
                bpmnInfo.setBpmnLabel(data.getDictLabel());
                break;
            }
        }

        // 2. 调用 activiti 进行流程部署
        Deployment deployment = null;
        String suffix = FileUtils.getSuffix(bpmnInfo.getResourceName());

        if ("zip".equals(suffix)) {
            deployment = repositoryService.createDeployment()
                    // classpath 下的资源路径
                    .addZipInputStream(new ZipInputStream(bpmnFile)) // 上传 zip 格式的文件
                    .name(bpmnInfo.getBpmnLabel())
                    .deploy();
        } else {
            deployment = repositoryService.createDeployment()
                    // classpath 下的资源路径
                    .addInputStream(bpmnInfo.getResourceName(), bpmnFile)
                    .name(bpmnInfo.getBpmnLabel())
                    .deploy();
        }

        // 3. 保存部署后的相关属性
        bpmnInfo.setDeploymentId(deployment.getId());
        bpmnInfo.setDeployTime(deployment.getDeploymentTime());
        String definitionKey = bpmnInfo.getBpmnType();
        bpmnInfo.setProcessDefinitionKey(definitionKey);

        // 4. 基于流程定义 key 查询流程定义对象
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(definitionKey)
                .deploymentId(deployment.getId())
                .singleResult();

        if (definition == null) {
            throw new ServiceException("流程定义 Key 不正确！");
        }

        bpmnInfo.setVersion(Long.parseLong(definition.getVersion() + ""));
        // 5. 保存 bpmnInfo 对象
        super.save(bpmnInfo);
    }

    @Override
    public BpmnInfo getById(Serializable id) {
        BpmnInfo bpmnInfo = super.getById(id);
        if (bpmnInfo == null) {
            throw new ServiceException("参数错误, 流程信息不存在");
        }
        return bpmnInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteDefinition(Long[] ids) {
        for (Long id : ids) {
            // 1. 基于流程信息 id 查询到流程信息对象
            BpmnInfo bpmnInfo = this.getById(id);

            try {
                // 2. 基于流程信息中的部署 id, 调用 activiti 的 api 来删除部署信息
                repositoryService.deleteDeployment(bpmnInfo.getDeploymentId(), true);
            } catch (Exception e) {
                e.printStackTrace();
                // 3. 如果删除失败, 提示前端有正在进行中的流程, 无法删除
                throw new ServiceException("删除失败, 当前存在正在进行的审核流程");
            }
        }

        // 4. 批量删除 bpmnInfo 的数据
        return super.removeBatchByIds(Arrays.asList(ids));
    }

    @Override
    public InputStream getResourceByType(Long id, String type) {
        // 1. 基于流程信息id查询流程信息对象
        BpmnInfo bpmnInfo = this.getById(id);

        // 2. 基于流程部署id查询流程定义对象
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(bpmnInfo.getDeploymentId())
                .singleResult();

        // 3. 判断当前要查询数据的类型
        // 4. 如果当前类型是 image, 就查询流程图
        if ("image".equals(type)) {
            // 4.1 通过流程定义对象的图片资源名称, 判断是否有图片资源
            if (StringUtils.isNotEmpty(definition.getDiagramResourceName())) {
                // 4.2 如果有图片, 直接通过仓库 Service 接口获取图片流对象并返回
                return repositoryService.getResourceAsStream(bpmnInfo.getDeploymentId(), definition.getDiagramResourceName());
            } else {
                // 4.3 获取 bpmn 模型对象
                BpmnModel bpmnModel = repositoryService.getBpmnModel(definition.getId());
                // 4.4 如果没有图片, 通过流程图生成工具, 生成流程图, 得到流对象并返回
                return processDiagramGenerator.generateDiagram(
                        bpmnModel,
                        "宋体",
                        "宋体",
                        "宋体"
                );
            }
        } else if ("bpmn".equals(type)) {
            // 5. 如果类型是 bpmn, 直接通过仓库 Service 获取文件流对象并返回
            return repositoryService.getResourceAsStream(bpmnInfo.getDeploymentId(), definition.getResourceName());
        }

        // 6. 如果是其他类型, 直接报错提示不支持的类型
        throw new ServiceException("当前查看的文件类型不支持");
    }
}
