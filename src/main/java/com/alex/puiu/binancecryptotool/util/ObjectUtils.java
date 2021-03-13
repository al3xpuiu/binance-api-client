package com.alex.puiu.binancecryptotool.util;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public interface ObjectUtils<T> extends Serializable {

    void writeObjectsToFile(List<T> objects, File file);
    List<T> readObjectsFromFile(File file);
}
