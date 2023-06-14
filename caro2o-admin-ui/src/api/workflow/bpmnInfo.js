import request from '@/utils/request'

// 查询流程定义列表
export function listBpmnInfo(query) {
  return request({
    url: '/workflow/bpmnInfo/list',
    method: 'get',
    params: query
  })
}

// 删除流程定义
export function delBpmnInfo(id) {
  return request({
    url: '/workflow/bpmnInfo/' + id,
    method: 'delete'
  })
}

// 获取流程图/流程文件资源
export function getResource(id, type) {
  return request({
    url: '/workflow/bpmnInfo/resources/' + id,
    method: 'get',
    params: {type}
  })
}

// 流程部署
export function deploy(formData) {
  return request({
    url: '/workflow/bpmnInfo/deploy',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
