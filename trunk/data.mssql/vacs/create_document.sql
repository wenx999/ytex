

create table $(db_schema).docdate_annotation (
	document_annotation_id int not null,
	doc_date datetime,
	primary key (document_annotation_id),
	foreign key (document_annotation_id) references $(db_schema).document_annotation(document_annotation_id) ON DELETE CASCADE
);

create table $(db_schema).dockey_annotation (
	document_annotation_id int not null,
	study_id numeric(20,0) not NULL,
	uid int not NULL,
	site_id varchar(4) not null default '',
	document_type_id int NOT NULL,
	primary key (document_annotation_id),
	foreign key (document_annotation_id) references $(db_schema).document_annotation(document_annotation_id) ON DELETE CASCADE
);


CREATE INDEX IX_dockey_anno_all ON $(db_schema).dockey_annotation
(
	document_type_id,
	study_id,
	uid,
	site_id
)
;

CREATE INDEX IX_dockey_anno_studyid ON $(db_schema).dockey_annotation
(
	study_id
)
;

CREATE INDEX IX_dockey_anno_uid ON $(db_schema).dockey_annotation
(
	uid
)
;
