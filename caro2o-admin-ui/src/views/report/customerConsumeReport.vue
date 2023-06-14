<template><!--客户消费统计-->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm"  :inline="true" v-show="showSearch">
      <el-form-item label="到店时间">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm" type="daterange"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item label="客户名称">
        <el-input v-model="queryParams.customerName"></el-input>
      </el-form-item>
      <el-form-item label="客户电话">
        <el-input v-model="queryParams.customerPhone"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="reportList">
      <el-table-column label="电话号码" width="180px" prop="customerPhone"/>
      <el-table-column label="消费金额" prop="statisticalAmount"/>
    </el-table>
    <!--报表分页-->
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
import {customerConsumeReport} from "@/api/report/report";

export default {
  name: "report",
  dicts: ['si_service_catalog', 'statement_status', 'appointment_status'],
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
        startTime:undefined,
        endTime:undefined,
        customerPhone:undefined,
        customerName:undefined,
        pageNum: 1,
        pageSize: 10,
      },
      reportList:[],


    };
  },
  created() {
    this.getList();
  },
  methods: {
    /**查询项目服务结算列表 */
    getList() {
      this.loading = true;
      customerConsumeReport(this.queryParams).then(response => {
        this.reportList = response.data.list;
        //分页
        this.total = response.data.total;
        this.queryParams.pageNum = response.data.pageNum;
        this.queryParams.pageSize = response.data.pageSize;
        this.loading = false;
      }
      );
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
        remark: undefined
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

  }
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
