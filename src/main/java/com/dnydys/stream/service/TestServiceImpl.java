package com.dnydys.stream.service;

import com.dnydys.stream.entity.Person;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Classname TestServiceImpl
 * @Description TODO
 * @Date 2021/12/22 22:38
 * @Created by hasee
 */
public class TestServiceImpl{

    public static void main(String[] args) {
        testFilter();
        testLimitAndSkip();
        testDistinct();
        testDistinctAttribute();
        testStored();
        testMatchAndFind();
        testStatute();
        testCollector();
    }


     static List<Person> list = new ArrayList<Person>() {
        {
            add(new Person("1Elsdon", "Jaycob", "Java programmer", "male", 2000, 18));
            add(new Person("2Tamsen", "Brittany", "Java programmer", "female", 2371, 55));
            add(new Person("3Floyd", "Donny", "Java programmer", "male", 3322, 25));
            add(new Person("4Sindy", "Jonie", "Java programmer", "female", 35020, 15));
            add(new Person("5Vere", "Hervey", "Java programmer", "male", 2272, 25));
            add(new Person("6Maude", "Jaimie", "Java programmer", "female", 2057, 87));
            add(new Person("7Shawn", "Randall", "Java programmer", "male", 3120, 99));
            add(new Person("8Jayden", "Corrina", "Java programmer", "female", 345, 25));
            add(new Person("9Palmer", "Dene", "Java programmer", "male", 3375, 14));
            add(new Person("10Addison", "Pam", "Java programmer", "female", 3429, 20));
            add(new Person("10Addison", "Pam", "Java programmer", "female", 3429, 20));
            add(new Person("10Addison", "Pam", "Java programmer", "female", 3429, 20));
            // add(new Person("Addison", "Pam", "Java programmer", null, 3422, 20));
        }
    };
    public static void createStream() {

        // 通过集合创建Stream流------> default Stream<E> stream：返回一个顺序流
        Stream<Person> stream = list.stream();

        // 通过集合创建Stream流------> default Stream<E> parallelStream：返回一个并行流
        Stream<Person> parallelStream = list.parallelStream();

        // 通过数组创建Stream流-------> 调用Arrays类的static <T> Stream<T>  Stream<T[] array> ：返回一个流
        Person[] arrPerson = new Person[]{list.get(0), list.get(1)};
        Stream<Person> streamObj = Arrays.stream(arrPerson);

        // 通过of创建Stream
        Stream<Integer> streamOf = Stream.of(1, 2, 3, 4, 5, 6);

        Stream<Person> streamOfs = Stream.of(list.get(0), list.get(1));

        //  通过无限流的方式创建Stream流
        /* 迭代--->*public static<T> Stream<T> iterate(final T seed,final UnaruOperator<T> f)
         * 遍历前10个偶数
         * 无限流，无限反复操作使用，所有一般都会配合limit使用
         */
        Stream.iterate(0, t -> t + 2).limit(10).forEach(System.out::println);  //seed是起始数值，limit代表循环前10次

        // 生成----》public static<T> Stream<T> generate(Supplier<T> s)
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }
    public static void testFilter(){
            //`filter`(Predicate p)–接受Lambda，从流中排除某些元素
            //原生方式  所有的中间操作不会做任何的处理
            Stream<Person> stream1 = list.stream()
                .filter((e) ->{
                        return e.getAge() < 20;
                });
            stream1.forEach(System.out::print);

            //使用lambdas表达式优化
            list.stream().filter(
                e->{
                    return e.getAge() < 20;
                }
            ).forEach(System.out::println);
    }

    public static void testLimitAndSkip(){
        //   `limit(n)`–截断流，使其元素不超过给定数量  类似于mysql中的limit，但这里只有最大值
        //  `skip(n)`–跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流。与limit(n)互补

        //通过无限流创建一个无限流，如果不使用Limit方法进行限制阶段，无限流将会形成一个类似于死循环的操作
        //当使用skip（）将会跳过前5个数据从第6个参数返回
        Stream.iterate(0,t->t+1)
            .limit(10).skip(5)
            .forEach(System.out::println);  //seed是起始数值，limit代表循环前10次
    }

    /**
     * @className TestServiceImpl
     * @author dnydys
     * @description 测试去重
     * @updateTime 2021/12/22 23:32
     * @return: void
     * @version 1.0
     */
    public static void testDistinct(){
        //distinct–筛选，通过流所生成的元素的 hashCode() 和 equals() 去除重复元素
        //distinct取出集合中对象信息完全重复的对象
        list.stream()
            .distinct()
            .forEach(System.out::println);


    }

