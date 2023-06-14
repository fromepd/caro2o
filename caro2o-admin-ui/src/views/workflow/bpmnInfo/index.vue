<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程类型" prop="bpmnType">
        <el-select v-model="queryParams.bpmnType" placeholder="请选择流程类型" clearable>
          <el-option
            v-for="dict in dict.type.sys_audit_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="部署时间">
        <el-date-picker
          v-model="daterangeDeployTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="bpmnDesigner = true"
          v-hasPermi="['workflow:bpmnInfo:deploy']"
        >设计流程图
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleDeploy"
          v-hasPermi="['workflow:bpmnInfo:deploy']"
        >流程文件部署
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bpmnInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="流程名称" align="center" prop="bpmnLabel"/>
      <el-table-column label="流程定义 Key" align="center" prop="processDefinitionKey"/>
      <el-table-column label="部署时间" align="center" prop="deployTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deployTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="版本号" align="center" prop="version"/>
      <el-table-column label="描述信息" align="center" prop="info"/>
      <el-table-column label="流程文件" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            v-hasPermi="['workflow:bpmnInfo:readResource']"
            @click="bpmnView(scope.row.id)"
          >查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="流程图" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            v-hasPermi="['workflow:bpmnInfo:readResource']"
            @click="imageView(scope.row.id)"
          >查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-customer"
            @click="handleNodeList(scope.row.id)"
          >流程节点列表
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:bpmnInfo:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改流程定义对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流程类型" prop="bpmnType">
          <el-select v-model="form.bpmnType" placeholder="请选择流程类型" clearable>
            <!-- Vuex -->
            <el-option
              v-for="dict in dict.type.sys_audit_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="流程文件" prop="bpmnFile">
          <el-upload
            action="/"
            :limit="1"
            :auto-upload="false"
            :on-change="fileChange"
            :file-list="fileList"
          >
            <el-button size="small" type="success">选择文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传bpmn/xml文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注" prop="info">
          <el-input type="textarea" v-model="form.info" placeholder="请输入备注信息"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="流程图设计" :visible.sync="bpmnDesigner" width="1200px" append-to-body>
      <vue-bpmn style="overflow: hidden; height: 570px;" product="activiti"></vue-bpmn>
    </el-dialog>

    <el-dialog title="流程图" :visible.sync="bpmnImageDialog" width="1200px" append-to-body>
      <div v-html="currentBpmnImage"/>
    </el-dialog>

    <el-dialog title="流程文件" :visible.sync="bpmnDialog" width="1200px" append-to-body>
      <xml-code-mirror :read-only="false" :value="currentBpmnFile"/>
    </el-dialog>
  </div>
</template>

<script>
import { getResource, delBpmnInfo, deploy, listBpmnInfo } from '@/api/workflow/bpmnInfo'
import VueBpmn from '@/components/bpmn/VueBpmn'
import XmlCodeMirror from '@/components/XmlCodeMirror'

export default {
  name: 'BpmnInfo',
  dicts: ['sys_audit_type'],
  components: {
    VueBpmn,
    XmlCodeMirror
  },
  data() {
    return {
      bpmnDesigner: false,
      bpmnImageDialog: false,
      bpmnDialog: false,
      currentBpmnImage: null, // 当前流程定义的图片数据
      currentBpmnFile: null, // 当前流程定义文件数据
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 流程定义表格数据
      bpmnInfoList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 描述信息时间范围
      daterangeDeployTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bpmnType: null,
        deployTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        bpmnType: [
          { required: true, message: '请选择流程类型', trigger: 'blur' }
        ]
      },
      fileList: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询流程定义列表 */
    getList() {
      this.loading = true
      this.queryParams.params = {}
      if (null != this.daterangeDeployTime && '' != this.daterangeDeployTime) {
        this.queryParams.params['beginDeployTime'] = this.daterangeDeployTime[0]
        this.queryParams.params['endDeployTime'] = this.daterangeDeployTime[1]
      }
      listBpmnInfo(this.queryParams).then(response => {
        this.bpmnInfoList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    imageView(id) {
      let type = 'image'
      // 发起请求到后台, 获取数据
      getResource(id, type).then(res => {
        this.currentBpmnImage = res
        this.bpmnImageDialog = true
      })
    },
    bpmnView(id) {
      let type = 'bpmn'
      // 发起请求到后台, 获取数据
      getResource(id, type).then(res => {
        this.currentBpmnFile = res
        this.bpmnDialog = true
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        bpmnLabel: null,
        bpmnType: null,
        processDefinitionKey: null,
        deployTime: null,
        version: null,
        info: null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeDeployTime = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleDeploy() {
      this.reset()
      this.open = true
      this.title = '部署流程定义'
    },
    fileChange(file, fileList) {
      this.fileList = fileList
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.fileList.length <= 0) {
            this.$modal.msgError('请选择要部署的流程文件')
            return
          }
          // 表单校验通过, 部署流程文件
          let formData = new FormData()
          formData.append('file', this.fileList[0].raw)
          formData.append('bpmnType', this.form.bpmnType)
          formData.append('info', this.form.info)

          // 发起请求
          deploy(formData).then(res => {
            // 刷新表格
            this.resetQuery()
            // 清空fileList的数据
            this.fileList = []
            // 关闭弹窗
            this.open = false
          })
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除流程定义编号为"' + ids + '"的数据项？').then(function() {
        return delBpmnInfo(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    handleNodeList(id) {
      this.$router.push({path: `/flow/bpmnNode?id=${id}`})
    }
  }
}
</script>
