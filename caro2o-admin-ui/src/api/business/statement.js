import request from '@/utils/request'

//获取服务结算单列表
export function listStatement(params) {
  return request({
    url: '/business/statement',
    method: 'get',
    params
  })
}
//服务结算单添加
export function addStatement(data) {
  return request({
    url: '/business/statement',
    method: 'post',
    data
  })
}

//获取结算单详情
export function getStatement(params) {
  return request({
    url: '/business/statement/' + params.id,
    method: 'get',
  })
}
//结算单详情删除
export function removeStatement(params) {
  return request({
    url: 'business/statement/' + params.id,
    method: 'delete'
  })
}
//结算单更新
export function updateStatement(params) {
  return request({
    url: '/business/statement',
    method: 'put',
    data: params
  })
}
//支付
export function payStatement(id) {
  return request({
    url: '/business/statement/pay/' + id,
    method: 'put'
  })
}
