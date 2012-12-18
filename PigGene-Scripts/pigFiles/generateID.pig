/* 
 * only for testing purpose...
 * 
 * call this script like this:
 * pig -param input=GeneSamples/idTest.txt generateID.pig
 */

file = LOAD '$input' AS (a:int, b:chararray, id:int);
DUMP file;

modified = FOREACH file GENERATE a, b, 1;
ordered = ORDER modified by a, b, id;

DUMP modified;