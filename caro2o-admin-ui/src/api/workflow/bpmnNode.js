import request from '@/utils/request'

// 查询流程定义节点列表
export function listBpmnNode(query) {
  return request({
    url: '/workflow/bpmnNode/list',
    method: 'get',
    params: query
  })
}

// 查询流程定义节点详细
export function getBpmnNode(id) {
  return request({
    url: '/workflow/bpmnNode/' + id,
    method: 'get'
  })
}

// 新增流程定义节点
export function addBpmnNode(data) {
  return request({
    url: '/workflow/bpmnNode',
    method: 'post',
    data: data
  })
}

// 修改流程定义节点
export function updateBpmnNode(data) {
  return request({
    url: '/workflow/bpmnNode',
    method: 'put',
    data: data
  })
}

// 删除流程定义节点
export function delBpmnNode(id) {
  return request({
    url: '/workflow/bpmnNode/' + id,
    method: 'delete'
  })
}
