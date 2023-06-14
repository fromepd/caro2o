import request from '@/utils/request'

// 查询门店信息列表
export function listStore(query) {
  return request({
    url: '/workflow/store/list',
    method: 'get',
    params: query
  })
}

// 查询门店信息详细
export function getStore(id) {
  return request({
    url: '/workflow/store/' + id,
    method: 'get'
  })
}

// 新增门店信息
export function addStore(data) {
  return request({
    url: '/workflow/store',
    method: 'post',
    data: data
  })
}

// 修改门店信息
export function updateStore(data) {
  return request({
    url: '/workflow/store',
    method: 'put',
    data: data
  })
}

// 删除门店信息
export function delStore(id) {
  return request({
    url: '/workflow/store/' + id,
    method: 'delete'
  })
}
