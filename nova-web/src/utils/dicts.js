export const GENDER_OPTIONS = [
  { label: '男', value: 1 },
  { label: '女', value: 2 },
]

export const JOB_OPTIONS = [
  { label: '班主任', value: 1 },
  { label: '讲师', value: 2 },
  { label: '学工主管', value: 3 },
  { label: '教研主管', value: 4 },
  { label: '咨询师', value: 5 },
]

export const SUBJECT_OPTIONS = [
  { label: 'Java', value: 1 },
  { label: '前端', value: 2 },
  { label: '大数据', value: 3 },
  { label: 'Python', value: 4 },
  { label: 'Go', value: 5 },
  { label: '嵌入式', value: 6 },
]

export const DEGREE_OPTIONS = [
  { label: '初中', value: 1 },
  { label: '高中', value: 2 },
  { label: '大专', value: 3 },
  { label: '本科', value: 4 },
  { label: '硕士', value: 5 },
  { label: '博士', value: 6 },
]

export function labelOf(options, value) {
  return options.find((item) => item.value === value)?.label ?? '-'
}
