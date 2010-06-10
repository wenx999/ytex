

CREATE TABLE $(db_schema).[document](
	[document_id] [int] IDENTITY(1,1) NOT NULL,
	[analysis_batch] [varchar](50) NOT NULL,
	[cas] [varbinary](max) NULL,
	[doc_text] [nvarchar](max) NULL,
 CONSTRAINT [PK_document] PRIMARY KEY CLUSTERED 
(
	[document_id]
)
)
;

CREATE NONCLUSTERED INDEX [IX_document_analysis_batch] ON $(db_schema).[document] 
(
	[analysis_batch]
)
;

CREATE TABLE $(db_schema).[document_class](
	[document_class_id] [int] IDENTITY(1,1) NOT NULL,
	[document_id] [int] NOT NULL,
	[task] [varchar](50) NOT NULL,
	[class_auto] [int] NOT NULL,
	[class_gold] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[document_class_id]
)
)
;

CREATE UNIQUE NONCLUSTERED INDEX [NK_document_class] ON $(db_schema).[document_class] 
(
	[document_id] ASC,
	[task] ASC
)
;

create table $(db_schema).document_annotation (
	document_annotation_id int identity not null, 
	document_id int not null, 
	span_begin int,
	span_end int,
	uima_type_id int not null,
	primary key (document_annotation_id),
	foreign key (document_id) references $(db_schema).document (document_id) ON DELETE CASCADE,
	foreign key (uima_type_id) references $(db_schema).ref_uima_type (uima_type_id)
)
;

CREATE INDEX IX_docanno_doc ON $(db_schema).document_annotation (document_id)
;

create table $(db_schema).sentence_annotation (
	document_annotation_id int not null,
	sentence_number int,
	primary key (document_annotation_id),
	foreign key (document_annotation_id) references $(db_schema).document_annotation(document_annotation_id)  ON DELETE CASCADE
);

create table $(db_schema).named_entity_annotation (
	document_annotation_id int not null, 
	discovery_technique int,
	status int,
	certainty int,
	type_id int,
	confidence float,
	segment_id varchar(64),
	primary key (document_annotation_id),
	foreign key (document_annotation_id) references $(db_schema).document_annotation(document_annotation_id)  ON DELETE CASCADE
);

create table $(db_schema).ontology_concept_annotation (
	ontology_concept_annotation_id int identity not null, 
	document_annotation_id int not null,
	coding_scheme varchar(20),
	code varchar(20),
	oid varchar(10),
	primary key (ontology_concept_annotation_id),
	foreign key (document_annotation_id) references $(db_schema).named_entity_annotation(document_annotation_id)  ON DELETE CASCADE
);

create table $(db_schema).umls_concept_annotation (
	ontology_concept_annotation_id int not null,
	cui varchar(10),
	primary key (ontology_concept_annotation_id),
	foreign key (ontology_concept_annotation_id) references $(db_schema).ontology_concept_annotation(ontology_concept_annotation_id)  ON DELETE CASCADE
);

CREATE INDEX IX_umls_concept_cui ON $(db_schema).umls_concept_annotation (cui)
;

CREATE TABLE $(db_schema).[segment_annotation](
	[document_annotation_id] [int] NOT NULL,
	[segment_id] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[document_annotation_id] ASC
)
)
;

ALTER TABLE $(db_schema).[segment_annotation]  WITH CHECK ADD FOREIGN KEY([document_annotation_id])
REFERENCES $(db_schema).[document_annotation] ([document_annotation_id])
ON DELETE CASCADE
;

CREATE NONCLUSTERED INDEX [IX_segment_anno_seg] ON $(db_schema).[segment_annotation] 
(
	[document_annotation_id] ASC,
	[segment_id] ASC
)
;

/*
 * mapped to SourceDocumentInformation
 */
create table $(db_schema).anno_source_doc_info (
	[document_annotation_id] [int] NOT NULL,
	uri varchar(256),
	offset_in_source int,
	document_size int,
	last_segment bit,
	PRIMARY KEY CLUSTERED 
	(
		[document_annotation_id] ASC
	),
	foreign key (document_annotation_id) references $(db_schema).document_annotation(document_annotation_id)  ON DELETE CASCADE
);


/**
 * mapped to BaseToken
 */
create table $(db_schema).anno_base_token (
	[document_annotation_id] [int] NOT NULL,
	token_number int,
	normalized_form varchar(256),
	part_of_speech varchar(5),
	PRIMARY KEY CLUSTERED 
	(
		[document_annotation_id] ASC
	),
	foreign key (document_annotation_id) 
		references $(db_schema).document_annotation(document_annotation_id)  
		ON DELETE CASCADE
);

/**
 * BaseToken.lemmaEntries
create table $(db_schema).anno_lemma (
	anno_lemma_id [int] IDENTITY(1,1) NOT NULL,
	document_annotation_id int not null,
	lemma_key varchar(10),
	pos_tag varchar(5),
	PRIMARY KEY CLUSTERED 
	(
		[anno_lemma_id] ASC
	),
	foreign key (document_annotation_id) 
		references $(db_schema).anno_base_token(document_annotation_id)  
		ON DELETE CASCADE

);
 */

/**
 * mapped to NumToken
 */
create table $(db_schema).anno_num_token (
	[document_annotation_id] [int] NOT NULL,
	num_type int,
	PRIMARY KEY CLUSTERED 
	(
		[document_annotation_id] ASC
	),
	foreign key (document_annotation_id) 
		references $(db_schema).anno_base_token(document_annotation_id)  
		ON DELETE CASCADE
)

/**
 * mapped to WordToken
 */
create table $(db_schema).anno_word_token (
	[document_annotation_id] [int] NOT NULL,
	capitalization [int],
	num_position [int],
	suggestion [int],
	canonical_form varchar(256),
	PRIMARY KEY CLUSTERED 
	(
		[document_annotation_id] ASC
	),
	foreign key (document_annotation_id) 
		references $(db_schema).anno_base_token(document_annotation_id)  
		ON DELETE CASCADE
);

