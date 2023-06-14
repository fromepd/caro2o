<template>
  <div class="app-container">
    <!--  搜索条件表单  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="名称:">
        <el-input v-model="queryParams.name" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="是否套餐:">
        <el-select v-model="queryParams.carPackage" clearable>
          <el-option v-for="dict in dict.type.si_car_package" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="服务分类:">
        <el-select v-model="queryParams.serviceCatalog" clearable>
          <el-option v-for="dict in dict.type.si_service_catalog" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态:">
        <el-select v-model="queryParams.auditStatus" clearable>
          <el-option v-for="dict in dict.type.si_audit_status" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="上架状态:">
        <el-select v-model="queryParams.saleStatus" clearable>
          <el-option v-for="dict in dict.type.si_sale_status" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!--  表格头部操作  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning"
                   plain icon="el-icon-download"
                   @click="handleAudit"
                   :disabled="!auditForm.id"
                   size="mini">发起审核</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <!--  表格  -->
    <el-table v-loading="loading" :data="serviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="服务名称" prop="name" />
      <el-table-column label="服务原价" prop="originalPrice" />
      <el-table-column label="折扣价" prop="discountPrice" />
      <el-table-column label="是否套餐" prop="carPackage">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.si_car_package" :value="scope.row.carPackage" />
        </template>
      </el-table-column>
      <el-table-column label="服务分类" prop="serviceCatalog">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.si_service_catalog" :value="scope.row.serviceCatalog" />
        </template>
      </el-table-column>
      <el-table-column label="审核状态" prop="auditStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.si_audit_status" :value="scope.row.auditStatus" />
        </template>
      </el-table-column>
      <el-table-column label="上架状态" prop="saleStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.si_sale_status" :value="scope.row.saleStatus" />
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="info" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="small" type="text" icon="el-icon-edit" :disabled="editStatus(scope.row)"
            @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="small" type="text" icon="el-icon-thumb" v-if="!scope.row.saleStatus"
            @click="handleAdded(scope.row)" :disabled="handleSaleStatus(scope.row)">上架</el-button>
          <el-button size="small" type="text" icon="el-icon-close" v-if="scope.row.saleStatus"
            @click="handleUnshelve(scope.row)">下架</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--  分页栏  -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或编辑服务单项对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="服务名称：" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="服务原价：" prop="originalPrice">
          <el-input v-model="form.originalPrice" />
        </el-form-item>
        <el-form-item label="服务折扣价：" prop="discountPrice">
          <el-input v-model="form.discountPrice" />
        </el-form-item>
        <el-form-item label="是否套餐：" prop="carPackage">
          <el-radio-group v-model="form.carPackage">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="服务分类：" prop="serviceCatalog">
          <el-select size="medium" v-model="form.serviceCatalog">
            <el-option label="维修" value="0"></el-option>
            <el-option label="保养" value="1"></el-option>
            <el-option label="其他" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注信息：" prop="info">
          <el-input v-model="form.info" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="发起套餐审核" :visible.sync="startAuditDialog" width="600px" append-to-body>
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="服务名称：" prop="name">
          <el-input disabled v-model="auditForm.name" />
        </el-form-item>
        <el-form-item label="服务原价：" prop="originalPrice">
          <el-input disabled v-model="auditForm.originalPrice" />
        </el-form-item>
        <el-form-item label="服务折扣价：" prop="discountPrice">
          <el-input disabled v-model="auditForm.discountPrice" />
        </el-form-item>
        <el-form-item label="备注：" prop="info">
          <el-input type="textarea" placeholder="请输入审核备注信息" v-model="auditForm.info" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listServiceItem,
  addServiceItem,
  getServiceItem,
  updateServiceItem,
  serviceItemSaleOn,
  serviceItemSaleOff,
} from "@/api/business/serviceItem";
import {startAudit} from '@/api/workflow/audit'

