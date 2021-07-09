package ru.geekbrains.notes30.ui.update;

import androidx.fragment.app.FragmentManager;

import ru.geekbrains.notes30.R;
import ru.geekbrains.notes30.domain.Note;
import ru.geekbrains.notes30.ui.NoteDetailsFragment;
import ru.geekbrains.notes30.ui.auth.AuthFragment;
import ru.geekbrains.notes30.ui.info.InfoFragment;
import ru.geekbrains.notes30.ui.notes.NotesFragment;

public class MainRouter {

    private final FragmentManager fragmentManager;

    public MainRouter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showNotes() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, NotesFragment.newInstance(), NotesFragment.TAG)
                .commit();
    }

    public void showAuth() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, AuthFragment.newInstance(), AuthFragment.TAG)
                .commit();
    }

    public void showInfo() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, InfoFragment.newInstance(), InfoFragment.TAG)
                .commit();
    }

    public void showNoteDetail(Note note) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, NoteDetailsFragment.newInstance(note), NoteDetailsFragment.TAG)
                .addToBackStack(NoteDetailsFragment.TAG)
                .commit();
    }


    public void showEditNote(Note note) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, UpdateNoteFragment.newInstance(note), UpdateNoteFragment.TAG)
                .addToBackStack(UpdateNoteFragment.TAG)
                .commit();
    }

    public void back() {
        fragmentManager.popBackStack();
    }

}
