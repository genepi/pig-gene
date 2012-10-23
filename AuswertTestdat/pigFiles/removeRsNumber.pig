/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script loads a test file
 * and removes all lines containing 
 * an rs Number. Finally stores the
 * new relation on the HDFS.
 * 
 * call this script like this:
 * pig -param sample=GeneSamples/rsNoInput.txt -param output=GeneSamples/out removeRsNumber.pig
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;
refFile = LOAD '$sample' USING PigStorage('\t')
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray);
outputFile = FILTER refFile BY pigGene.RemoveRsNumber(id);
STORE outputFile INTO '$output';