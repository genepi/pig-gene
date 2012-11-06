/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to filter a specified range
 * of positions and to join the input file
 * with the refererce file to gain the
 * rs-number. This script outputs the 
 * chromosome, the position, the rs-number
 * and the genotype information
 * 
 * call this script like this:
 * pig -param input=GeneSamples/input/range.txt -param ref=GeneRefFile/00-All.vcf -param chrom=20 -param start=70 -param end=80 -param accuracy=10 positionRange.pig
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;
 
inputFile = LOAD '$input' USING pigGene.PigGeneStorage();
Xyz = FILTER inputFile BY $chrom == ($0 + 0) AND ($1 + 0) >= ($start + 0 - $accuracy) AND ($1 + 0) <= ($end + 0 + $accuracy);
DUMP Xyz;

/* 
Abc = FILTER inputFile BY $chrom == ($0 + 0) AND pigGene.FilterRange(($1 + 0), $accuracy, $start, $end);
A = FILTER inputFile BY $chrom == $0 ;
B = FILTER A BY pigGene.FilterRange(A.$1, $accuracy, $start, $end);
refFile = LOAD '$ref' USING PigStorage();

B = FOREACH refFile GENERATE $0, $1;
C = JOIN A BY ($0,$1), B BY ($0,$1);

DUMP C;
*/