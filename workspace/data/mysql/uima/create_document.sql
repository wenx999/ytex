CREATE TABLE document(
	document_id int /* AUTO_INCREMENT */ NOT NULL,
	instance_id bigint not null default 0,
	instance_key varchar(256) null comment 'mapped to DocumentID.DocumentID',
	analysis_batch varchar(50) NOT NULL,
	cas longblob NULL,
	doc_text text NULL,
	CONSTRAINT PK_document PRIMARY KEY
	(
		document_id
	)
) engine=myisam
;

CREATE INDEX IX_document_analysis_batch ON document 
(
	analysis_batch,
	document_id
)
;

CREATE INDEX IX_instance_id ON document 
(
	instance_id
)
;

create table anno_base (
	anno_base_id int /* AUTO_INCREMENT */  not null,
	document_id int not null  comment 'fk document',
	span_begin int,
	span_end int,
	uima_type_id int not null comment 'fk ref_uima_type',
	primary key (anno_base_id)
)engine=myisam
;

ALTER TABLE `anno_base` 
	ADD INDEX `IX_type_span`(`document_id`, `span_begin`, `span_end`, `uima_type_id`),
	ADD INDEX `IX_type`(`document_id`, `uima_type_id`);
 
CREATE INDEX IX_docanno_doc ON anno_base (document_id)
;

create table anno_sentence (
	anno_base_id int not null comment 'fk anno_base',
	sentenceNumber int,
	segmentId varchar(20),
	primary key (anno_base_id)
)engine=myisam;

create table anno_named_entity (
	anno_base_id int not null comment 'fk anno_base',
	discoveryTechnique int,
	status int,
	certainty int,
	typeID int,
	confidence float,
	segmentID varchar(20),
	primary key (anno_base_id)
)engine=myisam;

create table anno_ontology_concept (
	anno_ontology_concept_id int auto_increment not null,
	anno_base_id int not null comment 'fk anno_base',
	code varchar(20) comment 'OntologyConcept.code',
	cui char(8) comment 'UmlsConcept.cui',
	disambiguated bit not null default 0 comment 'ytex OntologyConcept.disambiguated',
	primary key (anno_ontology_concept_id),
	KEY `IX_anno_base_id` (`anno_base_id`),
	KEY `IX_code` (`code`),
	KEY `IX_anno_code` (`anno_base_id`,`code`)
)engine=myisam;

CREATE TABLE anno_segment(
	anno_base_id int NOT NULL  comment 'fk anno_base',
	id varchar(20) NULL,
PRIMARY KEY
(
	anno_base_id ASC
)
)engine=myisam
;

CREATE INDEX IX_segment_anno_seg ON anno_segment
(
	anno_base_id ASC,
	id ASC
)
;

-- mapped to BaseToken, WordToken
create table anno_token (
	anno_base_id int NOT NULL  comment 'fk anno_base',
	tokenNumber int NOT NULL default 0 comment 'BaseToken',
	normalizedForm varchar(20) comment 'BaseToken',
	partofSpeech varchar(5) comment 'BaseToken',
	coveredText varchar(20) null,
	capitalization int not null default 0 comment 'ctakes WordToken',
	numPosition int not null default 0 comment 'ctakes WordToken',
	suggestion varchar(20) comment 'ctakes WordToken',
	canonicalForm varchar(20) comment 'ctakes WordToken',
	negated bit not null default 0 comment 'ytex WordToken',
	possible bit not null default 0  comment 'ytex WordToken',
	PRIMARY KEY
	(
		anno_base_id ASC
	),
	KEY `IX_coveredText` (`coveredText`),
	KEY `IX_canonicalForm` (`canonicalForm`)
) engine=myisam;

create table anno_date (
	anno_base_id int not null  comment 'fk anno_base',
	tstamp datetime,
	primary key (anno_base_id) 
) engine=myisam;

create table anno_drug_mention (
	anno_base_id int not null primary key comment 'fk anno_base',
	status int not null default 0,
	frequency varchar(20),
	duration varchar(20),
	route varchar(20),
	drugChangeStatus varchar(10),
	dosage varchar(20),
	strength varchar(20),
	form varchar(20),
	frequencyUnit varchar(20),
	startDate varchar(20)
) engine=myisam;

create table anno_markable (
	anno_base_id int not null primary key comment 'fk anno_base',
	id int default 0,
	anaphoric_prob double default 0,
	content int default 0
) engine=myisam;

create table anno_treebank_node (
	anno_base_id int not null primary key comment 'fk anno_base',
	parent int default 0,
	nodeType varchar(10),
	nodeValue varchar(10),
	leaf bit default 0,
	headIndex int default 0,
	`index` int default 0,
	tokenIndex int default 0
) engine=myisam;

create table anno_link (
	anno_link_id int auto_increment not null primary key,
	parent_anno_base_id int not null comment 'parent anno fk anno_base',
	child_anno_base_id int not null comment 'child anno fk anno_base',
	feature varchar(20),
	key IX_link (parent_anno_base_id, child_anno_base_id, feature)
) engine=myisam;

create table anno_contain (
  parent_anno_base_id int not null comment 'parent anno fk anno_base',
  parent_uima_type_id int not null comment 'parent type',
  child_anno_base_id int not null comment 'child anno fk anno_base',
  child_uima_type_id int not null comment 'child type',
  primary key (parent_anno_base_id, child_anno_base_id),
  key IX_parent_id_child_type (parent_anno_base_id, child_uima_type_id),
  key IX_child_id_parent_type (child_anno_base_id, parent_uima_type_id)
) engine=myisam, comment 'containment relationships between annotations';


CREATE TABLE fracture_demo(
	note_id int auto_increment NOT NULL primary key,
	site_id varchar(10) NULL,
	note_text text NULL,
	fracture varchar(20) NULL,
	note_set varchar(10) NULL
) engine=myisam, comment 'demo data';
