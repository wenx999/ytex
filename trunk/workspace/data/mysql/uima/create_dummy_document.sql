CREATE TABLE dummy_document(
	studyid int NOT NULL,
	uid int identity(1,1) NOT NULL,
	document_type_id int NOT NULL,
	doc_text text NOT NULL,
	site_id varchar(4) NOT NULL default '',
 CONSTRAINT PK_dummy_document PRIMARY KEY 
(
	studyid,
	uid,
	document_type_id
)
)
