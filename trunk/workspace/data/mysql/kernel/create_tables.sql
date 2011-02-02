drop table if exists kernel_eval; 
create table kernel_eval (
  kernel_eval_id int auto_increment not null primary key,
  name varchar(255) not null default '',
  instance_id1 int not null,
  instance_id2 int not null,
  similarity double not null
);

CREATE UNIQUE INDEX NK_kernel_eval ON kernel_eval
(
	name, instance_id1, instance_id2
);

drop table if exists concept_graph;
create table concept_graph (
  concept_graph_id int auto_increment not null primary key,
  depthMax int not null,
  conceptMap longblob not null,
  sabs varchar(1000) not null default ''
);

drop table if exists concept_graph_root;
create table concept_graph_root (
	concept_graph_id int not null comment 'fk concept_graph',
	cui char(10) not null,
	primary key (concept_graph_id, cui)
);

drop table if exists corpus;
create table corpus (
  corpus_id int auto_increment not null primary key,
  corpus_name varchar(100) not null,
  unique key corpus_name (corpus_name)
);

drop table if exists corpus_term;
create table corpus_term (
  corpus_term_id int auto_increment not null primary key,
  corpus_id int not null comment 'fk corpus',
  concept_id varchar(10) not null,
  frequency double not null default 0,
  info_content double null,
  unique key nk_corpus_term (corpus_id, concept_id)
);

drop table if exists info_content;
create table info_content (
	info_content_id int auto_increment not null primary key,
	corpus_id int not null comment 'fk corpus',
	concept_graph_id int not null default 0 comment 'fk concept_graph',
	concept_id char(10) not null,
	frequency double not null default 0,
	info_content double not null default 0,
	unique key nk_info_content (corpus_id, concept_graph_id, concept_id)
);
