package ru.geekbrains.notes30.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public interface NotesRepository {

    void getNotes(Callback<List<Note>> callback);

    void clear();

    void add(String title, String imageUrl, Callback<Note> callback);

    void remove(Note note, Callback<Object> callback);

    Note update(@NonNull Note note, @Nullable String title, @Nullable Date date);
}
