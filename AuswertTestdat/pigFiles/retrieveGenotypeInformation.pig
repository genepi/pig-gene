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
 * pig -param input=GeneSamples/input/range.txt -param output=GeneSamples/output -param ref=GeneRefFile/00-All.vcf -param chrom=20 -param start=70 -param end=80 -param accuracy=10 retrieveGenotypeInformation.pig
 * 
 * @author: Clemens Banas
 */
 
REGISTER pigGene.jar;
input = LOAD '$input' USING pigGene.PigGeneStorate();
in = FOREACH input GENERATE chrom, pos, exome, persID;
inFilter = FILTER in BY $chrom == in.chrom AND in.pos >= $start-$accuracy AND in.pos <= $end+$accuracy;

ref = LOAD '$ref' USING pigGene.PigGeneStorage(); /* last column is not needed -> exome */
refFilter = FOREACH ref GENERATE chrom, pos, id;

joined = JOIN inFilter BY (chrom,pos), refFilter BY (chrom,pos);
out = FOREACH joined GENERATE inFilter::chrom, inFilter::pos, refFilter::id, SUBSTRING(inFilter::exome,0,3), inFilter::persID;
STORE out INTO '$output';