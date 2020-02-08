# 手机归属地查询

## 简介
根据手机号前**7**位确定手机号运营商即归属地, 支持包括虚拟运营商的中国大陆手机号查询.

## 数据源

数据源`dat`文件来自[xluohome/phonedata](https://github.com/xluohome/phonedata)提供的数据库, 会不定时同步更新数据库

当前数据源版本: `201911`
## maven
```xml
<dependency>
    <groupId>me.ihxq.projects</groupId>
    <artifactId>phone-number-geo</artifactId>
    <version>1.0.0-201911</version>
</dependency>
```
版本号解释:  
![](./version_explain.png)

## 示例
```java
class Demo1{
    public static void main(String[] args){
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup();
        PhoneNumberInfo found = phoneNumberLookup.lookup("18798896741").orElseThrow(RuntimeException::new);
    }
}
```
```java
class Demo2{
    public static void main(String[] args){
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup();
        String province = phoneNumberLookup.lookup("130898976761")
                        .map(PhoneNumberInfo::getAttribution)
                        .map(Attribution::getProvince)
                        .orElse("未知");
    }
}
```
```java
class Demo3{
    public static void main(String[] args){
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup();
        PhoneNumberInfo found = phoneNumberLookup.lookup("18798896741").orElseThrow(RuntimeException::new);
        found.getNumber(); // 18798896741
        found.getAttribution().getProvince(); // 贵州
        found.getAttribution().getCity(); // 贵阳
        found.getAttribution().getZipCode(); // 550000
        found.getAttribution().getAreaCode(); // 0851
        found.getIsp(); // ISP.CHINA_MOBILE
    }
}
```

## 对比`libphonenumber`
对比[libphonenumber](https://github.com/google/libphonenumber), `libphonenumber`有更多功能, 包括验证号码格式, 格式化, 时区等, 
但基于[xluohome/phonedata](https://github.com/xluohome/phonedata)提供的`dat`数据库能囊括包含虚拟运营商号段的更多号段.  

至于速度, 未做比较, 但本仓库实现已足够快, 选择时建议更多权衡易用性, 功能和数据覆盖范围.

## Benchmark

工程里已内置三种算法, 跑分情况如下:
```
Benchmark                                  Mode  Cnt        Score       Error  Units
BenchmarkRunner.anotherBinarySearchLookup  avgt    5      393.928 ±     8.368  ns/op
BenchmarkRunner.binarySearchLookup         avgt    5      391.755 ±     3.839  ns/op
BenchmarkRunner.sequenceLookup             avgt    5  1504604.426 ± 25029.623  ns/op
```
性能测试源码位于`me.ihxq.projects.pna.benchmark.BenchmarkRunner`, 基于`JMH`

二分查找的平均速度为`391ns/op`, 测试结果因测试机器而异.  

默认使用的也是`me.ihxq.projects.pna.algorithm.BinarySearchAlgorithmImpl`, 
可以通过`new PhoneNumberLookup(new AlgorithmYouLike());`使用其他算法;  

也可自行实现算法, 实现`me.ihxq.projects.pna.algorithm.LookupAlgorithm`即可.

## 感谢
- 感谢[xluohome/phonedata](https://github.com/xluohome/phonedata)共享的数据库
- 也参考了@fengjiajie 的java实现[fengjiajie/phone-number-geo](https://github.com/fengjiajie/phone-number-geo)


## Todo
- [ ] 发布到`maven`仓库
