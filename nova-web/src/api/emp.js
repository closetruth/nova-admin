import request from '@/utils/request'

export const pageEmps = (params) => request.get('/emps', { params })
export const listEmps = () => request.get('/emps/list')
export const getEmp = (id) => request.get(`/emps/${id}`)
export const addEmp = (data) => request.post('/emps', data)
export const updateEmp = (data) => request.put('/emps', data)
export const deleteEmps = (ids) => request.delete('/emps', { params: { ids } })
export const uploadFile = (file) => {
  const form = new FormData()
  form.append('file', file)
  return request.post('/upload', form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}
