/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script calculates the median of 
 * the quality from the sample file.
 * 
 * @author: Clemens Banas
 */

REGISTER datafu.jar;
define Median datafu.pig.stats.Median();

sample1 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
sample1Filt = FILTER sample1 BY pigGene.IgnoreHeader(chrom);
sample1Med = FOREACH sample1Filt GENERATE qual;
sample1MedGrouped = GROUP sample1Med ALL;

median = FOREACH sample1MedGrouped { 
	sorted = ORDER sample1Med BY qual; 
	GENERATE Median(sorted.qual);
	};  
	
Dump median;