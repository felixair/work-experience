package com.felix.proxy.dynamic;

public class Business implements IBusiness, IBusiness2 {
    @Override
    public void doSomeThing() {
        System.out.println("do some thing!");
    }

    @Override
    public void doSomeThing2() {
        System.out.println("do some thing 2!");
    }
}