export default {
  name: "serviceItem",
  dicts: ['si_car_package', 'si_service_catalog', 'si_audit_status', 'si_sale_status'],
  data() {
    return {
      // 发起套餐审核弹窗
      startAuditDialog: false,
      // 发起请求状态
      isAudit: true,
      // 遮罩层
      loading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 角色表格数据
      serviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      info: {
        dId: 1,
        fId: ''
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        carPackage: undefined,
        serviceCatalog: undefined,
        auditStatus: undefined,
        saleStatus: undefined
      },
      // 审核表单
      auditForm: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "服务名称不能为空", trigger: "blur" }
        ],
        originalPrice: [
          { required: true, message: "服务原价不能为空", trigger: "blur" }
        ],
        discountPrice: [
          { required: true, message: "服务折扣价不能为空", trigger: "blur" }
        ],
        carPackage: [
          { required: true, message: "是否套餐不能为空", trigger: "blur" }
        ],
        serviceCatalog: [
          { required: true, message: "服务分类不能为空", trigger: "blur" }
        ],
        info: [
          { required: true, message: "备注信息不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  computed: {
    //未上架商品的编辑状态
    editStatus() {
      return function (row) {
        let saleStatus = row.saleStatus;
        let auditStatus = row.auditStatus;
        if (saleStatus == 1 || auditStatus == 1) {
          return true
        }
        return false;
      }
    },
    //未上架商品的上架按钮的状态
    handleSaleStatus() {
      return function (row) {
        let auditStatus = row.auditStatus;
        if (auditStatus == 0 || auditStatus == 1 || auditStatus == 3) return true;
        return false;
      }
    }
  },
  methods: {
    /** 查询养修服务单项列表 */
    getList() {
      this.loading = true;
      listServiceItem(this.queryParams).then(response => {
        this.serviceList = response.rows;
        this.total = response.total;
        this.loading = false;
      }
      );
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.startAuditDialog = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        originalPrice: undefined,
        discountPrice: undefined,
        carPackage: 1,
        serviceCatalog: undefined,
        info: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        name: undefined,
        carPackage: undefined,
        serviceCatalog: undefined,
        auditStatus: undefined,
        saleStatus: undefined
      }
      this.handleQuery();
    },
    handleAudit() {
      this.startAuditDialog = true
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加养修服务单项";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getServiceItem({ id }).then(response => {
        delete response.data.createTime;
        this.form = response.data;
        this.form.serviceCatalog = this.form.serviceCatalog.toString();
        this.open = true;
        this.title = "修改养修服务单项";
      });
    },
    // 复选框按钮事件处理
    handleSelectionChange(selection) {
      // selection == 当前被选中的 row 对象
      // 长度 == 1 且 是套餐 且 (审核状态为初始化或重新跳转) 且 当前是下架状态
      if (selection.length == 1 && selection[0].carPackage == 1 && (selection[0].auditStatus == 0 || selection[0].auditStatus == 3) && selection[0].saleStatus == 0) {
        this.auditForm = {...selection[0]}
        this.auditForm.info = '' // 将原来的备注清空
      } else {
        this.auditForm = {}
      }
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateServiceItem(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addServiceItem(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    submitAudit() {
      // 获取到当前需要审核的服务项的 id, 以及备注信息, 发起请求提交到后台
      startAudit(this.auditForm.id, this.auditForm.info).then(res => {
        // 提示操作成功
        this.$modal.msgSuccess("操作成功");
        // 刷新表格
        this.getList()
        // 关闭弹框
        this.startAuditDialog = false
      })
    },
    // 上架按钮操作
    handleAdded(row) {
      const id = row.id;
      this.$modal.confirm('是否需要上架该商品?').then(function () {
        return serviceItemSaleOn({ id });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => { });
    },
    handleUnshelve(row) {
      const id = row.id;
      this.$modal.confirm('是否需要下架该商品?').then(function () {
        return serviceItemSaleOff({ id });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => { });
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('system/role/export', {
        ...this.queryParams
      }, `role_${new Date().getTime()}.xlsx`)
    }
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
