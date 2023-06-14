import request from '@/utils/request'
import qs  from 'qs'

// qs == query string
// 将 json 参数序列化为 {aa:aa, bb:cc} => aa=aa&bb=cc

// 查询套餐审核列表
export function listAudit(query) {
  return request({
    url: '/workflow/todo/list',
    method: 'get',
    params: query
  })
}

// 查看流程进度
export function getProcessingImage(instanceId) {
  return request({
    url: '/workflow/carPackageAudit/processing/image',
    params: {instanceId},
    method: 'get'
  })
}
