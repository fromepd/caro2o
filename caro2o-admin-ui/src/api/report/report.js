import request from '@/utils/request'
//店铺收入
export function storeincome(params) {
  return request({
    url: '/report/storeincome',
    method: 'get',
    headers: {
    
    },
    params
  })
}

//店铺收入
export function shopConsumption(params) {
  return request({
    url: '/report/shopConsumption',
    method: 'get',
    headers: {
    
    },
    params
  })
}


//客户用户统计
export function customerConsumeReport(params) {
  return request({
    url: '/report/customerConsumeReport',
    method: 'get',
    headers: {
    
    },
    params
  })
}
