--calculates the distance between two given locations using the haversine formula
REGISTER pigGene.jar;
REGISTER datafu-0.0.9.jar;
R1 = LOAD '$input1' USING PigStorage('\t') AS (id:int, city:chararray, place:chararray, lat:double, lon:double);
R2 = LOAD '$input2' USING PigStorage('\t') AS (id:int, city:chararray, place:chararray, lat:double, lon:double);
R3 = JOIN R1 BY (id), R2 BY (id);
R4 = FILTER R3 BY R1::city != R2::city;
R5 = FOREACH R4 GENERATE R1::id .. R1::lon, R2::city .. R2::lon, datafu.pig.geo.HaversineDistInMiles(R1::lat, R1::lon, R2::lat, R2::lon) as distance;
STORE R5 INTO '$output1';
