package com.alex.puiu.binancecryptotool.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ObjectUtilsImpl<T> implements ObjectUtils<T> {

    @Override
    public void writeObjectsToFile(List<T> objects, File file) {
        try(ObjectOutput objectOutput = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(file)
                ))) {
            objects.forEach(object -> this.writeObject(object, objectOutput));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeObject(T obj, ObjectOutput objectOutput) {
        try {
            objectOutput.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> readObjectsFromFile(File file) {
        List<T> objects = new ArrayList<>();
        try(ObjectInput objectInput = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(file)
                ))) {
            while (true) {
                T object = (T) objectInput.readObject();
                objects.add(object);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
