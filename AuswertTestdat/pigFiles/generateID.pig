/* only for testing purpose... */

file = LOAD 'GeneSamples/idTest.txt' AS (a:int, b:chararray, id:int);
DUMP file;

modified = FOREACH file GENERATE a, b, 1;
ordered = ORDER modified by a, b, id;

DUMP modified;