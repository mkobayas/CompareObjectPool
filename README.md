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

## Simple Single Pool

### 1 Thread

| poolsize 	| Commons Pool 	| Commos Pool2 	| SimpleLockFreeObjectPool 	|
|:--------:	|:------------:	|:------------:	|:------------------------:	|
|        1 	|    3,320,464 	|    3,880,341 	|               15,920,249 	|
|       10 	|    3,355,802 	|    3,793,361 	|               14,939,606 	|
|      100 	|    3,284,267 	|    3,867,979 	|               14,829,086 	|
|    1,000 	|    3,320,200 	|    3,833,600 	|               15,889,029 	|


### 10 Threads

| poolsize | Commons Pool | Commos Pool2 | SimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:------------------------:|
|        1 |      113,377 |      234,363 |                3,952,861 |
|       10 |      614,072 |      864,738 |                6,650,565 |
|      100 |      610,891 |      879,401 |                6,634,705 |
|    1,000 |      615,388 |      850,219 |                6,865,540 |

### 100 Threads

| poolsize | Commons Pool | Commos Pool2 | SimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:------------------------:|
|        1 |      111,336 |      128,977 |                4,060,305 |
|       10 |      125,296 |      216,886 |                6,479,465 |
|      100 |      617,395 |      854,476 |                6,555,683 |
|    1,000 |      590,584 |      862,999 |                6,552,637 |

### 1000 Threads

| poolsize | Commons Pool | Commos Pool2 | SimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:------------------------:|
|        1 |       78,716 |      112,547 |                3,607,342 |
|       10 |      100,525 |      108,979 |                6,438,195 |
|      100 |      101,144 |      217,707 |                6,677,660 |
|    1,000 |      519,062 |      826,231 |                6,917,719 |

### 1000 Threads with sleep 1 ms per borrow

This scenario cause very high hard interrupt and context switch.  
Maximum transaction is 1,000,000 tps in the ideal situation.  

| poolsize | Commons Pool | Commos Pool2 | SimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:------------------------:|
|        1 |          853 |          850 |                      875 |
|       10 |        8,428 |        9,168 |                    9,042 |
|      100 |       69,037 |       84,582 |                   93,259 |
|    1,000 |      208,188 |      297,538 |                  875,781 |

## Keyed Pool ( key range = 0-9)

### 1 Thread

| poolsize | Commons Pool | Commos Pool2 | KeyedSimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:-----------------------------:|
|        1 |    2,204,008 |    1,345,254 |                    10,031,789 |
|       10 |    2,205,502 |    1,349,236 |                    10,154,906 |
|      100 |    2,202,482 |    1,350,118 |                    10,165,847 |
|    1,000 |    2,203,696 |    1,350,259 |                    10,201,876 |


### 10 Threads

| poolsize | Commons Pool | Commos Pool2 | KeyedSimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:-----------------------------:|
|        1 |      121,768 |      486,833 |                     1,824,816 |
|       10 |      283,950 |      621,488 |                    30,952,604 |
|      100 |      285,425 |      630,090 |                    30,194,467 |
|    1,000 |      279,160 |      627,237 |                    30,808,862 |

### 100 Threads

| poolsize | Commons Pool | Commos Pool2 | KeyedSimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:-----------------------------:|
|        1 |       22,744 |      139,053 |                     1,956,001 |
|       10 |       88,715 |      139,560 |                    28,468,021 |
|      100 |      276,970 |      137,699 |                    28,026,460 |
|    1,000 |      288,539 |      134,512 |                    29,418,600 |


### 1000 Threads

| poolsize | Commons Pool | Commos Pool2 | KeyedSimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:-----------------------------:|
|        1 |        4,230 |      113,789 |                     2,431,161 |
|       10 |        4,428 |       32,908 |                    28,342,516 |
|      100 |        5,037 |      116,860 |                    26,144,774 |
|    1,000 |      248,073 |      118,581 |                    24,981,358 |


### 1000 Threads with sleep 1 ms per borrow

This scenario cause very high hard interrupt and context switch.  
Maximum transaction is 1,000,000 tps in the ideal situation.  

| poolsize | Commons Pool | Commos Pool2 | KeyedSimpleLockFreeObjectPool |
|:--------:|:------------:|:------------:|:-----------------------------:|
|        1 |        2,900 |        7,136 |                         7,511 |
|       10 |        3,540 |       29,177 |                        89,851 |
|      100 |       86,866 |      103,998 |                       609,438 |
|    1,000 |      122,639 |      102,462 |                       784,193 |
