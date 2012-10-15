/**
 * PigGene - BACHELOR PROJECT
 * 
 * Pig script to test simple 
 * joins between two relations.
 * 
 * @author: Clemens Banas
 */
 
leftRel = LOAD 'GeneSamples/simpleJoinInput1.txt' USING PigStorage('\t') AS (l1:chararray,l2:int,l3:int);
rightRel = LOAD 'GeneSamples/simpleJoinInput2.txt' USING PigStorage('\t') AS (r1:int,r2:chararray);

newRel = JOIN leftRel BY l3, rightRel BY r1;
DUMP newRel;

leftRel2 = LOAD 'GeneSamples/simpleJoinInput3.txt' USING PigStorage('\t') AS (l1:int,l2:chararray,l3:int);
rightRel2 = LOAD 'GeneSamples/simpleJoinInput4.txt' USING PigStorage('\t') AS (r1:int,r2:chararray,r3:int);

newRel2 = JOIN leftRel2 BY (l1,l2), rightRel2 BY (r1,r2);
DUMP newRel2;