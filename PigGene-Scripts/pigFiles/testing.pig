Register pigGene.jar;

/*
 * Testing the error while processing the reference file - columns missing..? 
referenceFile = LOAD 'GeneRefFile/00-All.vcf' USING PigStorage('\t') AS (a:chararray, b:long, c:chararray, d:chararray, e:chararray, f:double, g:chararray, h:chararray);
Store referenceFile into 'GeneSamples/output';
*/


/*
 * Testing the filtering of the reference and indel values 
data = LOAD 'GeneSamples/input/indelTest2.txt' USING pigGene.PigGeneStorage();
dump data;
*/


/* 
 * Testing of my schema-implementation with multiple files - PigStorage('\t','-tagsource') */
data = LOAD 'GeneSamples/in/test' USING pigGene.PigGeneStorageUnmerged('\t','-tagsource');
filtering = FOREAcH data GENERATE file, chrom, pos;
DUMP filtering;

/* schema: file,chrom,pos,id,ref,alt,qual,filt,info,format,genotype */