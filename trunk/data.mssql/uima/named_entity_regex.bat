@set DB=VACS_PROGNOTES
@set SERVER=localhost
@set SCHEMA=ESLD

osql -d %DB% -E -S %SERVER% -i named_entity_regex.sql
bcp %DB%.%SCHEMA%.named_entity_regex in named_entity_regex.bcp -T -S %SERVER% -f named_entity_regex.fmt
