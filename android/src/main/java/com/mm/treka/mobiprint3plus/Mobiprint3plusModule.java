package com.mm.treka.mobiprint3plus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Base64;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.mobiiot.androidqapi.api.CsPrinter;
import com.mobiiot.androidqapi.api.MobiiotAPI;
import com.mobiiot.androidqapi.api.Utils.BitmapUtils;
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil;
import java.nio.charset.Charset;
import java.util.*;
import javax.annotation.Nullable;

public class Mobiprint3plusModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private CsPrinter printer;

  public Mobiprint3plusModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "Mobiprint3plus";
  }

  @ReactMethod
  public void connectPOS() {
    Context context = this.reactContext.getCurrentActivity();
    this.printer = new CsPrinter();
    try {
      new MobiiotAPI(context);
    } catch (NullPointerException ex) {
      ex.printStackTrace();
    }
  }

  @ReactMethod
  public void printText(String text) {
    printer.printText(text);
  }

  @ReactMethod
  public void printCustomText(
    String text,
    int size,
    int align,
    int center,
    boolean bold,
    boolean underline
  ) {
    printer.printText_FullParam(text, size, align, 1, center, bold, underline);
  }

  @ReactMethod
  public void printLeftText(
    String text,
    int size,
    boolean bold,
    boolean underline
  ) {
    printer.printText_FullParam(text, size, 0, 1, 0, bold, underline);
  }

  @ReactMethod
  public void printRightText(
    String text,
    int size,
    boolean bold,
    boolean underline
  ) {
    printer.printText_FullParam(text, size, 1, 1, 0, bold, underline);
  }

  @ReactMethod
  public void printCenterText(
    String text,
    int size,
    boolean bold,
    boolean underline
  ) {
    printer.printText_FullParam(text, size, 0, 1, 1, bold, underline);
  }

  @ReactMethod
  public void printHeader(String text) {
    printCenterText("********************************", 0, true, false);
    printCenterText("*********** " + text + " ************", 0, true, false);
    printCenterText("********************************", 0, true, false);
  }

  @ReactMethod
  public void printLine() {
    printCenterText(
      "------------------------------------------",
      0,
      false,
      false
    );
  }

  @ReactMethod
  public void printImage(
    String base64encodeStr,
    @Nullable ReadableMap options
  ) {
    int width = 0;
    int height = 0;
    int leftPadding = 0;
    if (options != null) {
      width = options.hasKey("width") ? options.getInt("width") : 0;
      height = options.hasKey("height") ? options.getInt("height") : 0;
      leftPadding = options.hasKey("left") ? options.getInt("left") : 0;
    }

    //cannot larger then devicesWith;
    if (width > 384 || width == 0) {
      width = 384;
    }

    if (height > 200 || height == 0) {
      height = 200;
    }

    try {
      byte[] bytes = Base64.decode(base64encodeStr, Base64.DEFAULT);
      if (bytes != null) {
        Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        int mwidth = mBitmap.getWidth();
        int mheight = mBitmap.getWidth();

        int deffInWidth = width - mwidth;
        int deffInHeight = height - mheight;

        printBitmap(
          getResizedBitmap(
            mBitmap,
            mBitmap.getWidth() + deffInWidth,
            mBitmap.getHeight() + deffInHeight
          ),
          Bitmap.Config.ARGB_8888
        );
      }
    } catch (Exception ex) {
      System.out.println("Quelque chose ne va pas sur MOBIIOT");
    }
  }

  public static Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
    Bitmap convertedBitmap = Bitmap.createBitmap(
      bitmap.getWidth(),
      bitmap.getHeight(),
      config
    );
    Canvas canvas = new Canvas(convertedBitmap);
    Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    canvas.drawBitmap(bitmap, 0, 0, paint);
    return convertedBitmap;
  }

  public void printBitmap(Bitmap bitmap, Bitmap.Config config) {
    Bitmap bit = convert(bitmap, config);
    printer.printBitmap(bit);
  }

  public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
    int width = bm.getWidth();
    int height = bm.getHeight();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight);

    // "RECREATE" THE NEW BITMAP
    Bitmap resizedBitmap = Bitmap.createBitmap(
      bm,
      0,
      0,
      width,
      height,
      matrix,
      false
    );
    bm.recycle();
    return resizedBitmap;
  }

  @ReactMethod
  public void print() {
    printer.printEndLine();
  }

  @ReactMethod
  public void printColumn(
    ReadableArray columnWidths,
    ReadableArray columnAligns,
    ReadableArray columnTexts,
    @Nullable ReadableMap options
  ) {
    if (
      columnWidths.size() != columnTexts.size() ||
      columnWidths.size() != columnAligns.size()
    ) {
      System.out.println("COLUMN_WIDTHS_ALIGNS_AND_TEXTS_NOT_MATCH");
      return;
    }
    int totalLen = 0;
    for (int i = 0; i < columnWidths.size(); i++) {
      totalLen += columnWidths.getInt(i);
    }
    int maxLen = 384 / 8;
    if (totalLen > maxLen) {
      System.out.println("COLUNM_WIDTHS_TOO_LARGE");
      return;
    }

    int size = 0;
    int alignText = 0;
    int center = 0;
    boolean bold = false;
    boolean underline = false;
    if (options != null) {
      size = options.hasKey("size") ? options.getInt("size") : 0;
      alignText = options.hasKey("align") ? options.getInt("align") : 0;
      center = options.hasKey("center") ? options.getInt("center") : 0;
      bold = options.hasKey("bold") ? options.getBoolean("bold") : false;
      underline =
        options.hasKey("underline") ? options.getBoolean("underline") : false;
    }

    List<List<String>> table = new ArrayList<List<String>>();

    /**splits the column text to few rows and applies the alignment **/
    int padding = 1;
    for (int i = 0; i < columnWidths.size(); i++) {
      int width = columnWidths.getInt(i) - padding; //1 char padding
      String text = String.copyValueOf(columnTexts.getString(i).toCharArray());
      List<ColumnSplitedString> splited = new ArrayList<ColumnSplitedString>();
      int shorter = 0;
      int counter = 0;
      String temp = "";
      for (int c = 0; c < text.length(); c++) {
        char ch = text.charAt(c);
        int l = 1;
        temp = temp + ch;

        if (counter + l < width) {
          counter = counter + l;
        } else {
          splited.add(new ColumnSplitedString(shorter, temp));
          temp = "";
          counter = 0;
          shorter = 0;
        }
      }
      if (temp.length() > 0) {
        splited.add(new ColumnSplitedString(shorter, temp));
      }
      int align = columnAligns.getInt(i);

      List<String> formated = new ArrayList<String>();
      for (ColumnSplitedString s : splited) {
        StringBuilder empty = new StringBuilder();
        for (int w = 0; w < (width + padding - s.getShorter()); w++) {
          empty.append(" ");
        }
        int startIdx = 0;
        String ss = s.getStr();
        if (align == 1 && ss.length() < (width - s.getShorter())) {
          startIdx = (width - s.getShorter() - ss.length()) / 2;
          if (startIdx + ss.length() > width - s.getShorter()) {
            startIdx--;
          }
          if (startIdx < 0) {
            startIdx = 0;
          }
        } else if (align == 2 && ss.length() < (width - s.getShorter())) {
          startIdx = width - s.getShorter() - ss.length();
        }
        empty.replace(startIdx, startIdx + ss.length(), ss);
        formated.add(empty.toString());
      }
      table.add(formated);
    }

    /**  try to find the max row count of the table **/
    int maxRowCount = 0;
    for (int i = 0; i < table.size()/*column count*/; i++) {
      List<String> rows = table.get(i); // row data in current column
      if (rows.size() > maxRowCount) {
        maxRowCount = rows.size();
      } // try to find the max row count;
    }

    /** loop table again to fill the rows **/
    StringBuilder[] rowsToPrint = new StringBuilder[maxRowCount];
    for (int column = 0; column < table.size()/*column count*/; column++) {
      List<String> rows = table.get(column); // row data in current column
      for (int row = 0; row < maxRowCount; row++) {
        if (rowsToPrint[row] == null) {
          rowsToPrint[row] = new StringBuilder();
        }
        if (row < rows.size()) {
          //got the row of this column
          rowsToPrint[row].append(rows.get(row));
        } else {
          int w = columnWidths.getInt(column);
          StringBuilder empty = new StringBuilder();
          for (int i = 0; i < w; i++) {
            empty.append(" ");
          }
          rowsToPrint[row].append(empty.toString()); //Append spaces to ensure the format
        }
      }
    }

    /** loops the rows and print **/
    for (int i = 0; i < rowsToPrint.length; i++) {
      rowsToPrint[i].append("\n\r"); //wrap line..
      try {
        printer.printText_FullParam(
          rowsToPrint[i].toString(),
          size,
          alignText,
          1,
          center,
          bold,
          underline
        );
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("COMMAND_NOT_SEND");
      }
    }
    return;
  }

  private static class ColumnSplitedString {

    private int shorter;
    private String str;

    public ColumnSplitedString(int shorter, String str) {
      this.shorter = shorter;
      this.str = str;
    }

    public int getShorter() {
      return shorter;
    }

    public String getStr() {
      return str;
    }
  }
}
