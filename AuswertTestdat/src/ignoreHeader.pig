/* only for testing purpose... */

REGISTER rsNumber.jar;
sample1 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
sample1Filt = FILTER sample1 BY pigGene.IgnoreHeader(chrom);
STORE sample1Filt INTO 'GeneSamples/out';