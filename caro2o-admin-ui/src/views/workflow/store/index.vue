<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="门店名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入门店名称"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in dict.type.bus_store_info"
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
            v-hasPermi="['workflow:store:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['workflow:store:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['workflow:store:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['workflow:store:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="storeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="门店名称" align="center" prop="name"/>
      <el-table-column label="门店介绍" align="center" prop="intro"/>
      <el-table-column label="经营范围" align="center" prop="businessScope"/>
      <el-table-column label="联系方式" align="center" prop="contact"/>
      <el-table-column label="门店地址" align="center" prop="address"/>
      <el-table-column label="开店时间" align="center" prop="openingTime" width="180">
<!--        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.openingTime) }}</span>
        </template>-->
      </el-table-column>
      <el-table-column label="闭店时间" align="center" prop="closingTime" width="180">
<!--        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.closingTime, '{H}-{m}-{s}') }}</span>
        </template>-->
      </el-table-column>
      <el-table-column label="管理员id" align="center" prop="managerId" />
      <el-table-column label="管理员名称" align="center" prop="managerName" />
      <el-table-column label="管理员手机号" align="center" prop="managerTel" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_store_info" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['workflow:store:edit']"
          >修改
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['workflow:store:remove']"
          >删除
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

    <!-- 添加或修改门店信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="门店名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入门店名称"/>
        </el-form-item>
        <el-form-item label="门店介绍" prop="intro">
          <el-input v-model="form.intro" placeholder="请输入门店介绍"/>
        </el-form-item>
        <el-form-item label="经营范围" prop="businessScope">
          <el-input v-model="form.businessScope" placeholder="请输入经营范围"/>
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入联系方式"/>
        </el-form-item>
        <el-form-item label="门店地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入门店地址"/>
        </el-form-item>
        <el-form-item label="开店时间" prop="openingTime">
          <el-time-picker
              v-model="form.openingTime"
              value-format="HH:mm:ss"
              placeholder="请选择开店时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="管理员" prop="managerId">
          <el-select v-model="form.managerId" placeholder="请选择管理员">
            <el-option
                v-for="u in userList"
                :key="u.userId"
                :label="u.nickName"
                :value="u.userId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="闭店时间" prop="closingTime">
          <el-time-picker
              v-model="form.closingTime"
              value-format="HH:mm:ss"
              placeholder="请选择闭店时间">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in dict.type.bus_store_info"
                :key="dict.value"
                :label="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
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
import {listStore, getStore, delStore, addStore, updateStore} from "@/api/workflow/store";
import {listAllUser} from "@/api/system/user";

export default {
  name: "Store",
  dicts: ['bus_store_info'],
  data() {
    return {
      userList:[],
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
      // 门店信息表格数据
      storeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        managerId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "门店名称不能为空", trigger: "blur"}
        ],
        contact: [
          {required: true, message: "联系方式不能为空", trigger: "blur"}
        ],
        address: [
          {required: true, message: "门店地址不能为空", trigger: "blur"}
        ],
        openingTime: [
          {required: true, message: "开店时间不能为空", trigger: "blur"}
        ],
        closingTime: [
          {required: true, message: "闭店时间不能为空", trigger: "blur"}
        ],
        businessScope: [
          {required: true, message: "经营范围不能为空", trigger: "blur"}
        ],

      }
    };
  },
  created() {
    this.getList();
    listAllUser().then(res => {
      this.userList = res.data
    })
  },
  methods: {
    /** 查询门店信息列表 */
    getList() {
      this.loading = true;
      listStore(this.queryParams).then(response => {
        this.storeList = response.rows;
        this.total = response.total;
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
        name: null,
        intro: null,
        businessScope: null,
        contact: null,
        address: null,
        openingTime: null,
        closingTime: null,
        managerId: null,
        managerName: null,
        managerTel: null,
        status: 0,
        creatorId: null,
        createdTime: null,
        updatorId: null,
        updatedTime: null
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加门店信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStore(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改门店信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStore(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStore(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除门店信息编号为"' + ids + '"的数据项？').then(function () {
        return delStore(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('workflow/store/export', {
        ...this.queryParams
      }, `store_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
