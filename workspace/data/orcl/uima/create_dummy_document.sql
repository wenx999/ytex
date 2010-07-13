CREATE TABLE dummy_document (
	studyid int NOT NULL,
	"uid" int NOT NULL,
	document_type_id int NOT NULL,
	doc_text clob NOT NULL,
	site_id varchar2(4) NOT NULL,
 CONSTRAINT PK_dummy_document PRIMARY KEY  
(
	studyid,
	"uid",
	document_type_id
)
);
