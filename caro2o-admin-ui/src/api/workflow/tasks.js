import request from '@/utils/request'

// 查询营销任务列表
export function listTasks(query) {
  return request({
    url: '/workflow/tasks/list',
    method: 'get',
    params: query
  })
}

// 查询营销任务详细
export function getTasks(id) {
  return request({
    url: '/workflow/tasks/' + id,
    method: 'get'
  })
}

// 新增营销任务
export function addTasks(data) {
  return request({
    url: '/workflow/tasks',
    method: 'post',
    data: data
  })
}

// 修改营销任务
export function updateTasks(data) {
  return request({
    url: '/workflow/tasks',
    method: 'put',
    data: data
  })
}

// 删除营销任务
export function delTasks(id) {
  return request({
    url: '/workflow/tasks/' + id,
    method: 'delete'
  })
}
