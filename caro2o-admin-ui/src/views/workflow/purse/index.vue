<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="现金余额" prop="balance">
        <el-input
            v-model="queryParams.balance"
            placeholder="请输入现金余额"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="rulePurse">
        <el-select v-model="queryParams.rulePurse" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in dict.type.purse"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
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
            v-hasPermi="['workflow:purse:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handlePay"
            v-hasPermi="['workflow:purse:edit']"
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
            v-hasPermi="['workflow:purse:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['workflow:purse:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="purseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户名" align="center" prop="name" />
      <el-table-column label="现金余额" align="center" prop="balance" />
      <el-table-column label="总收入" align="center" prop="revenue" />
      <el-table-column label="总支出" align="center" prop="expenditure" />
      <el-table-column label="状态" align="center" prop="rulePurse">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.purse" :value="scope.row.rulePurse"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              @click="handleSkype(scope.row.userId,1)"
              v-hasPermi="['workflow:purse:skype']"
              v-if="scope.row.rulePurse===0"
          >充值</el-button>
          <el-button
              size="mini"
              type="text"
              @click="handleSkype(scope.row.userId,1)"
              v-if="scope.row.rulePurse===0"
              v-hasPermi="['workflow:purse:consume']"
          >消费</el-button>
          <el-button
              size="mini"
              type="text"
              @click="handleFrost(scope.row)"
              v-if="scope.row.rulePurse===0"
              v-hasPermi="['workflow:purse:frost']"
          >冻结</el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['workflow:purse:remove']"
          >删除</el-button>
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

    <!-- 添加或修改钱包管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="用户" prop="userId">
            <el-select v-model="form.userId" placeholder="请选择">
              <el-option
                  v-for="item in users"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId">
              </el-option>
            </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改钱包管理对话框 -->
    <el-dialog :title="title" :visible.sync="openOne" width="500px" append-to-body>
      <el-form ref="form" :model="formOne" :rules="rules" label-width="80px">
        <el-form-item prop="userId" v-model="formOne.userId" hidden="hidden"/>
        <el-form :model="formOne">
          <el-form-item label="充值金额" >
            <el-input v-model="formOne.money" placeholder="请输入充值金额"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="formOne.remark" placeholder="充值备注"></el-input>
          </el-form-item>
        </el-form>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormOne">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPurse, getPurse, delPurse, addPurse, updatePurse, listUser,frost,skype} from "@/api/workflow/purse";

export default {
  name: "Purse",
  dicts: ['purse'],
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
      // 钱包管理表格数据
      purseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      openOne: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        balance: null,
        rulePurse: null,
      },
      users: [],
      // 表单参数
      form: {},
      formOne: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户名不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询钱包管理列表 */
    getList() {
      this.loading = true;
      listPurse(this.queryParams).then(response => {
        this.purseList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      listUser().then(response => {
        this.users = response.data;
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openOne = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: null,

      };
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加钱包管理";
    },
    /** 修改按钮操作 */
    handlePay(row) {
      this.reset();
      const id = row.id || this.ids
      getPurse(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改钱包管理";
      });
    },
    /** 提交按钮 */
    submitFormOne() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.formOne.id != null) {
            this.formOne.type = 0;
            skype(this.formOne).then(response => {
              this.$modal.msgSuccess("充值成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.formOne.type = 1;
            skype(this.formOne).then(response => {
              this.$modal.msgSuccess("消费成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePurse(this.form).then(response => {
              this.$modal.msgSuccess("消费成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPurse(this.form).then(response => {
              this.$modal.msgSuccess("充值成功");
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
      this.$modal.confirm('是否确认删除钱包管理编号为"' + ids + '"的数据项？').then(function() {
        return delPurse(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 充值按钮操作 */
    handleSkype(id, data) {
      this.formOne.userId=id;
      this.openOne = true;
      if (data === 0) {
        this.title = "充值";
        this.formOne.type = 0;
      }else {
        this.title = "消费";
        this.formOne.type = 1;
      }
    },
    /** 消费按钮 */
    handleConsume() {
      this.open = true;
    },
    /**冻结按钮*/
    handleFrost(row) {
      frost({userId: row.userId}).then(response => {
        this.getList();
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('workflow/purse/export', {
        ...this.queryParams
      }, `purse_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
