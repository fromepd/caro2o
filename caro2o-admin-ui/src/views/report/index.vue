<template><!--收入统计-->
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="到店时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="queryParams.dateStatus"
          placeholder="请选择查询类型"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="reportList">
      <el-table-column label="统计日期" width="180px" prop="statisticalDate" />
      <el-table-column label="盈利金额" prop="statisticalAmount" />
    </el-table>

      <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
          />


  </div>
</template>

<script>
import { storeincome } from "@/api/report/report";

export default {
  name: "report",
  dicts: ["si_service_catalog", "statement_status", "appointment_status"],
  data() {
    return {
      // 遮罩层
      loading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        dateStatus: 0,
        startTime: undefined,
        endTime: undefined,
        pageNum: 1,
        pageSize: 10,
      },
      reportList: [],
      //按年月周日分组
      options: [
        {
          label: "日盈利额",
          value: 0,
        },
        {
          label: "周盈利额",
          value: 1,
        },
        {
          label: "月盈利额",
          value: 2,
        },
        {
          label: "年盈利额",
          value: 3,
        },
      ],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /**查询项目服务结算列表 */
    getList() {
      this.loading = true;
      storeincome(this.queryParams).then((response) => {
        this.reportList = response.data.list;
        //分页功能
        this.total = response.data.total;
        this.queryParams.pageNum = response.data.pageNum;
        this.queryParams.pageSize = response.data.pageSize;
        this.loading = false;
        console.log(response);
      });
    },
    // 表单重置
    reset() {
      this.form = {
        roleId: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: 0,
        status: "0",
        menuIds: [],
        deptIds: [],
        menuCheckStrictly: true,
        deptCheckStrictly: true,
        remark: undefined,
      };
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
  },
};
</script>
<style lang="scss" scoped>
.el-date-editor.el-input {
  width: 100% !important;
}


.el-select {
  width: 80%;
}
</style>
