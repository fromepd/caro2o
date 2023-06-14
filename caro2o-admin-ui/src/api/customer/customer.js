import request from '@/utils/request'

// 查询客户档案列表
export function listCustomer(query) {
  return request({
    url: '/customer',
    method: 'get',
    params: query
  })
}

// 查询客户档案详细
export function getCustomer(id) {
  return request({
    url: '/customer/' + id,
    method: 'get'
  })
}

// 新增客户档案
export function addCustomer(data) {
  return request({
    url: '/customer',
    method: 'post',
    data: data
  })
}

// 修改客户档案
export function updateCustomer(data) {
  return request({
    url: '/customer',
    method: 'put',
    data: data
  })
}
