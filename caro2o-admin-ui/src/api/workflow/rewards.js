import request from '@/utils/request'

// 查询营销奖励 列表
export function listRewards(query) {
  return request({
    url: '/workflow/rewards/list',
    method: 'get',
    params: query
  })
}

// 查询营销奖励 详细
export function getRewards(id) {
  return request({
    url: '/workflow/rewards/' + id,
    method: 'get'
  })
}

// 新增营销奖励 
export function addRewards(data) {
  return request({
    url: '/workflow/rewards',
    method: 'post',
    data: data
  })
}

// 修改营销奖励 
export function updateRewards(data) {
  return request({
    url: '/workflow/rewards',
    method: 'put',
    data: data
  })
}

// 删除营销奖励 
export function delRewards(id) {
  return request({
    url: '/workflow/rewards/' + id,
    method: 'delete'
  })
}
