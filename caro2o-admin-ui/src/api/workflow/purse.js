import request from '@/utils/request'

// 查询钱包管理列表
export function listPurse(query) {
  return request({
    url: '/workflow/purse/list',
    method: 'get',
    params: query
  })
}

export function listUser() {
  return request({
    url: '/workflow/purse/users',
    method: 'get',
  })
}

export function frost(userId) {
  return request({
    url: '/workflow/purse/frost',
    method: 'post',
    params: userId
  })
}

export function consume() {
  return request({
    url: '/workflow/purse/consume',
    method: 'get',
  })
}

export function skype(data) {
  return request({
    url: '/workflow/purse/skype',
    method: 'post',
    data: data
  })
}

// 查询钱包管理详细
export function getPurse(id) {
  return request({
    url: '/workflow/purse/' + id,
    method: 'get'
  })
}

// 新增钱包管理
export function addPurse(data) {
  return request({
    url: '/workflow/purse',
    method: 'post',
    data: data
  })
}

// 修改钱包管理
export function updatePurse(data) {
  return request({
    url: '/workflow/purse',
    method: 'put',
    data: data
  })
}

// 删除钱包管理
export function delPurse(id) {
  return request({
    url: '/workflow/purse/' + id,
    method: 'delete'
  })
}

