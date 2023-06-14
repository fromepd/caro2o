<template>
  <div class="app-container">
    <!--  搜索条件表单  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="回访客户" prop="customerId">
        <el-select v-model="queryParams.customerId" clearable filterable @change="handleQuery">
          <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="回访方式" prop="visitMethod">
        <el-select v-model="queryParams.visitMethod" clearable filterable @change="handleQuery">
          <el-option v-for="dict in dict.type.customer_visit_method" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="录入人" prop="inputUserId">
        <el-select v-model="queryParams.inputUserId" clearable filterable @change="handleQuery">
          <el-option v-for="u in users" :key="u.userId" :label="u.nickName" :value="u.userId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="回访日期" prop="visitDate">
        <el-date-picker v-model="dateRange" style="width: 240px"
                        value-format="yyyy-MM-dd" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                        @change="handleQuery"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!--  表格头部按钮  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini"
                   @click="handleAdd" v-hasPermi="['customer:visit:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini"
                   @click="handleExport"
                   v-hasPermi="['customer:visit:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <!--  表格内容  -->
    <el-table v-loading="loading" :data="careList" @selection-change="handleSelectionChange">
      <el-table-column label="回访客户" align="center" prop="customerName"/>
      <el-table-column label="回访方式" align="center" prop="visitMethod">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.customer_visit_method" :value="scope.row.visitMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="回访原因" align="center" prop="visitCause"/>
      <el-table-column label="回访结果" align="center" prop="visitResult"/>
      <el-table-column label="回访日期" align="center" prop="visitDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="录入人" align="center" prop="inputUserName"/>
      <el-table-column label="录入时间" align="center" prop="entryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.entryTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
    </el-table>
    <!--  表格分页栏  -->
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改客户回访对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="回访客户" prop="customerId">
          <el-select v-model="form.customerId" clearable>
            <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="回访方式" prop="visitMethod">
          <el-select v-model="form.visitMethod" clearable>
            <el-option v-for="dict in dict.type.customer_visit_method" :key="dict.value" :label="dict.label" :value="dict.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="回访日期" prop="visitDate">
          <el-date-picker v-model="form.visitDate" clearable type="date" value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="回访原因" prop="visitCause">
          <el-input v-model="form.visitCause" type="textarea" rows="4" clearable placeholder="请输入回访原因"/>
        </el-form-item>

        <el-form-item label="回访结果" prop="visitResult">
          <el-input v-model="form.visitResult" type="textarea" rows="4" clearable placeholder="请输入回访结果"/>
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
import {listVisit, addVisit, listAllCustomer} from "@/api/customer/visit";
import {listAllUser} from "@/api/system/user";

export default {
  name: "customer-visit",
  dicts: ['customer_visit_method'],
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
      // 总条数
      total: 0,
      // 客户关怀表格数据
      careList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 录入人下拉数据
      users : [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerId: null,
        visitMethod: null,
        inputUserId: null,
      },
      customers: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerId: [
          {required: true, message: "回访客户不能为空", trigger: "blur"}
        ],
        visitMethod: [
          {required: true, message: "回访方式不能为空", trigger: "blur"}
        ],
        visitCause: [
          {required: true, message: "回访原因不能为空", trigger: "blur"}
        ],
        visitDate: [
          {required: true, message: "回访日期不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getCustomer();
    this.getUsers();
    this.getList();
  },
  methods: {
    /** 查询客户关怀列表 */
    getList() {
      this.loading = true;
      listVisit(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.careList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getCustomer() {
      listAllCustomer().then(response => {
        this.customers = response.data || []
      })
    },
    getUsers(){
      listAllUser().then(response => {
        this.users = response.data || [];
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加客户回访记录";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addVisit(this.form).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('customer/visit/export', {
        ...this.queryParams
      }, `客户回访记录_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
