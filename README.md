CompareObjectPool
=================

Compare perfomance.

- Commons Pool
- Commons Pool2
- SimpleLockFreeObjectPool

Borrow/release transactions per sec.

## Machine
Intel(R) Core(TM) i7-3770 CPU @ 3.40GHz  
Fedora release 18 (Spherical Cow)  
OpenJDK 1.7.0_19  


## 1 Thread

| poolsize 	| Commons Pool 	| Commos Pool2 	| SimpleLockFreeObjectPool 	|
|:--------:	|:------------:	|:------------:	|:------------------------:	|
|        1 	|    3,320,464 	|    3,880,341 	|               15,920,249 	|
|       10 	|    3,355,802 	|    3,793,361 	|               14,939,606 	|
|      100 	|    3,284,267 	|    3,867,979 	|               14,829,086 	|
|    1,000 	|    3,320,200 	|    3,833,600 	|               15,889,029 	|


## 10 Threads

| poolsize | Commons Pool | Commos Pool2 | SimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:------------------------:|
|        1 |      113,377 |      234,363 |                3,952,861 |
|       10 |      614,072 |      864,738 |                6,650,565 |
|      100 |      610,891 |      879,401 |                6,634,705 |
|    1,000 |      615,388 |      850,219 |                6,865,540 |

## 100 Threads

| poolsize | Commons Pool | Commos Pool2 | SimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:------------------------:|
|        1 |      111,336 |      128,977 |                4,060,305 |
|       10 |      125,296 |      216,886 |                6,479,465 |
|      100 |      617,395 |      854,476 |                6,555,683 |
|    1,000 |      590,584 |      862,999 |                6,552,637 |

## 1000 Threads

| poolsize | Commons Pool | Commos Pool2 | SimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:------------------------:|
|        1 |       78,716 |      112,547 |                3,607,342 |
|       10 |      100,525 |      108,979 |                6,438,195 |
|      100 |      101,144 |      217,707 |                6,677,660 |
|    1,000 |      519,062 |      826,231 |                6,917,719 |
