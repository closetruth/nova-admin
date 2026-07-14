import request from '@/utils/request'

export const pageLogs = (params) => request.get('/log/page', { params })
