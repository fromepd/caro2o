import request from '@/utils/request'

// 查询客户关怀列表
export function listVisit(query) {
  return request({
    url: '/customer/visit',
    method: 'get',
    params: query
  })
}

// 查询客户关怀详细
export function getVisit(id) {
  return request({
    url: '/customer/visit/' + id,
    method: 'get'
  })
}

// 新增客户关怀
export function addVisit(data) {
  return request({
    url: '/customer/visit',
    method: 'post',
    data: data
  })
}

export function listAllCustomer(){
  return request({
    url: '/customer/all',
    method: 'get',
  })
}
