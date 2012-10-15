/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to calculate the mean quality of
 * the information obtained by the sample file. 
 * 
 * @author: Clemens Banas
 */

REGISTER pigGene.jar;
sample1 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
sample1Filt = FILTER sample1 BY pigGene.IgnoreHeader(chrom);
 
qualValues = FOREACH sample1Filt GENERATE qual;
qualGrouped = GROUP qualValues ALL;
qualCalc = FOREACH qualGrouped GENERATE SUM(qualValues.qual), COUNT(qualValues);
mean = FOREACH qualGrouped GENERATE qualCalc.$0 / qualCalc.$1;

STORE mean INTO 'GeneSamples/out';