import request from '@/utils/request'
export function listStatementItem(params) {
  return request({
    url: '/business/statementItem',
    method: 'get',
    params: params
  })
}
// 保存服务选项
export function addStatementItem(params) {
  return request({
    url: '/business/statementItem',
    method: 'post',
    data: params
  })
}
