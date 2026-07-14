import request from '@/utils/request'

export const pageClazzs = (params) => request.get('/clazzs', { params })
export const listClazzs = () => request.get('/clazzs/list')
export const getClazz = (id) => request.get(`/clazzs/${id}`)
export const addClazz = (data) => request.post('/clazzs', data)
export const updateClazz = (data) => request.put('/clazzs', data)
export const deleteClazz = (id) => request.delete(`/clazzs/${id}`)
