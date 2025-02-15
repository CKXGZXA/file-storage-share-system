
# FileStorageShareSystem

一个基于区块链和IPFS的文件存储与分享原型系统。

## 项目概述
本项目旨在构建一个安全可靠的文件存储与分享系统，利用区块链的不可篡改特性和IPFS的分布式存储优势，确保文件的安全性和可用性。

## 技术栈
- **Spring Boot**：版本 `2.7.9`，用于构建独立的、生产级的Spring应用程序。
- **Java**：版本 `1.8`，作为主要的开发语言。
- **MySQL**：用于数据存储。
- **MyBatis**：版本 `2.3.0`，作为持久层框架。
- **ChainMaker SDK Java**：版本 `2.3.1`，用于与区块链交互。
- **IPFS Java客户端**：版本 `v1.3.3`，用于与IPFS网络交互。

## 项目结构
项目主要包含以下模块：
- **控制器层**：处理用户请求，如用户登录、注销、文件下载等。
- **服务层**：实现业务逻辑，如用户认证、文件管理等。
- **SDK层**：与区块链和IPFS进行交互，包括证书管理、文件存储等。

## 如何运行
略

## 主要依赖
以下是项目的主要依赖：
- `spring-boot-starter-web`：用于构建Web应用程序。
- `mysql-connector-j`：MySQL数据库驱动。
- `pagehelper`：分页插件。
- `lombok`：简化Java代码。
- `chainmaker-sdk-java`：ChainMaker区块链SDK。
- `java-ipfs-http-client`：IPFS Java客户端。

## 测试
项目使用JUnit进行单元测试，你可以运行以下命令来执行所有测试：
```bash
mvn test
```

## 贡献
欢迎任何形式的贡献，包括但不限于代码贡献、文档完善、问题反馈等。如果你有任何想法或建议，请随时提交Issue或Pull Request。

## 许可证
本项目采用 [MIT许可证](https://opensource.org/licenses/MIT) 进行开源。