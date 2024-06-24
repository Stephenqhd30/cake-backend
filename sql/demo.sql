-- 创建数据库
create database cake;

-- 使用数据库
use cake;

-- 用户表
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userPhone    varchar(256)                           null comment '手机号码',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    userEmail    varchar(256)                           null comment '用户邮箱',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
)
    comment '用户' collate = utf8mb4_unicode_ci;

-- 商品表
create table goods
(
    id          bigint auto_increment comment '商品id'
        primary key,
    goodsName   varchar(256)                       null comment '商品名称',
    goodsCover  varchar(512)                       null comment '商品封面图',
    goodsImage1 varchar(512)                       null comment '商品详细图1',
    goodsImage2 varchar(512)                       null comment '商品详细图2',
    price       float                              null comment '商品价格',
    content     text                               null comment '商品介绍',
    stock       int                                null comment '商品库存',
    typeId      int                                null comment '商品类型',
    userId      bigint                             not null comment '创建人id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '商品表';

-- 订单表
create table `order`
(
    id         bigint auto_increment comment '订单id'
        primary key,
    total      float                              null comment '总金额',
    amount     int                                null comment '商品数量',
    status     tinyint                            null comment '支付状态',
    payType    tinyint                            null comment '支付方式',
    userName   varchar(512)                       null comment '用户姓名',
    userPhone  varchar(512)                       null comment '用户电话',
    address    varchar(512)                       null comment '用户地址',
    dateTime   datetime                           null comment '订单日期',
    userId     int                                null comment '用户id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '订单表';

-- 订单项表
create table order_item
(
    id          bigint auto_increment comment '订单项id'
        primary key,
    goodsPrice  float                              null comment '商品价格',
    goodsAmount int                                null comment '商品数量',
    goodsId     int                                null comment '商品id',
    orderId     int                                null comment '订单id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '订单项表';

-- 类别表
create table type
(
    id         bigint auto_increment comment '商品id'
        primary key,
    typeName   varchar(256)                       null comment '商品名称',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '类别表';

-- 推荐表
create table recommend
(
    id         int auto_increment comment '推荐栏id'
        primary key,
    goodsType  varchar(256)                       null comment '商品类别',
    goodsId    int                                null comment '商品id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '推荐表';

