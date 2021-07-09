package ru.geekbrains.notes30.domain;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotesRepositoryImpl implements NotesRepository {

    public static final NotesRepository INSTANCE = new NotesRepositoryImpl();

    private final ArrayList<Note> notes = new ArrayList<>();

    private ExecutorService executor = Executors.newCachedThreadPool();

    private Handler handler = new Handler(Looper.getMainLooper());

    public NotesRepositoryImpl() {

        notes.add(new Note("id1", "Title Number One", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg", new Date()));
        notes.add(new Note("id2", "Title Number Two", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg", new Date()));
        notes.add(new Note("id3", "Title Number Three", "https://cdn.pixabay.com/photo/2020/07/06/01/33/sky-5375005_1280.jpg", new Date()));
        notes.add(new Note("id4", "Title Number Four", "https://cdn.pixabay.com/photo/2021/03/17/09/06/snowdrop-6101818_1280.jpg", new Date()));
        notes.add(new Note("id5", "Title Number Five", "https://cdn.pixabay.com/photo/2020/06/22/10/40/castle-5328719_1280.jpg", new Date()));
        notes.add(new Note("id6", "Title Number Six", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg", new Date()));
        notes.add(new Note("id7", "Title Number Seven", "https://cdn.pixabay.com/photo/2020/07/06/01/33/sky-5375005_1280.jpg", new Date()));
        notes.add(new Note("id8", "Title Number Eight", "https://cdn.pixabay.com/photo/2020/06/22/10/40/castle-5328719_1280.jpg", new Date()));
        notes.add(new Note("id9", "Title Number Nine", "https://cdn.pixabay.com/photo/2021/03/17/09/06/snowdrop-6101818_1280.jpg", new Date()));
        notes.add(new Note("id10", "Title Number Ten", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg", new Date()));
        notes.add(new Note("id11", "Title Number Eleven", "https://cdn.pixabay.com/photo/2020/12/25/11/57/flamingos-5859192_1280.jpg", new Date()));
        notes.add(new Note("id12", "Title Number Twelve", "https://cdn.pixabay.com/photo/2020/07/06/01/33/sky-5375005_1280.jpg", new Date()));

    }

    @Override
    public void getNotes(Callback<List<Note>> callback) {
        executor.execute(() -> {

            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(() -> callback.onSuccess(notes));
        });
    }

    @Override
    public void clear() {
        notes.clear();
    }

    @Override
    public void add(String title, String imageUrl, Callback<Note> callback) {

        Note note = new Note(UUID.randomUUID().toString(), title, imageUrl, new Date());
        notes.add(note);

        callback.onSuccess(note);

    }

    @Override
    public void remove(Note note, Callback<Object> callback) {
        notes.remove(note);

        callback.onSuccess(note);
    }

    @Override
    public Note update(@NonNull Note note, @Nullable String title, @Nullable Date date) {

        for (int i = 0; i < notes.size(); i++) {

            Note item = notes.get(i);

            if (item.getId().equals(note.getId())) {

                String titleToSet = item.getTitle();
                Date dateToSet = item.getDate();


                if (title != null) {
                    titleToSet = title;
                }

                if (date != null) {
                    dateToSet = date;
                }

                Note newNote = new Note(note.getId(), titleToSet, note.getUrl(), dateToSet);

                notes.remove(i);
                notes.add(i, newNote);

                return newNote;
            }
        }

        return note;
    }

}
