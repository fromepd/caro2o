<template>
  <div>
    <el-container>
      <el-main>
        <el-row>
          <el-col :span="userInfo.status ? 24 : 16" class="height-handle">
            <el-row :span="24" class="user_info">
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">客户姓名：</el-col>
                  <el-col :span="12">{{ userInfo.customerName }}</el-col>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">联系方式：</el-col>
                  <el-col :span="12">{{ userInfo.customerPhone }}</el-col>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">车牌号码：</el-col>
                  <el-col :span="12">{{ userInfo.licensePlate }}</el-col>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">汽车类型：</el-col>
                  <el-col :span="12">{{ userInfo.carSeries }}</el-col>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">服务类型：</el-col>
                  <el-col :span="12">{{ userInfo.serviceType ? '维修' : '保养' }}</el-col>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">到店时间：</el-col>
                  <el-col :span="12">{{ userInfo.actualArrivalTime }}</el-col>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">总消费金额：</el-col>
                  <el-col :span="12" v-if="userInfo.status">{{ userInfo.totalAmount }}</el-col>
                  <el-col :span="12" v-else>{{ totalSum }}</el-col>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">实付 价格： </el-col>
                  <el-col :span="12" v-if="userInfo.status">{{ userInfo.totalAmount - userInfo.discountAmount }}
                  </el-col>
                  <el-col :span="12" v-else>{{ actualPriceHandle }}</el-col>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-group">
                  <el-col :span="12">优惠价格：</el-col>
                  <el-col :span="12" v-if="userInfo.status">{{ userInfo.discountAmount }}</el-col>
                  <el-col :span="12" v-else>
                    <el-input size="mini" v-model="discountAmount" :min="0" :max="totalSum"
                      @blur="discountAmountHandle">
                    </el-input>
                  </el-col>
                </div>
              </el-col>
            </el-row>
            <el-row :span="24" class="server_item_box">
              <el-row :gutter="10" class="mb8">
                <el-col :span="6" v-if="!userInfo.status">
                  <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="saveHandle">保存</el-button>
                  <el-button type="warning" plain icon="el-icon-plus" size="mini" @click="payHandle">支付</el-button>
                </el-col>
                <right-toolbar @queryTable="getDetail"></right-toolbar>
              </el-row>
              <el-table :data="consumptionInfoList" style="width: 100%">
                <el-table-column prop="itemName" label="服务名称">
                </el-table-column>
                <el-table-column prop="itemPrice" label="服务价格">
                </el-table-column>
                <el-table-column prop="itemQuantity" label="数量">
                </el-table-column>
                <el-table-column label="操作" align="center" v-if="!userInfo.status">
                  <template slot-scope="scope">
                    <el-button size="mini" icon="el-icon-plus" type="primary" @click="plusHandle(scope.$index)">
                    </el-button>
                    <el-button size="mini" icon="el-icon-minus" type="danger" @click="minusHandle(scope.$index)">
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="page">显示第 1 到第 {{ consumptionInfoList.length }} 条记录，总共 {{ consumptionInfoList.length }} 条记录
              </div>
            </el-row>
          </el-col>
          <el-col :span="8" v-if="!userInfo.status" class="height-handle">
            <el-row>
              <el-col :span="24" class="serach_box">
                <el-form ref="form" :model="queryParams" label-width="100px">
                  <el-form-item label="名称：">
                    <el-input v-model="queryParams.name"></el-input>
                  </el-form-item>
                  <el-form-item label="是否套餐：">
                    <el-select v-model="queryParams.carPackage">
                      <el-option label="是" value="0"></el-option>
                      <el-option label="否" value="1"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="服务分类：">
                    <el-select v-model="queryParams.serviceType">
                      <el-option label="维修" value="0"></el-option>
                      <el-option label="保养" value="1"></el-option>
                      <el-option label="其他" value="2"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="success" icon="el-icon-search" round @click="searchHandle">搜索</el-button>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" class="service_list_box">
                <el-table :data="serviceList" style="width: 100%">
                  <el-table-column prop="name" label="服务名称">
                  </el-table-column>
                  <el-table-column prop="originalPrice" label="服务价格">
                  </el-table-column>
                  <el-table-column label="操作">
                    <template slot-scope="scope">
                      <el-button size="mini" icon="el-icon-plus" type="primary"
                        @click="serviceItemPlusHandle(scope.row)"></el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="page">显示第 1 到第 {{ serviceList.length }} 条记录，总共 {{ serviceList.length }} 条记录</div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { getStatement, payStatement } from "@/api/business/statement"