    //distinct（）不提供按照属性对对象列表进行去重的直接实现
    //它是基于hashCode（）和equals（）工作的。
    // 如果想要按照对象的属性，对对象列表进行去重，我们可以通过其它方法来实现
    public static <T> Predicate<T> distinctByKey(Function<? super T,?> keyExtractor){
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) ==null;
    }

    /**
     * @className TestServiceImpl
     * @author dnydys
     * @description distinct对集合中对象的某一属性进行去重
     * @updateTime 2021/12/23 0:00
     * @return: void
     * @version 1.0
     */
    public static void testDistinctAttribute(){
        //distinct对集合中对象的某一属性进行去重
        list.stream()
            .filter(
                distinctByKey(e->e.getAge())
            ).distinct()
            .forEach(System.out::println);
    }


    /**
     * @className TestServiceImpl
     * @author dnydys
     * @description 排序
     * @updateTime 2021/12/23 0:38
     * @return: void
     * @version 1.0
     */
    public static void testStored(){
        List<Integer> list1 = Arrays.asList(12, 43, 65, 3, 4, 0,01, -98);
//        list1.stream().sorted().forEach(System.out::println);

        //按年龄排序
//        list.stream().sorted((e1,e2)
//                    ->(Integer.compare(e1.getAge(), e2.getAge()))
//                )
//            .forEach(System.out::println);
        //按FirstName排序
        list.stream().sorted((e1,e2)
                    ->(e1.getFirstName().compareTo(e2.getFirstName()))
                )
            .forEach(System.out::println);
    }

    public static void testMatchAndFind(){
        /**
         * 匹配与查找
         *     allMatch(Predicate p)：检查是否匹配所有元素
         *     anyMatch(Predicate p)：检查是否至少匹配一个元素
         *     noneMatch(Predicate p)：检查是否没有匹配的元素
         *     findFirst()：返回第一个元素
         *     findAny()：返回当前流中的任意元素
         *     count()：返回流中元素的总数
         *     max(Comparator c)：返回流中最大值
         *     min(Comparator c)：返回流中最小值
         *     forEach(Consumer c)：内部迭代
         **/

        //allMatch()相当于制定一个规则，将集合中的对象一一与规则进行对象，判断是否所有的集合对象都符合该规则, true/false
        boolean allMatch = list.stream().allMatch(e -> e.getAge()>18);
        System.out.println("测试Stream流的终止操作----allMatch()--->"+allMatch);

        //anyMatch(Predicate p)：检查是否至少匹配一个元素  类似于多选一即可
        boolean anyMatch = list.stream().anyMatch(e -> e.getSalary()>1000);
        System.out.println("检查是否至少匹配一个元素----anyMatch()--->"+anyMatch);

        //noneMatch(Predicate p)：检查是否没有匹配的元素 没有返回true，反之false
        boolean noneMatch = list.stream().noneMatch(e -> e.getSalary() > 10000);
        System.out.println("检查是否没有匹配的元素----noneMatch()--->"+noneMatch);

        //findFirst()：返回第一个元素
        Person person = list.stream().findFirst().get();
        System.out.println("返回第一个元素----findFirst()--->"+person.toString());

        for (int i = 0; i <100 ; i++) {
            // findAny()：返回当前流中的任意元素
            Optional<Person> AnyPerson = list.stream().findAny();
            System.out.println("返回当前流中的任意元素----findAny()--->"+AnyPerson.toString()+"--------->"+i);
        }

        //count()：返回流中元素的总数
        long count = list.stream().count();
        System.out.println("返回流中元素的总数----count()--->"+count);

        //max(Comparator c)：返回流中最大值   Optional<T> max(Comparator<? super T> comparator);
        Person maxPersonSalary = list.stream().max(Comparator.comparing(Person::getSalary)).get();
        System.out.println("返回流中最大值----max()--->"+maxPersonSalary);

        //min(Comparator c)：返回流中最小值
        Person minPersonSalary = list.stream().min(Comparator.comparing(Person::getSalary)).get();
        System.out.println("返回流中最小值----max()--->"+minPersonSalary);
    }


    public static void testStatute(){
        /*
         * **规约**
         *     reduce(T identity,BinaryOperator accumulator)：可以将流中元素反复结合起来，得到一个值。返回T
         *     reduce(BinaryOperator accumulator)：可以将流中元素反复结合起来，得到一个值。返回Optional
         */

        //reduce(BinaryOperator accumulator)：可以将流中元素反复结合起来，得到一个值。返回Optional
        Integer integers = list.stream().map(Person::getSalary).reduce((e1, e2) -> e1 + e2).get();//lambda表达式
        Integer integer = list.stream().map(Person::getSalary).reduce(Integer::sum).get();//使用引用
        System.out.println("工资总和-------lambda表达式------>"+integers);
        System.out.println("工资总和-------使用引用------>"+integer);

    }

    public static void testCollector(){
        /*
        * **收集**
        *     collect(Collector c)：将流转换成其他形式。接受一个Collector接口的实现，用于给Stream中元素做汇总的方法
        *       Collector接口中方法的实现决定了如何对流执行收集的操作（如收集到List、Set、Map)。
        *       Collector需要使用Collectors提供实例。另外， Collectors实用类提供了很多静态方法，可以方便地创建常见收集器实例，具体方法与实例如下：
                    toList：返回类型List< T>，作用是把流中元素收集到List
                    toSet：返回类型Set< T>，作用是把流中元素收集到Set
                    toCollection：返回类型Collection< T>，作用是把流中元素收集到创建的集合
        */

        //collect(Collector c)：将流转换成其他形式。返回一个set
        System.out.println("将流转换成其他形式。返回一个set-------toSet()------>:");
        list.stream().filter(e -> e.getSalary() > 3000).collect(Collectors.toSet()).forEach(System.out::println);

        //collect(Collector c)：将流转换成其他形式。返回一个list
        System.out.println("将流转换成其他形式。返回一个list-------toList()------>:");
        list.stream().filter(e -> e.getSalary() > 3000).limit(2).collect(Collectors.toList()).forEach(System.out::println);

        //collect(Collector c)：将流转换成其他形式。返回一个map
        System.out.println("将流转换成其他形式。返回一个map-------toMap()------>:");
        list.stream().filter(e -> !e.getFirstName().equals("测试"))
            // 注意：key不能重复  toMap()参数一：key   参数二：value   参数三：对key值进行去重，当有重复的key，map中保留第一条重复数据
            .collect(Collectors.toMap(Person::getAge,person -> person,(key1, key2) -> key1))  //value 为对象 student -> student jdk1.8返回当前对象,也可以为对象的属性
            .forEach((key, value) -> System.out.println("key--"+key+"   value--"+value.toString()));


    }


}
