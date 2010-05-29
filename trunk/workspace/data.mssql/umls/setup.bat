@set DB=VACS_PROGNOTES
@set SERVER=localhost
@set SCHEMA=ESLD

@rem drop tables
osql -d %DB% -E -S %SERVER% -i drop_tables.sql
@rem create tables
osql -d %DB% -E -S %SERVER% -i create_tables.sql

@rem load tables
bcp %DB%.%SCHEMA%.umls_ms_2009 in umls_ms_2009.bcp -T -w -S %SERVER%
bcp %DB%.%SCHEMA%.umls_snomed_map in umls_snomed_map.bcp -T -w -S %SERVER%

@rem create indices
osql -d %DB% -E -S %SERVER% -i create_indices.sql

