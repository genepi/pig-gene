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

sample1Filt = FILTER sample1 BY pigGene.IgnoreHeader(chrom);
refFileFilt = FILTER refFile BY pigGene.IgnoreHeader(chrom);

/* wenn chrom und pos Ÿbereinstimmt, dann hol ich mir die rs nummer und erstell eine neue Relation */
joined = JOIN refFileFilt BY (chrom,pos), sample1Filt BY (chrom,pos) USING 'replicated';
reordered = FOREACH joined GENERATE sample1.chrom,sample1.pos,refFile.id,sample1.ref,sample1.alt,sample1.qual,
				sample1.filt,sample1.info,sample1.format,sample1.exome;
STORE reordered INTO 'GeneSamples/out';

/* TODO: fix!!! */