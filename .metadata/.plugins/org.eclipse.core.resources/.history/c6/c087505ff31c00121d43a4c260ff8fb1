/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script loads a test file
 * and removes all lines containing 
 * an rs Number. Finally stores the
 * new relation on the HDFS.
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;
refFile = LOAD 'GeneSamples/rsNoInput.txt' USING PigStorage('\t')
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray);
outputFile = FILTER refFile BY pigGene.RemoveRsNumber(id);
STORE outputFile INTO 'GeneSamples/out';