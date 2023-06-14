import request from '@/utils/request'

// 查询营销活动列表
export function listMarketing(query) {
  return request({
    url: '/workflow/marketing/list',
    method: 'get',
    params: query
  })
}

// 查询营销活动详细
export function getMarketing(id) {
  return request({
    url: '/workflow/marketing/' + id,
    method: 'get'
  })
}

// 新增营销活动
export function addMarketing(data) {
  return request({
    url: '/workflow/marketing',
    method: 'post',
    data: data
  })
}

// 修改营销活动
export function updateMarketing(data) {
  return request({
    url: '/workflow/marketing',
    method: 'put',
    data: data
  })
}

// 删除营销活动
export function delMarketing(id) {
  return request({
    url: '/workflow/marketing/' + id,
    method: 'delete'
  })
}
