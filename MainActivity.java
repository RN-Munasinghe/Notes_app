package com.example.notespro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNote;
    private Button buttonSave;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private ArrayList<String> noteList;
    private int editNotePosition = -1; // To track the note being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNote = findViewById(R.id.noteEditText);
        buttonSave = findViewById(R.id.saveButton);
        recyclerView = findViewById(R.id.recyclerView);

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editTextNote.getText().toString();
                if (!TextUtils.isEmpty(note)) {
                    if (editNotePosition == -1) {
                        // Add new note
                        noteList.add(note);
                        noteAdapter.notifyItemInserted(noteList.size() - 1);
                    } else {
                        // Update existing note
                        noteList.set(editNotePosition, note);
                        noteAdapter.notifyItemChanged(editNotePosition);
                        editNotePosition = -1;
                    }
                    editTextNote.setText("");
                }
            }
        });
    }

    class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

        private ArrayList<String> notes;

        NoteAdapter(ArrayList<String> notes) {
            this.notes = notes;
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.activity_item_note, parent, false);
            return new NoteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            String note = notes.get(position);
            holder.textViewNote.setText(note);
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        class NoteViewHolder extends RecyclerView.ViewHolder {
            TextView textViewNote;
            Button buttonDelete;
            Button buttonUpdate;

            NoteViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewNote = itemView.findViewById(R.id.textViewNote);
                buttonDelete = itemView.findViewById(R.id.buttonDelete);
                buttonUpdate = itemView.findViewById(R.id.buttonUpdate);

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        notes.remove(position);
                        notifyItemRemoved(position);
                    }
                });

                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        editTextNote.setText(notes.get(position));
                        editNotePosition = position; // Set the edit position
                    }
                });
            }
        }
    }
}
