<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="活动名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入活动名称"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="creattime">
        <el-date-picker clearable
                        v-model="queryParams.creattime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="终止时间" prop="stopTime">
        <el-date-picker clearable
                        v-model="queryParams.stopTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择终止时间">
        </el-date-picker>
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
            v-hasPermi="['workflow:marketing:add']"
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
            v-hasPermi="['workflow:marketing:edit']"
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
            v-hasPermi="['workflow:marketing:remove']"
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
            v-hasPermi="['workflow:marketing:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="marketingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="活动名称" align="center" prop="name"/>
      <el-table-column label="排序" align="center" prop="sort"/>
      <el-table-column label="创建时间" align="center" prop="creattime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.creattime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="终止时间" align="center" prop="stopTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.stopTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="显示位置" align="center" prop="displayposition"/>
      <el-table-column label="BANNER图" align="center" prop="bannerImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.bannerImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="H5链接" align="center" prop="h5Url"/>
      <el-table-column label="活动规则" align="center" prop="rule"/>
      <el-table-column label="状态" align="center" prop="state">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_job_status" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['workflow:marketing:edit']"
          >修改
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['workflow:marketing:remove']"
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

    <!-- 添加或修改营销活动对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入活动名称"/>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序"/>
        </el-form-item>
          <el-form-item label="活动时间">
            <el-col>
              <el-date-picker
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="选择日期"
                  v-model="form.creattime"
                  :picker-options="pickerOptions"
                  style="width: 45%;"
                  @change="changeData(true)"
              ></el-date-picker>
            </el-col>
           <span>至</span>
            <el-col>
              <el-date-picker
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="选择日期"
                  v-model="form.stopTime"
                  :picker-options="pickerOptions"
                  style="width: 45%;"
                  @change="changeData(false)"
              ></el-date-picker>
            </el-col>
          </el-form-item>
        <!--<el-form-item label="开始时间">
        <el-date-picker clearable
                        v-model="form.creattime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择创建时间"
                        style="width: 45%;"
                        :picker-options="creattime"
        />
        <span>至</span>
        <el-date-picker clearable
            v-model="form.stopTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择终止时间"
            :picker-options="stopTime"
            style="width: 45%;"
        />
        </el-form-item>-->
        <el-form-item label="显示位置" prop="displayposition">
          <el-input v-model="form.displayposition" placeholder="请输入显示位置"/>
        </el-form-item>
        <el-form-item label="BANNER图">
          <image-upload v-model="form.bannerImage"/>
        </el-form-item>
        <el-form-item label="H5链接" prop="h5Url">
          <el-input v-model="form.h5Url" placeholder="请输入H5链接"/>
        </el-form-item>
        <el-form-item label="活动规则" prop="rule">
          <el-input v-model="form.rule" placeholder="请输入活动规则"/>
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-select v-model="form.state" placeholder="请选择状态" clearable>
            <el-option
                v-for="dict in dict.type.sys_job_status"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"/>
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
import {listMarketing, getMarketing, delMarketing, addMarketing, updateMarketing} from "@/api/workflow/marketing";

export default {
  name: "Marketing",
  dicts: ['sys_job_status'],
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
      // 营销活动表格数据
      marketingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        creattime: null,
        stopTime: null,
      },
      // 表单参数
      form: {
        state: null,
      },
      pickerOptions: {
        disableDate: time => {
          return time.getTime() > Date.now();
        }
      },
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    changeData(flag) {
      if (flag) {
        if (this.form.stopTime) {
          if (this.form.creattime > this.form.stopTime) {
            this.form.creattime = null;
            this.$message.error("开始时间不能大于结束时间！");
          }
        }
      } else {
        if (this.form.creattime) {
          if (this.form.creattime > this.form.stopTime) {
            this.form.stopTime = null;
            this.$message.error("结束时间不能小于开始时间！");
          }
        }
      }
    },
    /** 查询营销活动列表 */
    getList() {
      this.loading = true;
      listMarketing(this.queryParams).then(response => {
        this.marketingList = response.rows;
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
        sort: null,
        creattime: null,
        stopTime: null,
        displayposition: null,
        bannerImage: null,
        h5Url: null,
        rule: null,
        state: null
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
      this.title = "添加营销活动";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMarketing(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改营销活动";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMarketing(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMarketing(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除营销活动编号为"' + ids + '"的数据项？').then(function () {
        return delMarketing(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('workflow/marketing/export', {
        ...this.queryParams
      }, `marketing_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
