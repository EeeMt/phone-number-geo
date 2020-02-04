# 手机归属地查询

## 简介



## Benchmark

工程里已内置三种算法, 跑分情况如下:
```
Benchmark                                  Mode  Cnt        Score       Error  Units
BenchmarkRunner.anotherBinarySearchLookup  avgt    5      393.928 ±     8.368  ns/op
BenchmarkRunner.binarySearchLookup         avgt    5      391.755 ±     3.839  ns/op
BenchmarkRunner.sequenceLookup             avgt    5  1504604.426 ± 25029.623  ns/op
```
二分查找的平均速度为`391ns/op`, 默认使用的是`me.ihxq.projects.pna.algorithm.BinarySearchAlgorithmImpl`, 可以通过`new PhoneNumberLookup(new AlgorithmYouLike());`使用其他算法;
也可自行实现算法, 实现`me.ihxq.projects.pna.algorithm.LookupAlgorithm`即可.
