/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script gets two parameters as input
 * and uses this parameter to identify the
 * range which lines (their corresponding #)
 * should be stored into an output file.
 * 
 * @author: Clemens Banas
 */
 
 REGISTER pigGene.jar;
 startLineNo = LOAD '$start' AS (start:int);
 endLineNo = LOAD '$end' AS (end:int);
 input = LOAD 'GeneSamples/parameterInput.txt' AS (key:int, val:chararray);
 
 output = FILTER input BY pigGene.FilterLineRange(startLineNo,endLineNo);
 DUMP output;
 /* STORE output INTO 'GeneSamples/out'; */