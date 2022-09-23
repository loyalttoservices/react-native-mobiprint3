// Mobiprint3plusModule.java

package com.mm.treka.mobiprint3plus;

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

  @Override
  protected void onResume() {
    super.onResume();

    //initialize mp3 printer
    new MobiiotAPI(this);
  }

  @ReactMethod
  private int mp3Print(byte[] data) {
    int result = 0;

    System.out.println("before csprinter");

    CsPrinter.printText("text test ");

    System.out.println("after csprinter");

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

        result = -1;
      }
    } catch (Exception ex) {
      System.out.println("Quelque chose ne va pas sur MOBIIOT");
      result = 0;
    }

    return result;
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
    //        CsPrinter.printText("-----------------------------");
    //        CsPrinter.printText("width   = "+bit.getWidth());
    //        CsPrinter.printText("width/8 = "+(float)bit.getWidth()/8);
    //        CsPrinter.printText("bit     = "+bit.getConfig());
    CsPrinter.printBitmap(bit);
  }

  @ReactMethod
  public void printText(String text) {
    CsPrinter.printText(text);
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
}