import { addStatementItem, listStatementItem } from "@/api/business/statementItem"
import { listServiceItem } from "@/api/business/serviceItem"
export default {
  data() {
    return {
      //优惠价格
      discountAmount: 0,
      //订单Id
      id: this.$route.query.id,
      // 用户信息
      userInfo: {},
      // 查询参数
      queryParams: {
        saleStatus: 1,
        pageNum: 1,
        pageSize: 5,
        name: undefined,
        carPackage: undefined,
        serviceCatalog: undefined
      },
      //消费参数
      itemQueryParams: {
        statementId: this.$route.query.id,
        pageNum: 1,
        pageSize: 5,
      },
      //已消费总数
      total: 0,
      // 未消费服务项
      serviceList: [],
      //消费服务项
      consumptionInfoList: [],
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    getDetail() {
      // 用户信息
      getStatement({ id: this.id }).then(response => {
        // console.log(response);
        this.discountAmount = response.data.discountAmount
        this.userInfo = response.data
      })
      // 服务单项
      listServiceItem(this.queryParams).then(response => {
        this.serviceList = response.rows
      })
      //消费列表
      listStatementItem(this.itemQueryParams).then(response => {
        this.total = response.total
        this.consumptionInfoList = response.rows;
      })
    },
    //搜索按钮
    searchHandle() {
      listServiceItem(this.queryParams).then(response => {
        this.serviceList = response.rows
      })
    },
    //服务单项添加按钮
    serviceItemPlusHandle(row) {
      // 标识符 表示默认row需要添加到consumptionInfoList中
      let flag = true;
      if (this.consumptionInfoList.length >= 1) {
        this.consumptionInfoList.map((item, index) => {
          if (item.itemId == row.id) {
            this.consumptionInfoList[index].itemQuantity++;
            flag = false;
            return;
          }
        })
        if (flag) {
          this.consumptionInfoList.push(this.serviceItemPlusDataHandle(row));
        }
      } else {
        this.consumptionInfoList.push(this.serviceItemPlusDataHandle(row));
      }
    },
    //数据处理
    serviceItemPlusDataHandle(data) {
      return {
        itemId: data.id,
        statementId: this.id,
        itemName: data.name,
        itemPrice: data.originalPrice,
        itemQuantity: 1,

      }
    },
    // 服务项目数量增加按钮操作
    plusHandle(index) {
      this.consumptionInfoList[index].itemQuantity++;
    },
    //服务项目数量减按钮操作
    minusHandle(index) {
      if (this.consumptionInfoList[index].itemQuantity > 1) {
        this.consumptionInfoList[index].itemQuantity--;
        return;
      }
      this.consumptionInfoList[index].itemQuantity = 1;
    },
    //保存服务选项按钮
    saveHandle() {
      //将要消费的consumptionInfoList数组中的最后一列，伪造一条虚假数据(优惠价格),该数据携带statementId和itemPrice即可
      //伪造数据(优惠价格)
      let discountInfo = {
        id: undefined,
        statementId: '',
        itemId: '',
        itemName: '',
        itemPrice: '',
        itemQuantity: ''
      };
      discountInfo.itemPrice = this.discountAmount;
      discountInfo.statementId = this.id;
      //定义一个新变量
      let data = [];
      Object.assign(data, this.consumptionInfoList);
      data.push(discountInfo)
      console.log("data", data);
      //发送请求
      addStatementItem(data).then(response => {
        this.$modal.msgSuccess("保存成功");
        this.getDetail()
      })
    },
    //支付按钮操作
    payHandle() {
      payStatement(this.id).then(response => {
        this.$modal.msgSuccess("支付成功");
        this.getDetail()
      })
    },
    //优惠价格
    discountAmountHandle() {
      if (this.discountAmount > this.totalSum || this.discountAmount < 0) {
        this.$alert('输入优惠价格有误', {
          confirmButtonText: '确定',
          callback: action => {
            this.discountAmount = 0;
          }
        });
      }
    }
  },
  computed: {
    totalSum() {
      let total = 0;
      this.consumptionInfoList.map(item => {
        total += item.itemPrice * item.itemQuantity
      })
      return total;
    },
    actualPriceHandle() {
      return this.totalSum - this.discountAmount;
    }
  },

}
</script>

<style lang="scss" scoped>
.user_info {
  background-color: #fff;
  border-radius: 6px;
  padding-top: 5px;
  padding-bottom: 13px;
  box-shadow: 1px 1px 3px rgb(0 0 0 / 20%);
  padding-left: 45px;
  color: #333;
  font-size: 13px;

  .form-group {
    line-height: 25px;
  }
}

.server_item_box {
  background: #fff;
  border-radius: 6px;
  margin-top: 10px;
  box-shadow: 1px 1px 3px rgb(0 0 0 / 20%);
  padding: 15px;


}

.serach_box {
  width: 100%;
  background: #fff;
  border-radius: 6px;
  margin-left: 15px;
  padding-top: 5px;
  box-shadow: 1px 1px 3px rgb(0 0 0 / 20%);

  .el-input {
    max-width: 217px;
  }
}

.service_list_box {
  width: 100%;
  background: #fff;
  border-radius: 6px;
  margin-top: 10px;
  padding: 15px;
  box-shadow: 1px 1px 3px rgb(0 0 0 / 20%);
  margin-left: 15px;


}

.page {
  line-height: 34px;
  font-size: 13px;
  color: #676a6c;
}

.el-form-item {
  margin-bottom: 15px;
}
</style>
