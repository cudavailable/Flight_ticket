# Flight_ticket
This is a web application which help users check flight tickets and book a flight. I am responsible for the database construction and backend code writing of the project.

# 各层之间的关系
**Controller**：负责接收请求并返回响应，主要与 Service 层交互。
**Service**：负责实现业务逻辑，调用 Mapper 或 Repository 来完成数据库操作。
**Mapper** (Repository/DAO)：执行实际的数据库查询、插入、更新、删除操作，并将数据返回给 Service 层。
**Entity**：作为数据库表的映射模型，被 Service 和 Mapper 使用来保存和获取数据。
