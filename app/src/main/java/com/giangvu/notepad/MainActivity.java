package com.giangvu.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView listNote;
    NoteListAdapter adapter = null;
    NoteItem publicNote;
    ConnectDOM dom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = this.getFilesDir().getAbsolutePath() + "/note.xml";
        dom = new ConnectDOM(path);

        listNote = (ListView) findViewById(R.id.noteList);
        adapter = new NoteListAdapter(MainActivity.this, R.layout.note_item_list, dom.ReadByDOM());
        if (adapter.list == null)
            listNote.setAdapter(null);
        else listNote.setAdapter(adapter);

        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMenuItem(view, adapter.list.get(position));
            }
        });
        registerForContextMenu(listNote);
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuNew:
                Intent intent = new Intent(MainActivity.this, NotepadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("store", "create");
                intent.putExtra("data", bundle);
                startActivity(intent);
                break;
            case R.id.menuAboutUs:
                OpenDialogAbout();
                break;
            case R.id.menuInstruction:
                OpenDialogInstruction();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMenuItem(View view, NoteItem note) {
        this.publicNote = note;
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuOpen:
                        Intent intent = new Intent(MainActivity.this, NotepadActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("store", "save");
                        bundle.putSerializable("note", publicNote);
                        intent.putExtra("data", bundle);
                        startActivity(intent);
                        break;
                    case R.id.menuDelete:
                        OpenDialogDelete(publicNote);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    //Dialog
    public void OpenDialogDelete(NoteItem note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete " + note.getTitle());
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dom.DeleteNoteByDOM(publicNote);
                adapter.remove(publicNote);
                Toast.makeText(getApplicationContext(), "Note: '" + publicNote.getTitle() + "' has been deleted!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void OpenDialogAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About us");
        builder.setMessage("Team 12:\n" +
                "3117410075 Đỗ Trung Hiếu\n" +
                "3117410058 Vũ Trường Giang\n" +
                "3117410298 Triệu Nguyễn Quốc Việt\n" +
                "3117410312 Ôn Tuấn Huy");
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void OpenDialogInstruction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions for use");
        builder.setMessage("-Press icon 'plus' at the top right corner of screen to create new note.\n" +
                "-Click button 'Create' to create a new note with title is at the top of app.\n" +
                "-Press 3 dots on the top right, you can choose a few options.\n" +
                "  * Instruction show how to use this app.\n" +
                "  * About us is our informations.\n" +
                "-Click button 'Save' to change context of this note.\n" +
                "-Click button 'Back to list' to turn back to list of notes.");
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
