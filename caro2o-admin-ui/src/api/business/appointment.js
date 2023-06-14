import request from '@/utils/request'

//获取修养信息预约列表
export function listAppointment(data) {
  return request({
    url: '/business/appointment',
    method: 'get',
    params: data
  })
}
// 修养信息预约添加
export function addAppointment(data) {
  return request({
    url: '/business/appointment',
    method: 'post',
    data: data
  })
}
//获取修养信息预约详情
export function getAppointment(id) {
  return request({
    url: '/business/appointment/' + id,
    method: 'get',
  })
}
//获取修养信息预约更新
export function updateAppointment(data) {
  return request({
    url: '/business/appointment',
    method: 'put',
    data: data
  })
}
//删除预约信息
export function removeAppointment(id) {
  return request({
    url: '/business/appointment/' + id,
    method: 'delete',
  })
}
//提交结算单
export function generateStatement(id) {
  return request({
    url: '/business/appointment/statement/' + id,
    method: 'put',
  })
}

//取消预约
export function cancelAppointment(id) {
  return request({
    url: '/business/appointment/cancel/' + id,
    method: 'put',
  })
}

//用户到店状态修改操作
export function arrival(id) {
  return request({
    url: '/business/appointment/arrival/' + id,
    method: 'put'
  })
}
