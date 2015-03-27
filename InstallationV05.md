# Introduction #

In YTEX v0.5 we've implemented the following features:

  * Support for Dictionary Lookup using stemmed words
> cTAKES' dictionary lookup algorithm requires an exact match to the text of a concept in a dictionary.  We've extended this to also support matching stemmed text to the dictionary.
  * Negation detection on words
> cTAKES only supports negation detection for Named Entities.  We've extended cTAKES to perform negation detection on words using the negex algorithm.
  * Tokenizer enhancements
> cTAKES' tokenizer does not split words at certain punctuation marks.  We modified the tokenizer to always split words at the following characters: `.,:;`

## Prerequisites ##
  * JDK 1.6 or higher [download](http://java.sun.com/javase/downloads/widget/jdk6.jsp)
  * Atleast 2 GB of free disk space
  * [we suggest that you install UMLS in your database](http://www.nlm.nih.gov/research/umls/implementation_resources/scripts/README_RRF_MySQL_Output_Stream.html).  NLM provides scripts for installing UMLS in MySQL and Oracle.  Refer to [UMLS SQL Server Installation](UMLS_SQL_SERVER.md) for instructions on how to install UMLS in MS SQL Server.
  * Tested on Linux and Windows.  We don't test YTEX on mac; however, users have successfully installed this on mac following linux installation instructions.

## Database Prerequisites ##
YTEX supports MS SQL Server 2008, MySQL version 5.x, and Oracle versions 10gR2 and above.  To set up database objects, the installation requires database-specific utilities:
  * MS SQL Server: [SQL Server Tools](http://www.microsoft.com/downloads/details.aspx?familyid=08E52AC2-1D62-45F6-9A4A-4B76A8564A2B&displaylang=en) and [SQL Server Command-Line Utilities](http://www.microsoft.com/downloads/en/details.aspx?FamilyId=C6C3E9EF-BA29-4A43-8D69-A2BED18FE73C&displaylang=en)
  * Oracle: [SQL\*Plus](http://docs.oracle.com/cd/E11882_01/server.112/e16604/apd.htm#CHDCHJJJ) and [SQL\*Loader](http://docs.oracle.com/cd/E11882_01/server.112/e22490/ldr_concepts.htm#SUTIL003)
  * MySQL: mysql installation.  We use the [mysql command line tool](http://dev.mysql.com/doc/refman/5.0/en/mysql.html); as far as we know, this is only distributed as part of a full MySQL server installation.  Note that YTEX and MySQL can be installed on separate servers.

Create a database user (and schema) for use with ytex.  See platform specific notes below.

### Oracle ###
As documented [here](http://www.nlm.nih.gov/research/umls/implementation_resources/scripts/index.html) your database must use the UTF-8 charset.

Make sure you use a tablespace with enough room; e.g. create the ytex user and schema like this:
```
create tablespace TBS_YTEX datafile 'C:/oracle/oradata/orcl/TBS_YTEX.dbf' size 1000M autoextend on online;
create user ytex identified by ytex default tablespace TBS_YTEX;
grant connect, resource to ytex;
grant create materialized view to ytex;
grant create view to ytex;
```

If you have installed the UMLS locally, you must also grant ytex select permissions on umls tables; e.g. assuming that umls tables are in the umls schema:
```
grant select on umls.MRCONSO to ytex;
grant select on umls.MRSTY to ytex;
```

### MySQL ###
To create the mysql user and database, login to mysql as root and run the following commands (change as necessary):
```
CREATE DATABASE ytex CHARACTER SET utf8;
CREATE USER 'ytex'@'localhost' IDENTIFIED BY 'ytex';
GRANT ALL PRIVILEGES ON ytex.* TO 'ytex'@'localhost';
```
On mac you should use the 127.0.0.1 instead of localhost.  Note that if ytex connects to the mysql server from a different machine, you should replace localhost with the host name or ip address of the machine you will connect from, or use the wildcard ('%'):
```
CREATE USER 'ytex'@'%' IDENTIFIED BY 'ytex';
GRANT ALL PRIVILEGES ON ytex.* TO 'ytex'@'%';
```
If you have installed UMLS in your database, you must give the ytex user select permission on these tables:
```
GRANT SELECT on umls.* to 'ytex'@'%';
```

The `document` table uses the `text` and `blob` datatypes for the doc\_text column that holds the document text.  If you are processing large documents, you may need to use longtext the datatype instead.  Furthermore, you may have to increase the [maximum packet size](http://dev.mysql.com/doc/refman/5.5/en/packet-too-large.html).

### SQL Server ###
YTEX has been tested with integrated authentication only.  You can use SQL Server Authentication for the YTEX runtime.  However, the YTEX setup requires integrated authentication.  You must have the permission to create database objects in the YTEX database and schema.  If you don't have these permissions, ask your DBA to add you to the `db_ddladmin` & `db_datawriter` roles for the YTEX database.

If you want to install the UMLS in your SQL Server, you may want to use a different database/schema from the YTEX database.  If that is the case, you need permissions on the UMLS database/schema as well.

## Installation ##
  1. Create a directory where ytex will be installed, e.g

Linux:
```
mkdir ${HOME}/clinicalnlp
```

Windows:
```
c:\java\clinicalnlp
```

  1. Download the following archives to the installation directory:

  * YTEX Archive: `ytex-with-dependencies-vX.Y.zip`
> Download from this site

  * LVG 2011 Archive: `lvg2011lite.tgz` (optional)
> ([download](http://lexsrv3.nlm.nih.gov/LexSysGroup/Projects/lvg/2011/release/lvg2011lite.tgz)). Make sure you save this file as lvg2011lite.tgz (your browser may decompress or expand this file). The lvg is required for stemming; if you do not download this file, stemming will not be performed.

  * UMLS Database Export (Required if you do not have UMLS installed in your DB)
> UMLS is used for Named Entity Recognition.  The YTEX distribution includes a tiny subset of the UMLS for demonstration purposes.  For production use, [we suggest that you install UMLS locally](http://www.nlm.nih.gov/research/umls/implementation_resources/scripts/README_RRF_MySQL_Output_Stream.html).  If you cannot/don't want to install UMLS, you can get a subset of the UMLS 2010AB that contains SNOMED-CT and RXNORM from us:
    * If you have not done so already, obtain a UMLS License and create a UMLS Technology Services (UTS) Account, available free of charge: https://uts.nlm.nih.gov/home.html
    * Download the YTEX UMLS Database Archive from http://www.ytex-nlp.org/umls.download/secure/index.html].  A valid UTS Login is required.
    * Place the archive (`umls-<platform>.zip`) in the installation directory as well
  1. Extract `ytex-with-dependencies-vX.Y.zip` to the installation directory; this will create a ytex subdirectory, e.g. windows `c:\java\clinicalnlp\ytex` / linux `${HOME}/clinicalnlp/ytex`, referred to subsequently as YTEX\_HOME.
  1. Edit environment batch/shell script, and Fix the path references to match your environment
    * windows edit <tt>YTEX_HOME\setenv.cmd</tt>
    * linux move <tt>YTEX_HOME/ytex.profile</tt> to <tt>${HOME}/ytex.profile</tt>
  1. Create `YTEX_HOME\config\desc\ytex.properties`: In this file, you specify the database connection parameters. Use `YTEX_HOME\config\desc\ytex.properties.<db type>.example` as a template.  If you have UMLS installed on your database, specify the `umls.schema` and `umls.catalog` properties (see the properties file for an explanation of what these are).  Before you continue, verify that you can connect to your database using the native database utilities.  Open a command prompt/shell and run the following commands (you may have to specify the full path to the executable).  If you cannot connect to your database using this command, then you must fix your configuration before proceeding.
    * MySQL (`${xxx}` refers to properties in `ytex.properties`):
```
mysql --user=${db.username} --password=${db.password} --host=${db.host} ${db.schema}
```
    * Oracle:
```
sqlplus ${db.username}/${db.password}@${db.name}
```
    * SQL Server:
```
sqlcmd -d ${db.name} -E -S ${db.host}
```
  1. Execute the setup script.
    * **windows**: Open a command prompt, navigate to YTEX\_HOME, and execute setup script:
```
cd /d c:\clinicalnlp\ytex
setup all
```
    * **linux**: From a shell, cd to the YTEX\_HOME directory, set the environment, make sure necessary scripts are executable, and execute the ant script:
```
chmod u+x ${HOME}/ytex.profile
cd ${HOME}/clinicalnlp/ytex
chmod u+x ../apache-ant-1.8.0/bin/ant
chmod u+x *.sh
. ${HOME}/ytex.profile
ant -buildfile build-setup.xml -Dytex.home=${YTEX_HOME} all > setup.out 2>&1 &
tail -f setup.out
```

This will call the ant script `build-setup.xml`, which does the following:
  * Unpacks lvg data files
  * Generates configuration files from templates
  * Sets up Semantic Search Web Application
> The semantic search web application will be deployed to a tomcat server under `YTEX_HOME\web\catalina`.
  * Sets up YTEX Database Objects
> The installation executes SQL scripts located in the `YTEX_HOME\data` directory, and imports umls tables (if they are not already in the database). All YTEX database objects will be dropped and recreated. If this is the initial installation, ignore the errors about objects not existing when they are being dropped.  If you have installed the UMLS in your database and configured YTEX to use it, YTEX will create a dictionary lookup table with all concepts from the UMLS.  The setup speed is dependent on the latency between the machine you are installing on and the database server.  This can take several hours.

## Notes on UMLS Installation ##
During setup, we try to see if the UMLS is installed in the database (we look for the MRCONSO table); if we don't find it, we look for a <tt>umls-(platform).zip</tt> file with the tables. If we don't find that we load the sample data files included in the YTEX distribution - this contains a tiny subset of the UMLS for use with the YTEX examples.

The cTAKES Database Lookup algorithm requires a table/view that contains the first word of a concept, a concept code, and the full tokenized text of the concept.  The <tt>MRCONSO</tt> table contains concept codes (CUI field) and the text of the concept.  We generate a table <tt>umls_aui_fword</tt> that contains the first word and tokenized text of every concept in the <tt>MRCONSO</tt> table.  We then join the <tt>umls_aui_fword</tt> and <tt>MRCONSO</tt> tables, to create a table that contains a subset of the UMLS (by default, the SNOMED-CT and RXNORM vocabularies).  You are free to replace this with subsets of the UMLS of your liking; refer to [Dictionary Lookup Configuration](DictionaryLookup_V05.md).

### Differences between cTAKES/ARC/YTEX ###
You are free to use whatever dictionary you like with cTAKES/ARC/YTEX.  'By default' cTAKES/ARC are configured to use a database table for SNOMED-CT, and a lucene index for RXNORM.  In addition cTAKES/ARC store the SNOMED Codes for concepts.

In contrast, YTEX uses only a database table for lookup, and only stores the UMLS CUIs.  The reasons for this include:
  * Performance
> Using the RXNORM lucene index incurs some memory requirements (you need a 1gb heap at least to load the lucene index).  For each UMLS concept it finds, cTAKES executes another query to find the corresponding SNOMED codes.  Therefore, for each word that cTAKES tries to map, cTAKES is running 2 queries (snomed + rxnorm lookups) + n queries (cui to snomed lookup).  YTEX in contrast looks in a single database table.  Thus, the dictionary lookup in YTEX runs just 1 query (umls lookup).
  * Cleaner Data Model
> cTAKES/ARC store the SNOMED-CT code and the UMLS CUI.  YTEX in contrast stores only the UMLS CUI: YTEX is DB-oriented, and storing only the UMLS CUI corresponds to a normalized data model.  To map UMLS CUIs to codes from any source vocabulary (e.g. SNOMED-CT/RXNORM), you can simply join the <tt>ytex.anno_ontology_concept</tt> and <tt>umls.mrconso</tt> tables.

If you prefer the way cTAKES is doing things, you can configure YTEX to get the same functionality.  Refer to [the cTAKES documentation](http://ohnlp.sourceforge.net/cTAKES/#boost_performance) for information on setting up the tables / lucene indices and configuring the Dictionary Lookup Annotator.

## Notes on running examples on linux ##
We've created .sh files for many of the .cmd files.  For the others, you should be able to figure out what to do by looking at the .cmd files.