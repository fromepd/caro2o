import request from '@/utils/request'
import qs  from 'qs'

// qs == query string
// 将 json 参数序列化为 {aa:aa, bb:cc} => aa=aa&bb=cc

// 查询套餐审核列表
export function listAudit(query) {
  return request({
    url: '/workflow/done/list',
    method: 'get',
    params: query
  })
}

// 查询套餐审核列表
export function historyList(query) {
  return request({
    url: '/workflow/done/history',
    method: 'get',
    params: query
  })
}
