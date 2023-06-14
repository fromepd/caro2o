import request from '@/utils/request'

// 查询交易明细列表
export function listDeal(query) {
  return request({
    url: '/workflow/deal/list',
    method: 'get',
    params: query
  })
}

// 查询交易明细详细
export function getDeal(id) {
  return request({
    url: '/workflow/deal/' + id,
    method: 'get'
  })
}

// 新增交易明细
export function addDeal(data) {
  return request({
    url: '/workflow/deal',
    method: 'post',
    data: data
  })
}

// 修改交易明细
export function updateDeal(data) {
  return request({
    url: '/workflow/deal',
    method: 'put',
    data: data
  })
}

// 删除交易明细
export function delDeal(id) {
  return request({
    url: '/workflow/deal/' + id,
    method: 'delete'
  })
}
