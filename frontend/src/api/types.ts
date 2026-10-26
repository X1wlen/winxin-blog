/**
 * 与后端 common/result 包对应的数据结构。
 * 两端字段保持一致是前后端并行的契约基础，改动需同步 docs/03-接口文档.md。
 */

/** 统一响应结构 Result<T> */
export interface Result<T = unknown> {
  /** 业务状态码：0 成功；分段规则见后端 ResultCode 枚举 */
  code: number
  /** 提示信息（可直接展示给用户） */
  message: string
  /** 业务数据，失败时为 null */
  data: T
}

/** 统一分页结构 PageResult<T> */
export interface PageResult<T = unknown> {
  list: T[]
  total: number
  page: number
  pageSize: number
}
