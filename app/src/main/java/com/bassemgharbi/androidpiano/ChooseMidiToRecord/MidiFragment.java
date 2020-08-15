package com.bassemgharbi.androidpiano.ChooseMidiToRecord;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bassemgharbi.androidpiano.Helper.Constants;
import com.bassemgharbi.androidpiano.R;
import com.bassemgharbi.androidpiano.recording.view.ui.RecordActivity;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MidiFragment extends Fragment implements View.OnClickListener  {
    private View view;
    private int bgRes;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private File[] allFiles ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bgRes = getArguments().getInt("data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ArrayList<FileInSystem> list = new ArrayList<>();
        File folder = new File(Environment.getExternalStorageDirectory().getPath()+"/midi/");
        allFiles = folder.listFiles();
        MidiFile midiFile;
        if (allFiles!=null){
            for (File f : allFiles){
                try {
                    midiFile = new MidiFile(f);
                    list.add(new FileInSystem(f.getName()+"",f.getPath(),midiFile.getTrackCount()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }



        //list.add(new FileInSystem("yo","yo",R.id.person_photo));
        this.view = inflater.inflate(R.layout.fragment_midi, container, false);
        recyclerView = this.view.findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this.view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(list);
        recyclerView.setAdapter(mAdapter);
        this.view.setOnClickListener(this);
        ((Button) this.view.findViewById(R.id.newMidi)).setOnClickListener(this);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.newMidi){

           createSaveDialog();
        }

    }
    @SuppressLint("WrongConstant")
    private void createSaveDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.view.getContext());
        Date date = Calendar.getInstance().getTime();
        final String dateFormat = Constants.getDateFormat(date.getTime());

        // creating view for dialog

        LayoutInflater inflater = (LayoutInflater) this.view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_ui, null);

        TextView chordDuration = (TextView) view.findViewById(R.id.chordDuration);
        chordDuration.setText(Constants.getDurationFormat(20));

        TextView chordDate = (TextView) view.findViewById(R.id.chordDate);
        chordDate.setText(dateFormat);

        final MaterialEditText editText = (MaterialEditText) view.findViewById(R.id.editText);
        editText.setPrimaryColor(
                getResources().getColor(R.color.edit_text_floating_color));

        editText.setMaxCharacters(30);
        editText.setFloatingLabel(1);
        editText.setFloatingLabelText("Record Name");
        editText.setFloatingLabelTextColor(
                getResources().getColor(R.color.edit_text_floating_color));
        editText.setFloatingLabelTextSize(30);

        //hide soft keyboard
        InputMethodManager imm = (InputMethodManager) this.view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);


        // pause timer

        // configure dialog
        builder.setView(view)
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == Dialog.BUTTON_NEGATIVE) {
                                    dialog.dismiss();

                                }
                            }
                        })
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which == Dialog.BUTTON_POSITIVE) {
                                    String name = editText.getText().toString();

                                    if (!name.isEmpty()) {
                                        createMidiFile(name);


                                        dialog.dismiss();
                                    }
                                }
                            }
                        });
        builder.create().show();

    }
    public void createMidiFile(String name){
         MidiTrack tempoTrack = new MidiTrack();
            TimeSignature ts = new TimeSignature();
            ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
            Tempo tempo = new Tempo();
            tempo.setBpm(Tempo.DEFAULT_BPM);
            tempoTrack.insertEvent(ts);
            tempoTrack.insertEvent(tempo);
            List<MidiTrack> tracks = new ArrayList<MidiTrack>();
            tracks.add(tempoTrack);
            MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
            String path= Environment.getExternalStorageDirectory().getPath()+"/midi/"+name+".mid";
            File output = new File(path);
            try
            {
                midi.writeToFile(output);
            }
            catch(IOException e)
            {
                System.err.println(e);
            }
            Intent intent=new Intent(this.view.getContext(), RecordActivity.class);

            intent.putExtra("FilePath",path);
            startActivity(intent);
    }

    private View.OnLongClickListener vLong = new View.OnLongClickListener() {
        public boolean onLongClick(View view) {
            view.showContextMenu();
            return true;
        }
    };




}