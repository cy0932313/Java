package com.chris.automated.trading.utils;

@FunctionalInterface
public interface Handler<T> {

  void handle(T t);
}
