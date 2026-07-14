import request from '@/utils/request'

export const listDepts = () => request.get('/depts')
export const getDept = (id) => request.get(`/depts/${id}`)
export const addDept = (data) => request.post('/depts', data)
export const updateDept = (data) => request.put('/depts', data)
export const deleteDept = (id) => request.delete('/depts', { params: { id } })
