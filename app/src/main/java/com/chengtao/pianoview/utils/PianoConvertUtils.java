package com.chengtao.pianoview.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/*
 * 钢琴转换工具类
 *
 * @author ChengTao <a href="mailto:tao@paradisehell.org">Contact me.</a>
 */

public class PianoConvertUtils {
  private static final int STANDARD_DO_GROUP = 3;
  private static final int STANDARD_DO_POSITION = 0;
  private static final long STANDARD_FREQUENCY = 240L;
  private static final String NUMBER_REGEX = "^\\d+$";
  private static final String MUSIC_NUMBER_REGEX = "^[0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$";
  private static final String MUSIC_HML_NUMBER_REGEX = "^[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$";
  private static final String MUSIC_HO_NUMBER_LO_NUMBER_REGEX = "^HO[0-7](\\*(0\\.25|0\\.5|2|4))?$|^LO[0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$";
  private static final String MUSIC_HO_HML_NUMBER_LO_HML_NUMBER_REGEX = "^HO[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4))?$|^LO[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$";
  private static final HashSet<Integer> HIGH_BLACK = new HashSet(Arrays.asList(1, 2, 4, 5, 6));
  private static final HashSet<Integer> LOW_BLACK = new HashSet(Arrays.asList(7, 4, 5, 3, 2));

  public PianoConvertUtils() {
  }

  public static Object[] convertByFilePath(String configFilePath) throws Throwable {
    File file = new File(configFilePath);
    if (file.exists()) {
      try {
        FileInputStream fis = new FileInputStream(file);
        return convertByInputStream(fis);
      } catch (FileNotFoundException var3) {
        throw new Exception("file not exist");
      }
    } else {
      throw new Exception("file not exist");
    }
  }

