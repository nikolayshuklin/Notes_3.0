package ru.geekbrains.notes30.ui.notes;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

import ru.geekbrains.notes30.R;
import ru.geekbrains.notes30.RouterHolder;
import ru.geekbrains.notes30.domain.Callback;
import ru.geekbrains.notes30.domain.Note;
import ru.geekbrains.notes30.domain.NotesFirestoreRepository;
import ru.geekbrains.notes30.domain.NotesRepository;
import ru.geekbrains.notes30.ui.update.MainRouter;
import ru.geekbrains.notes30.ui.update.UpdateNoteFragment;

public class NotesFragment extends Fragment {

    public static final String TAG = "NotesFragment";
    private final NotesRepository repository = NotesFirestoreRepository.INSTANCE;
    private NotesAdapter notesAdapter;

    private int longClickedIndex;
    private Note longClickedNote;

    private boolean isLoading = false;

    private ProgressBar progressBar;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notesAdapter = new NotesAdapter(this);

        isLoading = true;

        repository.getNotes(result -> {
            notesAdapter.setData(result);
            notesAdapter.notifyDataSetChanged();

            isLoading = false;

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });

        notesAdapter.setListener(note -> {

            if (requireActivity() instanceof RouterHolder) {
                MainRouter router = ((RouterHolder) requireActivity()).getMainRouter();

                router.showNoteDetail(note);
            }
//                Snackbar.make(view, note.getTitle(), Snackbar.LENGTH_SHORT).show();
        });

        notesAdapter.setLongClickedListener((note, index) -> {
            longClickedIndex = index;
            longClickedNote = note;
        });

        getParentFragmentManager().setFragmentResultListener(UpdateNoteFragment.UPDATE_RESULT, this, (requestKey, result) -> {
            if (result.containsKey(UpdateNoteFragment.ARG_NOTE)) {
                Note note = result.getParcelable(UpdateNoteFragment.ARG_NOTE);

                notesAdapter.update(note);

                notesAdapter.notifyItemChanged(longClickedIndex);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        RecyclerView notesList = view.findViewById(R.id.notes_list);

        DefaultItemAnimator animator = new DefaultItemAnimator();

        animator.setAddDuration(5000L);
        animator.setRemoveDuration(7000L);

        notesList.setItemAnimator(animator);

        progressBar = view.findViewById(R.id.progress);

        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        }

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add) {

                repository.add("This is new added title", "https://cdn.pixabay.com/photo/2020/04/17/16/48/marguerite-5056063_1280.jpg", new Callback<Note>() {
                    @Override
                    public void onSuccess(Note result) {

                        int index = notesAdapter.add(result);

                        notesAdapter.notifyItemInserted(index);

                        notesList.scrollToPosition(index);
                    }
                });

                return true;
            }

            if (item.getItemId() == R.id.action_clear) {

                repository.clear();

                notesAdapter.setData(Collections.emptyList());

                notesAdapter.notifyDataSetChanged();

                return true;
            }

            return false;
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());

        notesList.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_separator));

        notesList.addItemDecoration(dividerItemDecoration);

        // notesList.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        notesList.setAdapter(notesAdapter);

//        for (Note note : notes) {
//
//            View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_note, containerView, false);
//
//            TextView title = itemView.findViewById(R.id.title);
//            ImageView image = itemView.findViewById(R.id.image);
//
//            title.setText(note.getTitle());
//
//            Glide.with(this)
//                    .load(note.getUrl())
//                    .centerCrop()
//                    .into(image);
//
//            containerView.addView(itemView);
//        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                notesAdapter.setData(new ArrayList<>());
//                notesAdapter.notifyDataSetChanged();
//
//            }
//        }, 2000L);
//
//    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        requireActivity().getMenuInflater().inflate(R.menu.menu_notes_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_update) {

            if (requireActivity() instanceof RouterHolder) {
                MainRouter router = ((RouterHolder) requireActivity()).getMainRouter();

                router.showEditNote(longClickedNote);
            }

            return true;
        }

        if (item.getItemId() == R.id.action_delete) {

            repository.remove(longClickedNote, result -> {
                notesAdapter.remove(longClickedNote);

                notesAdapter.notifyItemRemoved(longClickedIndex);
            });


            return true;
        }

        return super.onContextItemSelected(item);
    }
}
