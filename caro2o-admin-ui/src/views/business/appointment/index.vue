<template>
  <div class="app-container">
    <!--  搜索条件表单  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="模糊搜索:">
        <el-input v-model="queryParams.keyword" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="电话号码:">
        <el-input v-model="queryParams.customerPhone" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="车牌号码:">
        <el-input v-model="queryParams.licensePlate" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
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
    <el-table :data="infoList" style="width: 100%">
      <el-table-column prop="customerName" label="客户名称" width="80"/>
      <el-table-column prop="customerPhone" label="联系方式" width="110"/>
      <el-table-column prop="appointmentTime" label="预约时间" width="150"/>
      <el-table-column prop="actualArrivalTime" label="到店时间" width="150"/>
      <el-table-column prop="licensePlate" label="车牌号码"/>
      <el-table-column prop="carSeries" label="汽车类型"/>
      <el-table-column prop="serviceType" label="服务类型">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cmi_service_type" :value="scope.row.serviceType"/>
        </template>
      </el-table-column>
      <el-table-column prop="info" label="备注" :show-overflow-tooltip="true">
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cmi_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column prop="handle" label="操作" width="260" align="center">
        <template slot-scope="scope" v-if="scope.row.id !== 1">
          <el-button size="mini" type="text" icon="el-icon-edit" :v-if="scope.row.status !== 0"
                     @click="handleUpdate(scope.row)"
          >编辑</el-button>

          <el-button size="mini" type="text" icon="el-icon-delete" :v-if="scope.row.status !== 0"
                     @click="handleArrive(scope.row)"
          >到店</el-button>

          <el-button size="mini" type="text" icon="el-icon-delete" :v-if="scope.row.status === 0 || scope.row.status === 2"
                     @click="handleClosing(scope.row)"
          >结算单</el-button>

          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
                       v-if="scope.row.status === 0">
            <span class="el-dropdown-link">
              <i class="el-icon-d-arrow-right el-icon--right"></i>更多
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleCancel" icon="el-icon-user">取消</el-dropdown-item>
              <el-dropdown-item command="handleDelData" icon="el-icon-delete">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!--  表格分页栏  -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>


    <!-- 添加或编辑预约对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" status-icon>
        <el-form-item label="客户姓名：" prop="customerName">
          <el-input v-model="form.customerName"/>
        </el-form-item>
        <el-form-item label="联系方式：" prop="customerPhone">
          <el-input v-model="form.customerPhone" type="tel"/>
        </el-form-item>
        <el-form-item label="预约时间：" prop="appointmentTime">
          <el-date-picker v-model="form.appointmentTime" type="datetime" :picker-options="pickerOptions"
                          value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="车牌号码：" prop="licensePlate">
          <el-input v-model="form.licensePlate"/>
        </el-form-item>
        <el-form-item label="汽车类型：" prop="carSeries">
          <el-input v-model="form.carSeries"/>
        </el-form-item>
        <el-form-item label="服务类型：" prop="serviceType">
          <el-select v-model="form.serviceType" clearable>
            <el-option v-for="dict in dict.type.cmi_service_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注信息：" prop="info">
          <el-input v-model="form.info"/>
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
import {validatePhone, validateLicensePlate} from '@/utils/validate'
import {listAppointment, addAppointment, getAppointment, updateAppointment, removeAppointment, generateStatement, cancelAppointment, arrival} from '@/api/business/appointment'

export default {
  name: "appointmentInfo",
  dicts: ['cmi_service_type', 'cmi_status'],
  data() {
    return {
      //限制时间选择当日之前时间
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      bpmnDegigner: false,
      // 遮罩层
      loading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 预约表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyword: undefined,
        customerPhone: undefined,
        licensePlate: undefined

      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerName: [
          {required: true, message: "客户名称不能为空", trigger: "blur"}
        ],
        customerPhone: [
          {required: true, message: "联系方式不能为空", trigger: "blur"},
          {validator: validatePhone, trigger: 'blur'}
        ],
        appointmentTime: [
          {required: true, message: "预约时间不能为空", trigger: "blur"}
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
    /**  查询修养信息预约列表 */
    getList() {
      this.loading = true;
      listAppointment(this.queryParams).then(response => {
          this.infoList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        customerName: undefined,
        customerPhone: undefined,
        appointmentTime: undefined,
        licensePlate: undefined,
        carSeries: undefined,
        serviceType: undefined,
        info: undefined,
      };
      this.resetForm("form");
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      // this.resetForm("queryForm");  //看不懂是啥，好像没用的
      this.queryParams = {}
    },

    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleDelData":
          this.handleDelData(row);
          break;
        case "handleCancel":
          this.handleCancel(row);
          break;
        default:
          break;
      }
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加预约信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getAppointment(id).then(response => {
        this.form = response.data;
        this.form.serviceType = this.form.serviceType.toString();
        this.open = true;
        this.title = "修改预约信息";
      });
    },
    /** 删除按钮操作 */
    handleDelData(row) {
      this.reset();
      const id = row.id;
      this.$modal.confirm('确定删除该条养修服务单项信息吗？').then(function () {
        return removeAppointment(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 取消操作操作 */
    handleCancel: function (row) {
      const id = row.id;
      this.$modal.confirm('用户是否取消预约?').then(function () {
        return cancelAppointment(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("取消成功");
      }).catch(() => {
      });
    },
    /** 到店按钮操作*/
    handleArrive(row) {
      const id = row.id;
      this.$modal.confirm('客户是否到店?').then(function () {
        return arrival(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => {
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        console.log(valid);
        if (valid) {
          if (this.form.id != undefined) {

            updateAppointment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            console.log('this.form');
            this.form.id = this.total;
            addAppointment(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 结算单按钮操作 */
    handleClosing(row) {
      const id = row.id;
      let _this = this
      this.$modal.confirm('是否需要生成结算单?').then(function () {
        return generateStatement(id);
      }).then(response => {
        _this.$router.push({path: "/business/statement/item", query: {id: response.data}})
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