  public static Object[] convertByInputStream(InputStream is) throws Throwable {
    if (is != null) {
      StringBuilder stringBuilder = new StringBuilder();

      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String line;
        while((line = reader.readLine()) != null) {
          stringBuilder.append(line);
        }

        convertByConfigString(stringBuilder.toString());
        return convertByConfigString(stringBuilder.toString());
      } catch (IOException var4) {
        throw new Exception("read file exception");
      }
    } else {
      throw new Exception("read file exception");
    }
  }

  public static Object[] convertByConfigString(String configString) throws Throwable {
    if (configString != null && !configString.equals("") && configString.indexOf("{") == 0 && configString.contains("}")) {
      StringBuilder stringBuilder = new StringBuilder();
      boolean nameStart = false;
      boolean nameEnd = false;
      char[] var4 = configString.toCharArray();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
        char c = var4[var6];
        if (nameStart && !nameEnd) {
          stringBuilder.append(c);
        } else if (!Character.isWhitespace(c)) {
          stringBuilder.append(c);
        }

        if (!nameStart && c == ':') {
          int length = stringBuilder.length();
          if (length >= 5) {
            String name = stringBuilder.substring(length - 5, length - 1);
            if (name.equals("name")) {
              nameStart = true;
            }
          }
        }

        if (nameStart && (c == ';' || c == '}')) {
          nameEnd = true;
        }
      }

      return convert(stringBuilder.toString());
    } else {
      throw new Exception("config file wrong");
    }
  }

  private static Object[] convert(String configString) throws Throwable {
    Object[] result = new Object[3];
    int currentDoGroup = 3;
    int currentDoPosition = 0;
    long currentFrequency = 240L;
    String name = null;
    StringBuilder baseConfigBuilder = new StringBuilder();
    int musicNoteConfigStartIndex = 0;
    char[] var9 = configString.toCharArray();
    int var10 = var9.length;

    int var11;
    for(var11 = 0; var11 < var10; ++var11) {
      char c = var9[var11];
      baseConfigBuilder.append(c);
      ++musicNoteConfigStartIndex;
      if (c == '}') {
        break;
      }
    }

    String baseConfigString = baseConfigBuilder.substring(1, baseConfigBuilder.length() - 1);
    String[] var24 = baseConfigString.split(";");
    var11 = var24.length;

    for(int var27 = 0; var27 < var11; ++var27) {
      String baseConfig = var24[var27];
      if (!baseConfig.equals("")) {
        String frequency;
        if (baseConfig.contains("tune:")) {
          frequency = baseConfig.replace("tune:", "");
          if (frequency.length() != 1) {
            throw new Exception("tune length is not 1");
          }

          char charTune = frequency.toUpperCase().charAt(0);
          if (charTune < 'A' || charTune > 'G') {
            throw new Exception("tune not in range [A-G]");
          }

          if (charTune == 'A') {
            --currentDoGroup;
            currentDoPosition = 5;
          } else if (charTune == 'B') {
            --currentDoGroup;
            currentDoPosition = 6;
          } else {
            currentDoPosition += charTune - 67;
          }
        } else if (baseConfig.contains("frequency:")) {
          frequency = baseConfig.replace("frequency:", "");
          if (!frequency.matches("^\\d+$")) {
            throw new Exception("frequency is not number");
          }

          currentFrequency = Long.valueOf(frequency);
          if (currentFrequency < 60L || currentFrequency > 4000L) {
            throw new Exception("frequency not int range [60,4000]");
          }
        } else if (baseConfig.contains("name:")) {
          name = baseConfig.replace("name:", "");
        }
      }
    }

    if (TextUtils.isEmpty(name)) {
      throw new Exception("no music name");
    } else {
      result[0] = name;
      result[1] = configString;
      Log.e("TAG", "convert(PianoConvertUtils.java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")configString:" + configString);
      String musicConfigString = configString.substring(musicNoteConfigStartIndex);
      HashSet<Integer> highSet = new HashSet();
      HashSet<Integer> lowSet = new HashSet();
      List<com.chengtao.pianoview.utils.PianoConvertUtils.PianoKey> pianoKeyList = new ArrayList();
      String[] var30 = musicConfigString.split("\\|");
      int var31 = var30.length;

      for(int var16 = 0; var16 < var31; ++var16) {
        String musicNotePart = var30[var16];
        String[] var18 = musicNotePart.split(",");
        int var19 = var18.length;

        for(int var20 = 0; var20 < var19; ++var20) {
          String musicNote = var18[var20];
          if (!musicNote.equals("")) {
            if (musicNote.matches("^[0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$")) {
              addNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet, pianoKeyList, musicNote, false, false);
            } else if (musicNote.matches("^[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$")) {
              addHighLowNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet, pianoKeyList, musicNote, false, false);
            } else {
              String remainString;
              if (musicNote.matches("^HO[0-7](\\*(0\\.25|0\\.5|2|4))?$|^LO[0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$")) {
                remainString = musicNote.substring(2);
                if (musicNote.contains("HO")) {
                  addNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet, pianoKeyList, remainString, true, false);
                } else if (musicNote.contains("LO")) {
                  addNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet, pianoKeyList, remainString, false, true);
                }
              } else {
                if (!musicNote.matches("^HO[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4))?$|^LO[H,M,L][0-7](\\*(0\\.25|0\\.5|2|4|6|8))?$")) {
                  throw new Exception("music config wrong:" + musicNote);
                }

                remainString = musicNote.substring(2);
                if (musicNote.contains("HO")) {
                  addHighLowNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet, pianoKeyList, remainString, true, false);
                } else if (musicNote.contains("LO")) {
                  addHighLowNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet, pianoKeyList, remainString, false, true);
                }
              }
            }
          }
        }

        highSet.clear();
        lowSet.clear();
      }

      result[2] = pianoKeyList;
      return result;
    }
  }

  private static void addHighLowNumberKey(int currentDoGroup, int currentDoPosition, long currentFrequency, HashSet<Integer> highSet, HashSet<Integer> lowSet, List<com.chengtao.pianoview.utils.PianoConvertUtils.PianoKey> pianoKeyList, String musicNote, boolean highTune, boolean lowTune) {
    char status = musicNote.charAt(0);
    int number = Integer.valueOf(musicNote.charAt(1) + "");
    switch(status) {
      case 'H':
        highSet.add(number);
        lowSet.remove(number);
        break;
      case 'L':
        lowSet.add(number);
        highSet.remove(number);
        break;
      case 'M':
        highSet.remove(number);
        lowSet.remove(number);
    }

    addNumberKey(currentDoGroup, currentDoPosition, currentFrequency, highSet, lowSet, pianoKeyList, musicNote.substring(1), highTune, lowTune);
  }

  private static void addNumberKey(int currentDoGroup, int currentDoPosition, long currentFrequency, HashSet<Integer> highSet, HashSet<Integer> lowSet, List<com.chengtao.pianoview.utils.PianoConvertUtils.PianoKey> pianoKeyList, String musicNote, boolean highTune, boolean lowTune) {
    if (musicNote.length() == 1) {
      pianoKeyList.add(obtainPianoKey(currentDoGroup, currentDoPosition, currentFrequency, Integer.valueOf(musicNote), highSet, lowSet, highTune, lowTune));
    } else {
      int number = Integer.valueOf(musicNote.charAt(0) + "");
      Float times = Float.valueOf(musicNote.substring(2));
      pianoKeyList.add(obtainPianoKey(currentDoGroup, currentDoPosition, (long)((float)currentFrequency * times), number, highSet, lowSet, highTune, lowTune));
    }

  }

  private static com.chengtao.pianoview.utils.PianoConvertUtils.PianoKey obtainPianoKey(int currentDoGroup, int currentDoPosition, long frequency, int musicNoteNumber, HashSet<Integer> highSet, HashSet<Integer> lowSet, Boolean highTune, Boolean lowTune) {
    com.chengtao.pianoview.utils.PianoConvertUtils.PianoKey key = new com.chengtao.pianoview.utils.PianoConvertUtils.PianoKey();
    if (musicNoteNumber == 0) {
      key.setType(-1);
    } else {
      int group = currentDoGroup;
      int position = currentDoPosition + musicNoteNumber - 1;
      if (position > 6) {
        group = currentDoGroup + 1;
        position -= 7;
      }

      if (highTune) {
        ++group;
      } else if (lowTune) {
        --group;
      }

      if (highSet.contains(musicNoteNumber)) {
        if (!HIGH_BLACK.contains(musicNoteNumber)) {
          ++position;
          if (position > 6) {
            ++group;
            position -= 7;
          }

          key.setType(1);
        } else {
          if (position > 1) {
            --position;
          }

          key.setType(0);
        }
      } else if (lowSet.contains(musicNoteNumber)) {
        if (!LOW_BLACK.contains(musicNoteNumber)) {
          --position;
          if (position < 0) {
            --group;
            if (group != 0) {
              position += 7;
            } else {
              position += 2;
            }
          }

          key.setType(1);
        } else {
          if (position <= 2) {
            --position;
          } else {
            position -= 2;
          }

          key.setType(0);
        }
      } else {
        key.setType(1);
      }

      key.setGroup(group);
      key.setPosition(position);
    }

    key.setFrequency(frequency);
    return key;
  }

  public static final class PianoKey {
    public static final int BLACK_KEY = 0;
    public static final int WHITE_KEY = 1;
    public static final int NULL_KEY = -1;
    private int group;
    private int position;
    private int type = -1;
    private long frequency;

    public PianoKey() {
    }

    public int getGroup() {
      return this.group;
    }

    public void setGroup(int group) {
      this.group = group;
    }

    public int getPosition() {
      return this.position;
    }

    public void setPosition(int position) {
      this.position = position;
    }

    public int getType() {
      return this.type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public long getFrequency() {
      return this.frequency;
    }

    public void setFrequency(long frequency) {
      this.frequency = frequency;
    }

    public String toString() {
      return "PianoKey [group=" + this.group + ", position=" + this.position + ", type=" + this.type + ", frequency=" + this.frequency + "]";
    }
  }

  public static final class Error {
    public static final String FILE_NOT_EXIT = "file not exist";
    public static final String READ_FILE_EXCEPTION = "read file exception";
    public static final String CONFIG_FILE_WRONG = "config file wrong";
    public static final String TUNE_LENGTH_NOT_ONE = "tune length is not 1";
    public static final String TUNE_NOT_IN_RANGE = "tune not in range [A-G]";
    public static final String FREQUENCY_NOT_NUMBER = "frequency is not number";
    public static final String FREQUENCY_NOT_IN_RANGE = "frequency not int range [60,4000]";
    public static final String NO_MUSIC_NAME = "no music name";
    public static final String MUSIC_NOTE_CONFIG_WRONG = "music config wrong";

    public Error() {
    }
  }
}