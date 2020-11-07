package com.learning.libnetwork;

import java.lang.reflect.Type;

public interface Convert<T> {
    T convert(String content , Type type);
}
