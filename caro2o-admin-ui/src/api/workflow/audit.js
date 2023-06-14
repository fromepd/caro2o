import request from '@/utils/request'
import qs  from 'qs'

// qs == query string
// 将 json 参数序列化为 {aa:aa, bb:cc} => aa=aa&bb=cc

// 查询套餐审核列表
export function listAudit(query) {
  return request({
    url: '/workflow/carPackageAudit/list',
    method: 'get',
    params: query
  })
}

// 查询套餐审核详细
export function getAudit(id) {
  return request({
    url: '/workflow/carPackageAudit/' + id,
    method: 'get'
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

// 重新申请
export function reapply(params) {
  let formParams = qs.stringify(params, {indices: false});

  return request({
    url: '/workflow/carPackageAudit/reapply',
    data: formParams,
    headers: {'content-type': 'application/x-www-form-urlencoded'}, // 表单参数
    method: 'post'
  })
}



// 取消申请
export function cancel(id, reason) {
  let params = {id, reason}
  let formParams = qs.stringify(params, {indices: false});

  return request({
    url: '/workflow/carPackageAudit/cancel',
    data: formParams,
    headers: {'content-type': 'application/x-www-form-urlencoded'}, // 表单参数
    method: 'post'
  })
}

// 审批功能
export function doAudit(params) {
  let formParams = qs.stringify(params, {indices: false});

  return request({
    url: '/workflow/carPackageAudit/doAudit',
    data: formParams,
    headers: {'content-type': 'application/x-www-form-urlencoded'}, // 表单参数
    method: 'post'
  })
}

// 开始审核
export function startAudit(id, info) {
  let params = {id, info} // 转换为 id=1&info=xxxx
  console.log(params)
  let formParams = qs.stringify(params, {indices: false});
  console.log(formParams)

  return request({
    url: '/workflow/carPackageAudit/audit',
    data: formParams,
    headers: {'content-type': 'application/x-www-form-urlencoded'}, // 表单参数
    method: 'post'
  })
}

// 新增套餐审核
export function addAudit(data) {
  return request({
    url: '/workflow/carPackageAudit',
    method: 'post',
    data: data
  })
}

// 修改套餐审核
export function updateAudit(data) {
  return request({
    url: '/workflow/carPackageAudit',
    method: 'put',
    data: data
  })
}

// 删除套餐审核
export function delAudit(id) {
  return request({
    url: '/workflow/carPackageAudit/' + id,
    method: 'delete'
  })
}
