package com.bassemgharbi.androidpiano.improv.services.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.bassemgharbi.androidpiano.improv.services.model.Instrument;

import java.util.ArrayList;
import java.util.List;

public class InstrumentChooserRepository {
    static final ArrayList<Instrument> instruments = new ArrayList<>();
    private static InstrumentChooserRepository single_instance = null;
    private Context context;
    public static InstrumentChooserRepository getInstance(){
        if (single_instance ==null){
            single_instance = new InstrumentChooserRepository();
        }
        return single_instance;
    }

    public MutableLiveData<List<Instrument>> getInstruments() {
        setInstruments();
        final MutableLiveData<List<Instrument>> data =
                new MutableLiveData<>();
        data.setValue(instruments);
        return data;
    }
    private void setInstruments(){
        /*
        instruments.add(new Instrument("Acoustic Grand Piano","Piano","Piano",0));
        instruments.add(new Instrument("Violin","violin","Violin",40));
        instruments.add(new Instrument("French Horn","frenchhorn","Brass",60));
        instruments.add(new Instrument("Flute","flute","flute",73));
        instruments.add(new Instrument("Bright Acoustic Piano","Piano","Piano",1));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (clean)","electricguitar","Guitar",27));*/
        instruments.add(new Instrument("Acoustic Grand Piano","piano","Piano",0));
        instruments.add(new Instrument("Bright Acoustic Piano","piano","Piano",1));
        instruments.add(new Instrument("Electric Grand Piano","piano","Piano",2));
        instruments.add(new Instrument("Honky-tonk Piano","piano","Piano",3));
        instruments.add(new Instrument("Electric Piano 1","piano","Piano",4));
        instruments.add(new Instrument("Electric Piano 2","piano","Piano",5));
        instruments.add(new Instrument("Harpsichord","piano","Piano",6));
        instruments.add(new Instrument("Clavinet","piano","Piano",7));
        instruments.add(new Instrument("Celesta","chromatic_percussion","chromatic_percussion",8));
        instruments.add(new Instrument("Glockenspiel","chromatic_percussion","chromatic_percussion",9));
        instruments.add(new Instrument("Music Box","chromatic_percussion","chromatic_percussion",10));
        instruments.add(new Instrument("Vibraphone","chromatic_percussion","chromatic_percussion",11));
        instruments.add(new Instrument("Marimba","chromatic_percussion","chromatic_percussion",12));
        instruments.add(new Instrument("Xylophone","chromatic_percussion","chromatic_percussion",13));
        instruments.add(new Instrument("Tubular Bells","chromatic_percussion","chromatic_percussion",14));
        instruments.add(new Instrument("Dulcimer","chromatic_percussion","chromatic_percussion",15));
        instruments.add(new Instrument("Drawbar Organ","organ","Organ",16));
        instruments.add(new Instrument("Percussive Organ","organ","Organ",17));
        instruments.add(new Instrument("Rock Organ","organ","Organ",18));
        instruments.add(new Instrument("Church Organ","organ","Organ",19));
        instruments.add(new Instrument("Reed Organ","organ","Organ",20));
        instruments.add(new Instrument("Accordion","organ","Organ",21));
        instruments.add(new Instrument("Harmonica","organ","Organ",22));
        instruments.add(new Instrument("Tango Accordion","organ","Organ",23));
        instruments.add(new Instrument("Acoustic Guitar (nylon)","guitar","Guitar",24));
        instruments.add(new Instrument("Acoustic Guitar (steel)","guitar","Guitar",25));
        instruments.add(new Instrument("Electric Guitar (jazz)","guitar","Guitar",26));
        instruments.add(new Instrument("Electric Guitar (clean)","guitar","Guitar",27));
        instruments.add(new Instrument("Electric Guitar (muted)","guitar","Guitar",28));
        instruments.add(new Instrument("Overdriven Guitar","guitar","Guitar",29));
        instruments.add(new Instrument("Distortion Guitar","guitar","Guitar",30));
        instruments.add(new Instrument("Guitar Harmonics","guitar","Guitar",31));
        instruments.add(new Instrument("Acoustic Bass","bass","Bass",32));
        instruments.add(new Instrument("Electric Bass (finger)","bass","Bass",33));
        instruments.add(new Instrument("Electric Bass (pick)","bass","Bass",34));
        instruments.add(new Instrument("Fretless Bass","bass","Bass",35));
        instruments.add(new Instrument("Slap Bass 1","bass","Bass",36));
        instruments.add(new Instrument("Slap Bass 2","bass","Bass",37));
        instruments.add(new Instrument("Synth Bass 1","bass","Bass",38));
        instruments.add(new Instrument("Synth Bass 2","bass","Bass",39));
        instruments.add(new Instrument("Violin","strings","Strings",40));
        instruments.add(new Instrument("Viola","strings","Strings",41));
        instruments.add(new Instrument("Cello","strings","Strings",42));
        instruments.add(new Instrument("Contrabass","strings","Strings",43));
        instruments.add(new Instrument("Tremolo Strings","strings","Strings",44));
        instruments.add(new Instrument("Pizzicato Strings","strings","Strings",45));
        instruments.add(new Instrument("Orchestral Harp","strings","Strings",46));
        instruments.add(new Instrument("Timpani","strings","Strings",47));
        instruments.add(new Instrument("String Ensemble 1","ensemble","Ensemble",48));
        instruments.add(new Instrument("String Ensemble 2","ensemble","Ensemble",49));
        instruments.add(new Instrument("Synth Strings 1","ensemble","Ensemble",50));
        instruments.add(new Instrument("Synth Strings 2","ensemble","Ensemble",51));
        instruments.add(new Instrument("Choir Aahs","ensemble","Ensemble",52));
        instruments.add(new Instrument("Voice Oohs","ensemble","Ensemble",53));
        instruments.add(new Instrument("Synth Choir","ensemble","Ensemble",54));
        instruments.add(new Instrument("Orchestra Hit","ensemble","Ensemble",55));
        instruments.add(new Instrument("Trumpet","brass","Brass",56));
        instruments.add(new Instrument("Trombone","brass","Brass",57));
        instruments.add(new Instrument("Tuba","brass","Brass",58));
        instruments.add(new Instrument("Muted Trumpet","brass","Brass",59));
        instruments.add(new Instrument("French Horn","brass","Brass",60));
        instruments.add(new Instrument("Brass Section","brass","Brass",61));
        instruments.add(new Instrument("Synth Brass 1","brass","Brass",62));
        instruments.add(new Instrument("Synth Brass 2","brass","Brass",63));
        instruments.add(new Instrument("Soprano Sax","reed","Reed",64));
        instruments.add(new Instrument("Alto Sax","reed","Reed",65));
        instruments.add(new Instrument("Tenor Sax","reed","Reed",66));
        instruments.add(new Instrument("Baritone Sax","reed","Reed",67));
        instruments.add(new Instrument("Oboe","reed","Reed",68));
        instruments.add(new Instrument("English Horn","reed","Reed",69));
        instruments.add(new Instrument("Bassoon","reed","Reed",70));
        instruments.add(new Instrument("Clarinet","reed","Reed",71));
        instruments.add(new Instrument("Piccolo","pipe","Pipe",72));
        instruments.add(new Instrument("Flute","pipe","Pipe",73));
        instruments.add(new Instrument("Recorder","pipe","Pipe",74));
        instruments.add(new Instrument("Pan Flute","pipe","Pipe",75));
        instruments.add(new Instrument("Blown bottle","pipe","Pipe",76));
        instruments.add(new Instrument("Shakuhachi","pipe","Pipe",77));
        instruments.add(new Instrument("Whistle","pipe","Pipe",78));
        instruments.add(new Instrument("Ocarina","pipe","Pipe",79));
        instruments.add(new Instrument("Lead 1 (square)","synth_lead","synth_lead",80));
        instruments.add(new Instrument("Lead 2 (sawtooth)","synth_lead","synth_lead",81));
        instruments.add(new Instrument("Lead 3 (calliope)","synth_lead","synth_lead",82));
        instruments.add(new Instrument("Lead 4 (chiff)","synth_lead","synth_lead",83));
        instruments.add(new Instrument("Lead 5 (charang)","synth_lead","synth_lead",84));
        instruments.add(new Instrument("Lead 6 (voice)","synth_lead","synth_lead",85));
        instruments.add(new Instrument("Lead 7 (fifths)","synth_lead","synth_lead",86));
        instruments.add(new Instrument("Lead 8 (bass + lead)","synth_lead","synth_lead",87));
        instruments.add(new Instrument("Pad 1 (new age)","synth_pad","synth_pad",88));
        instruments.add(new Instrument("Pad 2 (warm)","synth_pad","synth_pad",89));
        instruments.add(new Instrument("Pad 3 (polysynth)","synth_pad","synth_pad",90));
        instruments.add(new Instrument("Pad 4 (choir)","synth_pad","synth_pad",91));
        instruments.add(new Instrument("Pad 5 (bowed)","synth_pad","synth_pad",92));
        instruments.add(new Instrument("Pad 6 (metallic)","synth_pad","synth_pad",93));
        instruments.add(new Instrument("Pad 7 (halo)","synth_pad","synth_pad",94));
        instruments.add(new Instrument("Pad 8 (sweep)","synth_pad","synth_pad",95));
        instruments.add(new Instrument("FX 1 (rain)","synth_effects","synth_effects",96));
        instruments.add(new Instrument("FX 2 (soundtrack)","synth_effects","synth_effects",97));
        instruments.add(new Instrument("FX 3 (crystal)","synth_effects","synth_effects",98));
        instruments.add(new Instrument("FX 4 (atmosphere)","synth_effects","synth_effects",99));
        instruments.add(new Instrument("FX 5 (brightness)","synth_effects","synth_effects",100));
        instruments.add(new Instrument("FX 6 (goblins)","synth_effects","synth_effects",101));
        instruments.add(new Instrument("FX 7 (echoes)","synth_effects","synth_effects",102));
        instruments.add(new Instrument("FX 8 (sci-fi)","synth_effects","synth_effects",103));
        instruments.add(new Instrument("Sitar","ethnic","Ethnic",104));
        instruments.add(new Instrument("Banjo","ethnic","Ethnic",105));
        instruments.add(new Instrument("Shamisen","ethnic","Ethnic",106));
        instruments.add(new Instrument("Koto","ethnic","Ethnic",107));
        instruments.add(new Instrument("Kalimba","ethnic","Ethnic",108));
        instruments.add(new Instrument("Bagpipe","ethnic","Ethnic",109));
        instruments.add(new Instrument("Fiddle","ethnic","Ethnic",110));
        instruments.add(new Instrument("Shanai","ethnic","Ethnic",111));
        instruments.add(new Instrument("Tinkle Bell","percussive","Percussive",112));
        instruments.add(new Instrument("Agogo","percussive","Percussive",113));
        instruments.add(new Instrument("Steel Drums","percussive","Percussive",114));
        instruments.add(new Instrument("Woodblock","percussive","Percussive",115));
        instruments.add(new Instrument("Taiko Drum","percussive","Percussive",116));
        instruments.add(new Instrument("Melodic Tom","percussive","Percussive",117));
        instruments.add(new Instrument("Synth Drum","percussive","Percussive",118));
        instruments.add(new Instrument("Reverse Cymbal","percussive","Percussive",119));
        instruments.add(new Instrument("Guitar Fret Noise","sound_effects","sound_effects",120));
        instruments.add(new Instrument("Breath Noise","sound_effects","sound_effects",121));
        instruments.add(new Instrument("Seashore","sound_effects","sound_effects",122));
        instruments.add(new Instrument("Bird Tweet","sound_effects","sound_effects",123));
        instruments.add(new Instrument("Telephone Ring","sound_effects","sound_effects",124));
        instruments.add(new Instrument("Helicopter","sound_effects","sound_effects",125));
        instruments.add(new Instrument("Applause","sound_effects","sound_effects",126));
        instruments.add(new Instrument("Gunshot","sound_effects","sound_effects",127));
    }
}
