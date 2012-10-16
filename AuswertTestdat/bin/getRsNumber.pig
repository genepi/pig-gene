/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to gain the rsNumber from the reference file and
 * write it into the corresponding  id-column of the sample file.
 * Finally store the new generated relation (including the rsNumber). 
 * 
 * @author: Clemens Banas
 */

REGISTER pigGene.jar;
sample1 = LOAD 'GeneSamples/sample1.vcf' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
refFile = LOAD 'GeneRefFile/00-All.vcf' USING PigStorage('\t')
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray);

rfp = FOREACH refFile GENERATE chrom, pos, id; /* reduce data amount just before filtering and joining */
s1f = FILTER sample1 BY pigGene.IgnoreHeader(chrom);
rff = FILTER rfp BY pigGene.IgnoreHeader(chrom);

/* if chrome and pos match: add rs Number to the sample1 file */
joined = JOIN rff BY (chrom,pos), s1f BY (chrom,pos);
reordered = FOREACH joined GENERATE s1f::chrom,s1f::pos,rff::id,s1f::ref,s1f::alt,s1f::qual,
				s1f::filt,s1f::info,s1f::format,s1f::exome;

STORE reordered INTO 'GeneSamples/out';