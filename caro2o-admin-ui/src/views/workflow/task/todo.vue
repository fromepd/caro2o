<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="创建时间">
        <el-date-picker
            v-model="daterangeCreateTime"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="daterange"
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="auditList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="主键" align="center" prop="id"/>
      <el-table-column label="套餐名称" align="center" prop="serviceItemName"/>
      <el-table-column label="套餐备注" show-overflow-tooltip align="center" prop="serviceItemInfo"/>
      <el-table-column label="套餐价格" align="center" prop="serviceItemPrice"/>
      <el-table-column label="流程实例" align="center" prop="instanceId"/>
      <el-table-column label="创建者" align="center" prop="creatorName"/>
      <el-table-column label="当前任务" align="center" prop="taskName"/>
      <el-table-column label="审核人" align="center" prop="auditors">
        <template v-slot="scope">
          {{ scope.row.status === 0 ? scope.row.auditors : '无' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="备注" show-overflow-tooltip align="center" prop="info"/>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cp_audit_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              @click="handleAudit(scope.row)"
              v-if="scope.row.status === 0"
          >审批
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-refresh"
              v-if="scope.row.status === 1"
              @click="handleReapply(scope.row)"
          >重新提交
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEditServiceItem(scope.row)"
              v-if="scope.row.status === 1"
          >修改表单
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-tickets"
              v-hasPermi="['workflow:history:list']"
              @click="handleHistory(scope.row)"
          >审批历史
          </el-button>
          <el-button
              size="mini"
              type="text"
              @click="handleViewImage(scope.row)"
              v-hasPermi="['workflow:audit:resource']"
          >查看进度
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 审批弹框 -->
    <el-dialog title="审批任务" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="审批结果">
          <el-radio v-model="form.result" :label="true" border>通过</el-radio>
          <el-radio v-model="form.result" :label="false" border>拒绝</el-radio>
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input type="textarea" v-model="form.info" placeholder="请输入审批备注信息"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 编辑服务单项对话框 -->
    <el-dialog title="修改服务项" :visible.sync="serviceItemEditDialog" width="600px" append-to-body>
      <el-form ref="serviceItemForm" :model="serviceItemForm" :rules="serviceItemRules" label-width="150px">
        <el-form-item label="服务名称：" prop="name">
          <el-input v-model="serviceItemForm.name"/>
        </el-form-item>
        <el-form-item label="服务原价：" prop="originalPrice">
          <el-input v-model="serviceItemForm.originalPrice"/>
        </el-form-item>
        <el-form-item label="服务折扣价：" prop="discountPrice">
          <el-input v-model="serviceItemForm.discountPrice"/>
        </el-form-item>
        <el-form-item label="服务分类：" prop="serviceCatalog">
          <el-select size="medium" v-model="serviceItemForm.serviceCatalog">
            <el-option label="维修" value="0"></el-option>
            <el-option label="保养" value="1"></el-option>
            <el-option label="其他" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注信息：" prop="info">
          <el-input v-model="serviceItemForm.info"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitServiceItemForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="流程图" :visible.sync="bpmnImageDialog" width="1200px" append-to-body>
      <div v-html="currentBpmnImage"/>
    </el-dialog>
    <el-dialog title="审批历史" :visible.sync="historyListDialog" width="1200px" append-to-body>
      <el-table v-loading="loading" :data="historyList">
        <el-table-column label="任务名称" align="center" prop="activityName"/>
        <el-table-column label="处理人" align="center" prop="assigneeName"/>
        <el-table-column label="审批意见" align="center" prop="comment"/>
        <el-table-column label="开始时间" align="center" prop="startTime">
          <template v-slot="scope">
            {{ formatDate(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column label="结束时间" align="center" prop="endTime">
          <template v-slot="scope">
            {{ formatDate(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="耗时" align="center" prop="durationInMillis">
          <template v-slot="scope">
            {{ formatDuration(scope.row.durationInMillis) }}
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyListDialog = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {historyList} from "@/api/workflow/done";
import {getProcessingImage, listAudit} from "@/api/workflow/todo";
import {doAudit, reapply} from "@/api/workflow/audit";
import {getServiceItem, updateForAudit} from "@/api/business/serviceItem";

export default {
  name: "Audit",
  // Vuex
  dicts: ['cp_audit_status'],
  data() {
    return {
      historyListDialog: false, // 审批历史弹窗控制
      bpmnImageDialog: false, // 图片弹框是否展示
      currentBpmnImage: null, // 当前流程定义的图片数据
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
      // 套餐审核表格数据
      auditList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展示服务项编辑弹窗
      serviceItemEditDialog: false,
      // 创建时间时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        createTime: null
      },
      // 服务项校验
      serviceItemRules: {},
      // 服务项表单
      serviceItemForm: {},
      // 表单参数
      form: {
        result: true,
        info: null
      },
      // 表单校验
      rules: {},
      historyList: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询套餐审核列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' !== this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listAudit(this.queryParams).then(response => {
        this.auditList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.serviceItemEditDialog = false
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        result: true,
        info: null
      };
      this.resetForm("form");

      this.serviceItemForm = {}
      this.resetForm("serviceItemFrom");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAudit(row) {
      this.open = true
      this.form.id = row.id
      this.form.taskId = row.taskId
    },
    submitForm() {
      // 提交参数: 审核记录id/任务id/结果/备注信息
      doAudit(this.form).then(res => {
        // 刷新表格
        this.getList()
        // 提示消息
        this.$modal.msgSuccess('审核成功')
        // 关闭弹窗
        this.cancel()
      })
    },
    handleEditServiceItem(row) {
      this.reset();
      const id = row.serviceItemId;
      getServiceItem({ id }).then(response => {
        delete response.data.createTime;
        this.serviceItemForm = response.data;
        // 将审核记录的 id 携带到表单中
        this.serviceItemForm.auditId = row.id;
        this.serviceItemForm.serviceCatalog = this.serviceItemForm.serviceCatalog.toString();
        this.serviceItemEditDialog = true;
      });
    },
    handleReapply(row) {
      // 弹框提示用户重新申请
      this.$modal.confirm('确认重新提交吗?').then(function(data) {
        return reapply({id: row.id, taskId: row.taskId});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("重新提交成功");
      }).catch(() => {
        console.log("提交失败")
      });
    },
    submitServiceItemForm() {
      this.$refs["serviceItemForm"].validate(valid => {
        if (valid) {
          // 请求用于审核页面修改服务想的接口
          // 将当前审核记录的id放进去
          updateForAudit(this.serviceItemForm).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.cancel();
            this.getList();
          });
        }
      });
    },
    handleHistory(row) {
      historyList({instaanceId: row.instaanceId}).then(res => {
        this.historyList = res.data // 将后台查询到的数据存入当前页面
        this.historyListDialog = true // 显示弹框
      })
    },
    handleViewImage(row) {
      getProcessingImage(row.instanceId).then(res => {
        this.currentBpmnImage = res
        this.bpmnImageDialog = true
      })
    }
  }
};
</script>
