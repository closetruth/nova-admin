import request from '@/utils/request'

export const pageStudents = (params) => request.get('/students', { params })
export const getStudent = (id) => request.get(`/students/${id}`)
export const addStudent = (data) => request.post('/students', data)
export const updateStudent = (data) => request.put('/students', data)
export const deleteStudents = (ids) => request.delete(`/students/${ids.join(',')}`)
export const addViolation = (id, score) => request.put(`/students/violation/${id}/${score}`)
