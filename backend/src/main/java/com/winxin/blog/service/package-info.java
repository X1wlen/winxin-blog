/**
 * 业务逻辑层（预留占位，不实现）。
 *
 * <p>分层职责与边界：
 * <ul>
 *   <li>接口命名：XxxService（接口）+ impl/XxxServiceImpl（实现），或直接用类（个人项目可选简洁风格，需全文统一）</li>
 *   <li>只写业务编排：参数业务校验、领域规则、调用 Mapper 组装数据；不写 SQL、不碰 HTTP 语义</li>
 *   <li>边界约定：事务边界、缓存策略由你自行实现；Security/JWT、AOP 切面亦不在本层</li>
 *   <li>建议按模块建子包：auth / article / user / comment / ai（接口清单见 docs/03-接口文档.md）</li>
 * </ul>
 */
package com.winxin.blog.service;
