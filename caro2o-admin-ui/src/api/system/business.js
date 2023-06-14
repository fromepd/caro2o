import request from '@/utils/request'
//获取修养信息预约列表
export function appointmentInfo(data) {
  return request({
    url: '/business/appointment/query',
    method: 'post',
    data
  })
}
// 修养信息预约添加
export function appointmentInfoAdd(data) {
  return request({
    url: '/business/appointment/add',
    method: 'post',
    data
  })
}
//获取修养信息预约详情
export function appointmentInfoGetItem(params) {
  return request({
    url: '/business/appointment/getItem',
    method: 'get',
    params
  })
}
//获取修养信息预约更新
export function appointmentInfoUpdata(data) {
  return request({
    url: '/business/appointment/edit',
    method: 'post',
    data
  })
}
//删除预约信息
export function appointmentInfoDel(params) {
  return request({
    url: '/business/appointment/remove',
    method: 'get',
    params
  })
}
//提交结算单
export function submitClosing(params) {
  return request({
    url: '/business/appointment/generateStatement',
    method: 'get',
    params
  })
}

//取消预约

export function userCancel(params) {
  return request({
    url: '/business/appointment/cancel',
    method: 'get',
    params
  })
}

//用户到店状态修改操作
export function arriveStatusChange(params) {
  return request({
    url: '/business/appointment/arrival',
    method: 'get',
    params
  })
}
//获取修养服务单项列表
export function serviceItem(params) {
  return request({
    url: '/business/serviceItem/query',
    method: 'get',
    params
  })
}
// 添加养修服务单项
export function serviceItemAdd(params) {
  return request({
    url: '/business/serviceItem/add',
    method: 'get',
    params
  })
}
// 编辑更新养修服务单项
export function serviceItemEdit(params) {
  return request({
    url: '/business/serviceItem/edit',
    method: 'get',
    params
  })
}
//获取养修服务单项详情
export function serviceItemGetItem(params) {
  return request({
    url: '/business/serviceItem/getItem',
    method: 'get',
    params
  })
}
//养修服务单项上架
export function serviceItemSaleOn(params) {
  return request({
    url: '/business/serviceItem/saleOn',
    method: 'get',
    params
  })
}
//养修服务单项下架
export function serviceItemSaleOff(params) {
  return request({
    url: '/business/serviceItem/saleOff',
    method: 'get',
    params
  })
}
//发起审核按钮操作 
export function serviceItemAuditPage(params) {
  return request({
    url: '/business/serviceItem/auditPage',
    method: 'get',
    params
  })
}
//审核提交操作 
export function serviceItemStartAudit(params) {
  return request({
    url: '/business/serviceItem/startAudit',
    method: 'get',
    params
  })
}
//获取服务结算单列表
export function statement(params) {
  return request({
    url: '/business/statement/query',
    method: 'get',
    params
  })
}
//服务结算单添加
export function statementAdd(params) {
  return request({
    url: '/business/statement/add',
    method: 'get',
    params
  })
}

//获取结算单详情
export function statementGetItem(params) {
  return request({
    url: '/business/statement/getItem',
    method: 'get',
    params
  })
}
//结算单详情删除 
export function statementRemoveItem(params) {
  return request({
    url: 'business/statement/remove',
    method: 'get',
    params
  })
}
//结算单更新 
export function statementEdit(params) {
  return request({
    url: '/business/statement/edit',
    method: 'get',
    params
  })
} 

// 获取结算单用户信息

export function itemDetail(params) {
  return request({
    url: '/business/statementItem/itemDetail',
    method: 'get',
    params
  })
} 
export function statementItemQuery(params) {
  return request({
    url: '/business/statementItem/query',
    method: 'get',
    params
  })
}
// 保存服务选项
export function statementSaveItems(params) {
  return request({
    url: '/business/statementItem/saveItems',
    method: 'post',
    data:params
  })
}
//支付
export function statementPay(params) {
  return request({
    url: '/business/statementItem/pay',
    method: 'get',
    params
  })
}