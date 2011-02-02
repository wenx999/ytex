drop table CMCDocument;
create table CMCDocument (
  	documentId int not null,
    documentSet varchar(50) not null,
	clinicalHistory varchar(2000) not null,
	impression varchar(2000) not null,

  	CONSTRAINT PK_Document PRIMARY KEY
	(
	documentId
	)
);

drop table CMCDocumentCode;
create table CMCDocumentCode (
	documentId int not null,
	code varchar(50) not null,
	CONSTRAINT PK_DocumentCode PRIMARY KEY
	(
	documentId, code
	)
);
