drop database if exists bank;
create database bank character set utf8;

create table bank.customer 
(	
    cuno varchar(15) primary key not null comment '�ͻ���',
	cuname varchar(200) not null          comment '����',
	paptype char(2) not null              comment '֤������',
	papno varchar(18) not null            comment '֤������',
	phoneno varchar(11) not null          comment '�ֻ���',
	addr varchar(500)                     comment '��ַ',
	custate char(2) default '01'          comment '�ͻ�״̬',
	modifytime timestamp(6)               comment '�޸�ʱ��',
	reserve1 varchar(500)                 comment '�����ֶ�1',
	reserve2 varchar(500)                 comment '�����ֶ�2',
	reserve3 varchar(500)                 comment '�����ֶ�3',
	reserve4 varchar(500)                 comment '�����ֶ�4'
);	
create index customer_idx1 on bank.customer (cuname) ;

--  create table bank.account 
--  (	
--      accno varchar(16) primary key not null comment '�˺�',
--  	accstate char(1) default '1' not null  comment '�˻�״̬',
--  	realtimeremain decimal(18,2) default 0 not null  comment 'ʵʱ���',
--  	currency char(3) not null              comment '����',
--  	rate decimal(13,5) not null  default 1 comment '����',
--  	accnature char(1) default '1' not null comment '�˺�����',
--  	cuno varchar(15) not null              comment '�ͻ���',
--  	reserve1 varchar(500)                  comment '�����ֶ�1',
--  	reserve2 varchar(500)                  comment '�����ֶ�2',
--  	reserve3 varchar(500)                  comment '�����ֶ�3',
--  	reserve4 varchar(500)                  comment '�����ֶ�4'
--  )distributed by hash(cuno)(g1,g2,g3,g4);	
--  create index account_idx1 on bank.account (cuno) ;

create table bank.account 
(	
    accno varchar(16) primary key not null comment '�˺�',
	accstate char(1) default '1' not null  comment '�˻�״̬',
	realtimeremain decimal(18,2) default 0 not null  comment 'ʵʱ���',
	currency char(3) not null              comment '����',
	rate decimal(13,5) not null  default 1 comment '����',
	accnature char(1) default '1' not null comment '�˺�����',
	cuno varchar(15) not null              comment '�ͻ���',
	reserve1 varchar(500)                  comment '�����ֶ�1',
	reserve2 varchar(500)                  comment '�����ֶ�2',
	reserve3 varchar(500)                  comment '�����ֶ�3',
	reserve4 varchar(500)                  comment '�����ֶ�4'
);

create index account_idx1 on bank.account (cuno) ;

create table bank.bill 
(	
    flowno varchar(19) not null             comment '��ˮ��',
	flowdate date not null                  comment '����',
	accno varchar(16) not null              comment '�˺�',
	debitamount decimal(18,2) not null      comment '�跽������',
	credityield decimal(18,2) not null      comment '����������',
	cuno varchar(15) not null               comment '�ͻ���',
	abscode char(2)                         comment 'ժҪ��',
	note varchar(200)                       comment '����',
	reserve1 varchar(500)                   comment '�����ֶ�1',
	reserve2 varchar(500)                   comment '�����ֶ�2',
	reserve3 varchar(500)                   comment '�����ֶ�3',
	reserve4 varchar(500)                   comment '�����ֶ�4',
	constraint pk_bill primary key (accno, flowno)
);	

create table bank.journal 
(	
    flowno varchar(19)      not null      comment '��ˮ��',
	flowdate date not null                comment '����',
	amount decimal(18,2) not null         comment '������',
	debitacc varchar(16) not null         comment '�跽�˺�',
	creditacc varchar(16) not null        comment '�����˺�',
	errcode char(4)             comment '������',
	state char(1)                         comment '״̬',
	reserve1 varchar(500)                 comment '�����ֶ�1',
	reserve2 varchar(500)                 comment '�����ֶ�2',
	reserve3 varchar(500)                 comment '�����ֶ�3',
	reserve4 varchar(500)                 comment '�����ֶ�4',
	constraint pk_journal primary key (flowno, debitacc,creditacc)
);	
