package ru.geekbrains.notes30.domain;

public interface Callback<T> {

    void onSuccess(T result);
}
