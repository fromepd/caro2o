<template>
  <div class="app-container">
    <!--  搜索条件表单  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="到店时间">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!--  表格头部按钮  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <!--  表格内容  -->
    <el-table v-loading="loading" :data="statementList">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="客户姓名" prop="customerName"/>
      <el-table-column label="联系方式" prop="customerPhone" width="120"/>
      <el-table-column label="到店时间" prop="actualArrivalTime" width="150"/>
      <el-table-column label="车牌号码" prop="licensePlate"/>
      <el-table-column label="汽车类型" prop="carSeries"/>
      <el-table-column label="服务类型" prop="serviceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.si_service_catalog" :value="scope.row.serviceType"/>
        </template>
      </el-table-column>
      <el-table-column label="预约用户" prop="appointmentId" align="center">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.appointment_status" :value="scope.row.appointmentId"/>
        </template>
      </el-table-column>
      <el-table-column label="结算状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.statement_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" :disabled="scope.row.payeeId == 1"
                     @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-notebook-2" @click="handleClosing(scope.row)">明细
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" :disabled="scope.row.payeeId == 1"
                     @click="handleDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--  表格分页栏  -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改服务结算单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="客户姓名：" prop="customerName">
          <el-input v-model="form.customerName"/>
        </el-form-item>
        <el-form-item label="联系方式：" prop="customerPhone">
          <el-input v-model="form.customerPhone"/>
        </el-form-item>
        <el-form-item label="到店时间：" prop="actualArrivalTime">
          <el-date-picker v-model="form.actualArrivalTime" type="datetime" :picker-options="pickerOptions"
                          placeholder="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="车牌号码：" prop="licensePlate">
          <el-input v-model="form.licensePlate"/>
        </el-form-item>
        <el-form-item label="汽车类型：" prop="carSeries">
          <el-input v-model="form.carSeries"/>
        </el-form-item>
        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="form.serviceType" clearable>
            <el-option v-for="dict in dict.type.si_service_catalog" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注信息：" prop="info">
          <el-input type="textarea" row="4" v-model="form.info"/>
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
import {
  listStatement,
  addStatement,
  getStatement,
  removeStatement,
  updateStatement
} from "@/api/business/statement";
import {validatePhone, validateLicensePlate} from '@/utils/validate';

export default {
  name: "statement",
  dicts: ['si_service_catalog', 'statement_status', 'appointment_status'],
  data() {
    return {
      //限制时间选择当日之前时间
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startTime: undefined,
        endTime: undefined
      },
      statementList: [],
      // 表单参数
      form: {},
      appointmentStatus: [],
      // 表单校验
      rules: {
        customerName: [
          {required: true, message: "客户名称不能为空", trigger: "blur"}
        ],
        customerPhone: [
          {required: true, message: "联系方式不能为空", trigger: "blur"},
          {validator: validatePhone, trigger: 'blur'}
        ],
        actualArrivalTime: [
          {required: true, message: "到店时间不能为空", trigger: "blur"}
        ],
        licensePlate: [
          {required: true, message: "车牌号码不能为空", trigger: "blur"},
          {validator: validateLicensePlate, trigger: 'blur'}
        ],
        carSeries: [
          {required: true, message: "汽车类型不能为空", trigger: "blur"}
        ],
        serviceType: [
          {required: true, message: "服务类型不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /**查询项目服务结算列表 */
    getList() {
      this.loading = true;
      listStatement(this.queryParams).then(response => {
          this.statementList = response.rows;
          response.rows.map((item, index) => {
            if (item.appointmentId == undefined || item.appointmentId.length == 0) {
              this.statementList[index].appointmentId = 0
            } else {
              this.statementList[index].appointmentId = 1
            }
          })
          this.total = response.total;
          this.loading = false;
        }
      );
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
      this.queryParams.startTime = this.dateRange[0];
      this.queryParams.endTime = this.dateRange[1];
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加结算单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getStatement({id}).then(response => {
        delete response.data.createTime;
        delete response.data.payTime;
        this.form = response.data;
        this.form.serviceType = this.form.serviceType.toString();
        this.open = true;
        this.title = "修改结算单";
      });
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        if (this.form.id) {
          updateStatement(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
        } else {
          addStatement(this.form).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },

    /** 结算单按钮操作 */
    handleClosing(row) {
      const id = row.id;
      this.$router.push({path: "/business/statement/item", query: {id}})
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('确认此条消费单吗?此操作不可逆!').then(function () {
        return removeStatement({id});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  }
};
</script>
<style lang="scss" scoped>
.el-date-editor.el-input {
  width: 100% !important;
}

.el-select {
  width: 100%;
}
</style>
