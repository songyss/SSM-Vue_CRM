# CRM客户关系管理系统

一个基于SSM + Vue.js的现代化客户关系管理系统，帮助企业高效管理客户信息、销售流程和营销活动。

## 项目简介

本CRM系统是一个功能完整的客户关系管理平台，采用前后端分离架构，支持多角色用户权限管理，包含客户管理、商机管理、订单管理、营销管理、数据统计分析等核心功能模块。

### 主要功能

- **用户认证与权限管理**：基于JWT的用户认证，RBAC角色权限控制
- **客户信息管理**：客户信息收集、审核、分配、跟进
- **营销活动管理**：活动策划、执行、效果分析、二维码推广
- **销售管理**：商机管理、订单处理、售后服务、发票管理
- **电话销售管理**：任务分配、通话记录、客户回访
- **数据统计与分析**：仪表盘、销售报表、客户分析、营销分析
- **系统管理**：员工管理、部门管理、角色管理、操作日志
- **紧急事务管理**：紧急事件上报、应急处理

## 技术栈

### 后端技术
- **语言**：Java 17
- **框架**：Spring 6.1, Spring MVC, Spring AOP, Spring Transaction
- **数据访问**：MyBatis 3.5, Druid连接池
- **数据库**：MySQL 8.0
- **安全**：JWT Token, Argon2密码加密
- **其他**：Lombok, PageHelper分页, Hutool工具类

### 前端技术
- **框架**：Vue.js 3.5, Vue Router 4.5, Pinia 3.0
- **UI组件**：Element Plus 2.11
- **图表**：ECharts 6.0
- **HTTP客户端**：Axios 1.12
- **构建工具**：Vite 7.0

## 项目结构

```
├── CRM/                    # 后端Spring项目
│   ├── src/main/java/com/csi/  # Java源码
│   │   ├── controller/     # 控制器层
│   │   ├── service/        # 业务逻辑层
│   │   ├── mapper/         # 数据访问层
│   │   ├── domain/         # 实体类
│   │   ├── config/         # 配置类
│   │   └── util/           # 工具类
│   ├── src/main/resources/ # 配置文件
│   └── pom.xml            # Maven依赖配置
├── vue/                   # 前端Vue项目
│   ├── src/               # Vue源码
│   │   ├── components/    # 组件
│   │   ├── views/         # 页面视图
│   │   ├── router/        # 路由配置
│   │   ├── stores/        # Pinia状态管理
│   │   └── utils/         # 工具函数
│   └── package.json       # npm依赖配置
```

## 安装与部署

### 环境要求

- **后端**：
  - Java 17+
  - Maven 3.6+
  - MySQL 8.0+
  - Redis（可选）

- **前端**：
  - Node.js 20.19+ 或 >=22.12.0
  - npm 或 yarn

### 后端部署

1. **数据库配置**
   ```bash
   # 创建数据库
   CREATE DATABASE crm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 导入数据库结构（具体SQL文件请参考项目文档）
   ```

2. **修改数据库配置**
   ```bash
   # 修改 CRM/src/main/resources/jdbc.properties
   jdbc.driver=com.mysql.cj.jdbc.Driver
   jdbc.url=jdbc:mysql://localhost:3306/crm?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
   jdbc.username=your_username
   jdbc.password=your_password
   ```

3. **编译和运行**
   ```bash
   cd CRM
   mvn clean compile
   mvn spring-boot:run
   ```

### 前端部署

1. **安装依赖**
   ```bash
   cd vue
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run dev
   ```

3. **构建生产版本**
   ```bash
   npm run build
   ```

## 使用说明

系统支持多种用户角色，包括：
- 系统管理员：负责系统配置和用户管理
- 市场部经理：负责营销活动和客户审核
- 电话销售经理：负责客户分配和任务管理
- 销售部经理：负责商机和订单审批
- 普通员工：执行具体业务操作

### 主要业务流程

1. **客户信息收集**：市场部员工录入潜在客户信息
2. **客户审核**：市场部经理审核客户信息
3. **客户分配**：客户进入公共池，由经理分配给相应员工
4. **销售跟进**：销售员工跟进商机，处理订单
5. **数据分析**：通过仪表盘查看业务数据

## API文档

系统提供RESTful API接口，支持：
- 用户认证（JWT Token）
- 客户信息管理
- 商机订单管理
- 营销活动管理
- 数据统计分析

详细API接口文档请参考后端Controller层注释。

## 贡献指南

欢迎提交Issue和Pull Request来帮助改进这个项目！

### 开发规范

- 代码遵循阿里巴巴Java开发手册
- 前端代码遵循Vue官方风格指南
- 提交信息使用约定式提交规范
- 功能开发遵循分支开发流程

### 本地开发

1. Fork本项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

## 作者与致谢

- 感谢所有为项目贡献代码和提出建议的开发者

## 项目状态

本项目已完成核心功能开发，处于维护阶段。欢迎社区贡献者参与功能扩展和问题修复。

---

## 支持

如需帮助，请通过以下方式联系我们：
- 提交GitHub Issue
- 查看项目文档
