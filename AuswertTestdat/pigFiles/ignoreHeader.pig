/* 
 * only for testing purpose...
 *
 * call this script like this:
 * pig -param sample=GeneSamples/unmerged/sample1.vcf -param output=GeneSamples/out ignoreHeader.pig
 */

REGISTER pigGene.jar;
/*sample1 = LOAD '$sample' USING PigStorage('\t') 
			AS (chrom:chararray, pos:int, id:chararray, ref:chararray, 
						alt:chararray, qual:float, filt:chararray, info:chararray, format:chararray, exome:chararray);
sample1Filt = FILTER sample1 BY pigGene.IgnoreHeader(chrom);
*/

sample1Filt = LOAD '$sample' USING pigGene.PigGeneStorageUnmerged();
STORE sample1Filt INTO '$output';