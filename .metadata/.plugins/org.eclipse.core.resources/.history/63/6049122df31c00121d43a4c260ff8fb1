/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to filter just the first
 * 15 lines of the sample1 file and store
 * these 15 lines to a new file. 
 * 
 * @author: Clemens Banas
 */

REGISTER pigGene.jar;
sample1 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
sample1Filt = FILTER sample1 BY pigGene.IgnoreHeader(chrom);
lessLines = FILTER sample1Filt BY pigGene.FilterLines(chrom);

STORE lessLines INTO 'GeneSamples/out';