<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="节点" prop="nodeKey">
        <el-input
          v-model="queryParams.nodeKey"
          placeholder="请输入节点"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['workflow:bpmnNode:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['workflow:bpmnNode:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['workflow:bpmnNode:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['workflow:bpmnNode:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bpmnNodeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="流程信息" align="center" prop="bpmnInfoId" />
      <el-table-column label="节点 Key" align="center" prop="nodeKey" />
      <el-table-column label="节点描述" align="center" prop="nodeDesc" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['workflow:bpmnNode:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:bpmnNode:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 添加或修改流程定义节点对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="节点 Key" prop="nodeKey">
          <el-input v-model="form.nodeKey" placeholder="请输入节点 Key" />
        </el-form-item>
        <el-form-item label="节点描述" prop="nodeDesc">
          <el-input v-model="form.nodeDesc" placeholder="请输入节点描述" />
        </el-form-item>
        <el-form-item label="审核人员" prop="auditors">
          <el-select v-model="form.auditors" multiple filterable>
            <el-option v-for="u in users" :value="u.userId" :label="u.nickName || u.userName"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBpmnNode, getBpmnNode, delBpmnNode, addBpmnNode, updateBpmnNode } from "@/api/workflow/bpmnNode";
import { listAllUser } from '@/api/system/user'

export default {
  name: "BpmnNode",
  data() {
    return {
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
      // 流程定义节点表格数据
      bpmnNodeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        nodeKey: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        nodeKey: [
          { required: true, message: '请输入用户节点 Key', trigger: 'blur' }
        ],
        auditors: [
          { required: true, message: '请选择审核人员', trigger: 'blur' }
        ],
      },
      // 所有用户列表
      users: []
    };
  },
  created() {
    // 基于流程信息 id 查询流程节点列表
    this.queryParams.bpmnInfoId = this.$route.query.id
    this.getList();

    // 查询所有用户信息
    listAllUser().then(res => {
      this.users = res.data
    })
  },
  methods: {
    /** 查询流程定义节点列表 */
    getList() {
      this.loading = true;
      listBpmnNode(this.queryParams).then(response => {
        this.bpmnNodeList = response.data;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        bpmnInfoId: null,
        nodeKey: null,
        nodeDesc: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加流程定义节点";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBpmnNode(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改流程定义节点";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 提交之前, 获取到当前页面的 bpmnInfoId 设置到表单中
          this.form.bpmnInfoId = this.$route.query.id

          if (this.form.id != null) {
            updateBpmnNode(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBpmnNode(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除流程定义节点编号为"' + ids + '"的数据项？').then(function() {
        return delBpmnNode(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('workflow/bpmnNode/export', {
        ...this.queryParams
      }, `bpmnNode_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
