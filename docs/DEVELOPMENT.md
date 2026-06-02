# 开发流程约定

## 分支策略

本项目采用简单分支模型：

- `main`：稳定主分支，保证能构建、能运行
- `feature/*`：功能开发分支
- `fix/*`：问题修复分支
- `docs/*`：文档调整分支

个人练习阶段可以直接在 `main` 上小步提交；进入模块开发后，建议使用功能分支。

## 提交信息格式

提交信息使用：

```text
<type>: <summary>
常用 type：

type	用途
chore	工程配置、依赖、脚手架
docs	文档
feat	新功能
fix	修复问题
test	测试
refactor	重构
style	格式调整，不改变逻辑
示例：

text


chore: initialize spring boot project
docs: add development workflow
feat: add global response wrapper
test: add user service tests
fix: reject disabled user login
小步提交原则
每次提交只做一类事情。

好：

text


chore: add maven project skeleton
feat: add result response wrapper
test: add result factory method tests
不好：

text


feat: add user role login redis docker all
阶段完成检查
每个阶段结束前至少执行：

bash

git status
有 Maven 项目后，还要执行：

bash

mvn test
推送习惯
本地提交后及时推送：

bash

git push
第一次推送新分支：

bash

git push -u origin feature/branch-name
敏感信息规则
不要提交：

数据库真实密码
GitHub token
服务器私钥
.env
application-local.yml
application-secret.yml
本地配置只放在不提交的文件里。