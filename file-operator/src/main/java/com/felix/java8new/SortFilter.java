package com.felix.java8new;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SortFilter {

    public static void main(String[] args) {

        String str = "abcdedafdsadf";
        List<String> list = new ArrayList<>();

        for (char c : str.toCharArray()) {
            list.add(String.valueOf(c));
        }

        List newList = list.stream().distinct().sorted().collect(Collectors.toList());
        System.out.println(String.join("", newList));

    }

}
