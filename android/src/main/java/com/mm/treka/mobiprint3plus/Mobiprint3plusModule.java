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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.mobiiot.androidqapi.api.CsPrinter;
import com.mobiiot.androidqapi.api.MobiiotAPI;
import com.mobiiot.androidqapi.api.Utils.BitmapUtils;
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil;

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
    printCenterText("********************************", 1, true, false);
    printCenterText("*********** " + text + " ************", 1, true, false);
    printCenterText("********************************", 1, true, false);
  }

  @ReactMethod
  public void printLine() {
    printer.printCenterText(
      "================================",
      1,
      false,
      false
    );
  }

  @ReactMethod
  public void printImage(byte[] data) {
    try {
      if (data != null) {
        Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        int mwidth = mBitmap.getWidth();

        int deffInWidth = 384 - mwidth;

        printBitmap(
          getResizedBitmap(
            mBitmap,
            mBitmap.getWidth() + deffInWidth,
            mBitmap.getHeight() + deffInWidth
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
    // Context context = this.reactContext.getCurrentActivity();
    printer.printEndLine();
  }
}
