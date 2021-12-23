package com.dnydys.stream.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname Person
 * @Description TODO
 * @Date 2021/12/22 22:35
 * @Created by hasee
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private String firstName;
    private String lastName;
    private String job;
    private String gender;
    private int salary;
    private int age;
}
