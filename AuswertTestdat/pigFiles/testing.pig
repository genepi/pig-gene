
Register pigGene.jar;
/* 
referenceFile = LOAD 'GeneRefFile/00-All.vcf' USING PigStorage('\t') AS (a:chararray, b:long, c:chararray, d:chararray, e:chararray, f:double, g:chararray, h:chararray);
Store referenceFile into 'GeneSamples/output';
*/

data = LOAD 'GeneSamples/input/indelTest.txt' USING pigGene.PigGeneStorage();
dump data;

/*Store data into 'GeneSamples/output';
 * 
 */