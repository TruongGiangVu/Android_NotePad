package com.giangvu.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NotepadActivity extends AppCompatActivity {

    EditText edtContent, edtTitle;
    TextView txtId;
    Button btnReturn, btnStore;
    NoteItem item;
    ConnectDOM dom;
    String storeType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        String path = this.getFilesDir().getAbsolutePath() + "/note.xml";
        dom = new ConnectDOM(path);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        item = (NoteItem) bundle.getSerializable("note");
        storeType = bundle.getString("store");
        Mapping();
        if (item != null) {
            txtId.setText(item.getId() + "");
            edtTitle.setText(item.getTitle());
            edtContent.setText(item.getContent());
        }
    }

    private void Mapping() {
        txtId = (TextView) findViewById(R.id.txtNoteId);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContent = (EditText) findViewById(R.id.edtContent);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnStore = (Button) findViewById(R.id.btnStore);
        if (storeType.equals("save")) {
            btnStore.setText(R.string.notepad_btnSave);
        }
        if (storeType.equals("create"))
            btnStore.setText(R.string.notepad_btnCreate);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotepadActivity.this, MainActivity.class));
            }
        });
        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteItem note = getNote();
                if (note.getTitle().equals("")) {
                    Toast.makeText(getApplicationContext(), "Your title is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    if (storeType.equals("create")) {
                        // new id
                        NoteListAdapter adapter = new NoteListAdapter(getApplicationContext(), 0, dom.ReadByDOM());
                        note.setId(adapter.newId());
                        dom.CreateNewByDOM(note);
                        Toast.makeText(getApplicationContext(), "Create " + note.getTitle() + " success!", Toast.LENGTH_SHORT).show();
                    }
                    if (storeType.equals("save")) {
                        note.setId(Integer.parseInt(txtId.getText().toString()));
                        dom.SaveByDOM(note);
                        Toast.makeText(getApplicationContext(), "Change content of " + note.getTitle() + " success! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private NoteItem getNote() {
        NoteItem note = new NoteItem();
        note.setTitle(edtTitle.getText().toString().trim());
        note.setContent(edtContent.getText().toString().trim());
        return note;
    }
}
