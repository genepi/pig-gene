/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script gets two parameters as input. These parameters define the location 
 * of the two input files. Each file defines only a single integer value which is 
 * used to identify the range which lines (their corresponding number) should be 
 * stored into the output file.
 * 
 * call this script like this:
 * pig -param start=comParamStart.txt -param end=comParamEnd.txt getComParameter.pig
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;
startLineNo = LOAD 'GeneSamples/$start' AS (start:int); 
endLineNo = LOAD 'GeneSamples/$end' AS (end:int);
inputFile = LOAD 'GeneSamples/parameterInput.txt' AS (key:int, val:chararray);

outputFile = FILTER inputFile BY pigGene.FilterLineRange(startLineNo.start,endLineNo.end);
STORE outputFile INTO 'GeneSamples/out';