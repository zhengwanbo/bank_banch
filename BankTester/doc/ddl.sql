drop database if exists bank;
create database bank character set utf8;

create table bank.customer 
(	
    cuno varchar(15) primary key not null comment '客户号',
	cuname varchar(200) not null          comment '户名',
	paptype char(2) not null              comment '证件类型',
	papno varchar(18) not null            comment '证件号码',
	phoneno varchar(11) not null          comment '手机号',
	addr varchar(500)                     comment '地址',
	custate char(2) default '01'          comment '客户状态',
	modifytime timestamp(6)               comment '修改时间',
	reserve1 varchar(500)                 comment '备用字段1',
	reserve2 varchar(500)                 comment '备用字段2',
	reserve3 varchar(500)                 comment '备用字段3',
	reserve4 varchar(500)                 comment '备用字段4'
);	
create index customer_idx1 on bank.customer (cuname) ;

--  create table bank.account 
--  (	
--      accno varchar(16) primary key not null comment '账号',
--  	accstate char(1) default '1' not null  comment '账户状态',
--  	realtimeremain decimal(18,2) default 0 not null  comment '实时余额',
--  	currency char(3) not null              comment '币种',
--  	rate decimal(13,5) not null  default 1 comment '利率',
--  	accnature char(1) default '1' not null comment '账号性质',
--  	cuno varchar(15) not null              comment '客户号',
--  	reserve1 varchar(500)                  comment '备用字段1',
--  	reserve2 varchar(500)                  comment '备用字段2',
--  	reserve3 varchar(500)                  comment '备用字段3',
--  	reserve4 varchar(500)                  comment '备用字段4'
--  )distributed by hash(cuno)(g1,g2,g3,g4);	
--  create index account_idx1 on bank.account (cuno) ;

create table bank.account 
(	
    accno varchar(16) primary key not null comment '账号',
	accstate char(1) default '1' not null  comment '账户状态',
	realtimeremain decimal(18,2) default 0 not null  comment '实时余额',
	currency char(3) not null              comment '币种',
	rate decimal(13,5) not null  default 1 comment '利率',
	accnature char(1) default '1' not null comment '账号性质',
	cuno varchar(15) not null              comment '客户号',
	reserve1 varchar(500)                  comment '备用字段1',
	reserve2 varchar(500)                  comment '备用字段2',
	reserve3 varchar(500)                  comment '备用字段3',
	reserve4 varchar(500)                  comment '备用字段4'
);

create index account_idx1 on bank.account (cuno) ;

create table bank.bill 
(	
    flowno varchar(19) not null             comment '流水号',
	flowdate date not null                  comment '日期',
	accno varchar(16) not null              comment '账号',
	debitamount decimal(18,2) not null      comment '借方发生额',
	credityield decimal(18,2) not null      comment '贷方发生额',
	cuno varchar(15) not null               comment '客户号',
	abscode char(2)                         comment '摘要码',
	note varchar(200)                       comment '附言',
	reserve1 varchar(500)                   comment '备用字段1',
	reserve2 varchar(500)                   comment '备用字段2',
	reserve3 varchar(500)                   comment '备用字段3',
	reserve4 varchar(500)                   comment '备用字段4',
	constraint pk_bill primary key (accno, flowno)
);	

create table bank.journal 
(	
    flowno varchar(19)      not null      comment '流水号',
	flowdate date not null                comment '日期',
	amount decimal(18,2) not null         comment '发生额',
	debitacc varchar(16) not null         comment '借方账号',
	creditacc varchar(16) not null        comment '贷方账号',
	errcode char(4)             comment '错误码',
	state char(1)                         comment '状态',
	reserve1 varchar(500)                 comment '备用字段1',
	reserve2 varchar(500)                 comment '备用字段2',
	reserve3 varchar(500)                 comment '备用字段3',
	reserve4 varchar(500)                 comment '备用字段4',
	constraint pk_journal primary key (flowno, debitacc,creditacc)
);	
