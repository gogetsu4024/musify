package com.bassemgharbi.androidpiano;

import com.leff.midi.event.MidiEvent;

public class EventPrinter implements com.leff.midi.util.MidiEventListener {

    private String mLabel;

    public EventPrinter(String label)
    {
        mLabel = label;
    }

    @Override
    public void onStart(boolean fromBeginning)
    {
        if(fromBeginning)
        {
            System.out.println(mLabel + " Started!");
        }
        else
        {
            System.out.println(mLabel + " resumed");
        }
    }

    @Override
    public void onEvent(MidiEvent event, long ms)
    {
        System.out.println(mLabel + " received event: " + event);
    }

    @Override
    public void onStop(boolean finished)
    {
        if(finished)
        {
            System.out.println(mLabel + " Finished!");
        }
        else
        {
            System.out.println(mLabel + " paused");
        }
    }
}
