package com.crispakeinc.ocrdict.common;

import android.annotation.TargetApi;
import android.app.*;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.*;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crispakeinc.ocrdict.domain.EnumAlign;
import com.crispakeinc.ocrdict.domain.Time;
import com.crispcakeinc.ocrdict.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class OcrDictUtils {
    public static final String LEFT_BRACKET = Pattern.quote("(");
    public static final String RIGHT_BRACKET = Pattern.quote(")");
    public static final String PLUS = Pattern.quote("+");
    public static final int ONE_DAY_MINISECONDS = 1000 * 60 * 60 * 24;
    //    private static final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(WhereskAndroidCommonUtils.class.getName());
    private static PowerManager.WakeLock wl;
    static private final String[] MARKER_COLORS = {
            "#FF8A65",
            "#9CCC65",
            "#BA68C8",
            "#B39DDB",
            "#AED581",
            "#90A4AE",
            "#9575CD",
            "#F6BF26",
            "#7986CB",
            "#57BB8A",
            "#E06055",
            "#F06292",
            "#FF8A65"};

    public static File GetFileByFileName(Context context, String imageFileNameWithExtension) {
        File image = new File(GetWritableDir(context).getAbsolutePath() + "/" + imageFileNameWithExtension);
        return image;
    }

    public static File GetWritableDir(Context context) {
        return context.getExternalFilesDir(null);
    }

    public static String GetExternalFilePath(Context context, String fileName) {
        return GetWritableDir(context).getAbsolutePath() + "/" + fileName;
    }

    public static Bitmap Rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();

            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                throw ex;
            }
        }
        return b;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static String GeneratePhoneNumberWhereClauseForDB(String phoneNumber, String columnName) {
        String phoneNumberWhereClause;
        if (phoneNumber.length() >= 8) {
            phoneNumberWhereClause = columnName + " like '%" + phoneNumber.substring(phoneNumber.length() - 8, phoneNumber.length()) + "'";
        } else {
            phoneNumberWhereClause = columnName + " = '" + phoneNumber + "'";
        }
        return phoneNumberWhereClause;
    }


    public static int[] GetPhoneScreenSize(Context context) {
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int screenWidth = 0;
        int screenHeight = 0;
        Display defaultDisplay = w.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            defaultDisplay.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        } else {
            Display d = defaultDisplay;
            screenWidth = d.getWidth();
            screenHeight = d.getHeight();
        }
        return new int[]{screenWidth, screenHeight};
    }

    public static Time GetTimeByMiliSeconds(Long miliSeconds) {
        if (miliSeconds == null)
            return null;
        int days = (int) (miliSeconds / (1000 * 60 * 60 * 24));
        int hours = (int) ((miliSeconds - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int minutes = (int) (miliSeconds - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        int seconds = (int) (miliSeconds - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours) - (1000 * 60 * minutes)) / 1000;
        Time time = new Time(days, hours, minutes, seconds);
        return time;
    }

    public static Time GetTimeDifferenceByDates(Date begin, Date end) {
        if (begin == null || end == null || begin.after(end))
            return null;
        return GetTimeByMiliSeconds(end.getTime() - begin.getTime());
    }

    public static String GetRoughTimeTextByTime(Context context, Time time) {
        if (time == null)
            return 1 + context.getString(R.string.second);
        int days = time.getDay();
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();

        StringBuilder stringBulder = new StringBuilder();
        if (days != 0)
            stringBulder.append(days + context.getString(days == 1 ? R.string.day : R.string.days));
        else if (hours != 0)
            stringBulder.append(hours + context.getString(hours == 1 ? R.string.hour : R.string.hours));
        else if (minutes != 0)
            stringBulder.append(minutes + context.getString(minutes == 1 ? R.string.minute : R.string.minutes));
        else if (seconds != 0)
            stringBulder.append(seconds + context.getString(seconds == 1 ? R.string.second : R.string.seconds));
        else
            stringBulder.append(1 + context.getString(R.string.second));
        return stringBulder.toString();
    }

    public static String GetSubstringByCount(String str, int count, String suffix) {
        if (str == null || count == 0)
            return null;
        if (str.length() <= count)
            return str;
        return str.substring(0, count) + suffix;
    }

    public static String GetTimePrintable(String pattern, Date date) {
        if (pattern == null || date == null) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Error in class MapyouAndroidCommonUtils, method: getTimePrintable: ", e);
            return null;
        }
    }

    public static Bitmap GetPhotoBitmap(Context context, String photoPath, Integer widthPixel, Integer heightPixel) {
        String absolutePhotoPath = GetWritableDir(context).getAbsolutePath() + "/" + photoPath;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        if (widthPixel != null || heightPixel != null) {
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(absolutePhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = 1;
            if (widthPixel != null)
                scaleFactor = photoW / widthPixel;
            else if (heightPixel != null)
                scaleFactor = photoH / heightPixel;
            bmOptions.inSampleSize = scaleFactor;
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;
        return BitmapFactory.decodeFile(absolutePhotoPath, bmOptions);
    }

    public static Bitmap GetBitmapFromImageFile(File imageFile) {
        Bitmap bmp = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;
        if (imageFile.exists())
            bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        return bmp;
    }

    /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
     */
    public static Bundle IntentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }
        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static Intent FragmentArgumentsToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable("_uri");
        if (data != null) {
            intent.setData(data);
        }

        intent.putExtras(arguments);
        intent.removeExtra("_uri");
        return intent;
    }

    public static String GetMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String key = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                if (metaKey.equals("AA_DB_VERSION"))
                    key = String.valueOf(metaData.getInt(metaKey));
                else
                    key = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return key;
    }

    static public boolean IsSeriviceRunning(Context ctx, String servicePackageName) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (servicePackageName.equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }

    public static Bitmap GetBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    public static void StartNewActivity(Context context, Class<? extends Activity> newTopActivityClass) {
        Intent intent = new Intent(context, newTopActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static File WriteImageToFile(Context context, Bitmap bitmap, String imageNameWithExtention) {
        try {
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //Compress image with quality control
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            byte[] bitmapdata = bos.toByteArray();
            File file = GetFileByFileName(context, imageNameWithExtention);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            bos.close();
            return file;
        } catch (Exception e) {
            Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Error occured in Class MapyouAndroidCommonUtils, method writeImageToFile: ", e);
            return null;
        }
    }

    public static void DeleteFile(String fileAbsolutePath) {
        File originalFile = new File(fileAbsolutePath);
        boolean succeed = originalFile.delete();
        if (!succeed) {
            Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Can not delete file: " + fileAbsolutePath);
        }
    }

    public static Bitmap ResizeAndScalePhoto(String originalPhotoPath, int targetWidth, int targetHeight) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(originalPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        if (photoW > photoH) {
            photoW = bmOptions.outHeight;
            photoH = bmOptions.outWidth;
        }

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetWidth, photoH / targetHeight);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(originalPhotoPath, bmOptions);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width > height) {
                bitmap = Rotate(bitmap, 90);
            }
        }
        return bitmap;
    }

    public static Bitmap ResizeImageForImageView(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float multFactor = -1.0F;
        if (originalHeight > originalWidth) {
            multFactor = (float) originalWidth / (float) originalHeight;
            newWidth = (int) (newHeight * multFactor);
        } else if (originalWidth > originalHeight) {
            multFactor = (float) originalHeight / (float) originalWidth;
            newHeight = (int) (newWidth * multFactor);
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        if (resizedBitmap != null) {
            int width = resizedBitmap.getWidth();
            int height = resizedBitmap.getHeight();
            if (width > height) {
                resizedBitmap = Rotate(resizedBitmap, 90);
            }
        }
        return resizedBitmap;
    }

    public static void MoveFile(Context context, String originalFileName, String targetFileName) {
        File originalFile = new File(GetExternalFilePath(context, originalFileName));
        File targetFile = new File(GetExternalFilePath(context, targetFileName));
        targetFile.delete();
        originalFile.renameTo(targetFile);
    }

    public static String RemoveSpecialCharactersForPhoneNumber(String string) {
        if (string == null)
            return null;
        String withOutSpace = StringUtils.replace(string, " ", "");
        String withOutHyphone = StringUtils.replace(withOutSpace, "-", "");
        String withoutLeftBracket = StringUtils.replace(withOutHyphone, LEFT_BRACKET, "");
        String withoutRightBracket = StringUtils.replace(withoutLeftBracket, RIGHT_BRACKET, "");
        return withoutRightBracket;
    }

    public static boolean IsPhoneNumberUnknown(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() <= 2)
            return true;
        else
            return false;
    }

    public static boolean IsSameDay(long time1, long time2) {
        TimeZone LOCAL_TIME_ZONE = TimeZone.getDefault();
        TimeZone.setDefault(LOCAL_TIME_ZONE);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        cal2.setTimeInMillis(time2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean IsSameMinute(long time1, long time2) {
        TimeZone LOCAL_TIME_ZONE = TimeZone.getDefault();
        TimeZone.setDefault(LOCAL_TIME_ZONE);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        cal2.setTimeInMillis(time2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                && cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)
                && cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)
                ;
    }

    public static int GetWeekOfTheDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int GetDayOfTheWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    public static String GetFormatDateTime(Context context, long callingTime, Date today) {
        Date yesterday = new Date(today.getTime() - ONE_DAY_MINISECONDS);
        int weekOfToday = GetWeekOfTheDate(today);
        int lastWeekOfToday = weekOfToday - 1;

        if (IsSameDay(today.getTime(), callingTime)) {
            return context.getString(R.string.today);
        } else if (IsSameDay(yesterday.getTime(), callingTime)) {
            return context.getString(R.string.yesterday);
        } else {
            Date callingDate = new Date(callingTime);
            int dayOfWeekCallingDate = GetDayOfTheWeek(callingDate);
            String weekString = DateUtils.formatDateTime(context, callingTime, DateUtils.FORMAT_SHOW_WEEKDAY);
            if (weekOfToday == GetWeekOfTheDate(callingDate)) {
                switch (dayOfWeekCallingDate) {
                    case Calendar.MONDAY:
                        weekString = context.getString(R.string.this_monday);
                        break;
                    case Calendar.TUESDAY:
                        weekString = context.getString(R.string.this_tuesday);
                        break;
                    case Calendar.WEDNESDAY:
                        weekString = context.getString(R.string.this_wednesday);
                        break;
                    case Calendar.THURSDAY:
                        weekString = context.getString(R.string.this_thursday);
                        break;
                    case Calendar.FRIDAY:
                        weekString = context.getString(R.string.this_friday);
                        break;
                    case Calendar.SATURDAY:
                        weekString = context.getString(R.string.this_saturday);
                        break;
                    case Calendar.SUNDAY:
                        weekString = context.getString(R.string.this_sunday);
                        break;
                }
            } else if (lastWeekOfToday == GetWeekOfTheDate(callingDate)) {
                switch (dayOfWeekCallingDate) {
                    case Calendar.MONDAY:
                        weekString = context.getString(R.string.last_monday);
                        break;
                    case Calendar.TUESDAY:
                        weekString = context.getString(R.string.last_tuesday);
                        break;
                    case Calendar.WEDNESDAY:
                        weekString = context.getString(R.string.last_wednesday);
                        break;
                    case Calendar.THURSDAY:
                        weekString = context.getString(R.string.last_thursday);
                        break;
                    case Calendar.FRIDAY:
                        weekString = context.getString(R.string.last_friday);
                        break;
                    case Calendar.SATURDAY:
                        weekString = context.getString(R.string.last_saturday);
                        break;
                    case Calendar.SUNDAY:
                        weekString = context.getString(R.string.last_sunday);
                        break;
                }
            }
            return DateUtils.formatDateTime(context, callingTime, DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE) + " " + weekString;
        }
    }

    static public boolean IsOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    public static boolean IsWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isAvailable() && mWifi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    static public boolean IsNetworkAvailable(Context context) {
        return IsOnline(context) || IsWifiConnected(context);
    }

    static public String GetFormatedDisplayDateByPattern(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    static public String GetDistanceStringByValue(Context context, double distance) {
        String distanceString;
        if (distance < 0) {
            distanceString = 0 + " " + context.getString(R.string.meter);
        } else if (distance < 1000) {
            distanceString = (int) Math.round(distance) + " " + context.getString(R.string.meter);
        } else {
            DecimalFormat df = new DecimalFormat(".0");
            df.setMaximumFractionDigits(1);
            distanceString = df.format(distance / 1000) + " " + context.getString(R.string.kilometer);
        }
        return distanceString;
    }

    static public Double GetDistanceBetweenTwoLocations(double lat1, double lon1, double lat2, double lon2) {
        double DEF_PI180 = 0.01745329252; // PI/180.0
        double DEF_R = 6370693.5; // radius of earth
        double ew1, ns1, ew2, ns2;
        double distance;

        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 求大圆劣弧与球心所夹的角(弧度)
        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0)
            distance = 1.0;
        else if (distance < -1.0)
            distance = -1.0;
        // 求大圆劣弧长度
        distance = DEF_R * Math.acos(distance);
        return distance;
    }


    static public void SetupOnGlobalLayoutListener(final View view, final Callable<Void> callable) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                                 @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                                                 @Override
                                                                 public void onGlobalLayout() {
                                                                     try {
                                                                         callable.call();
                                                                     } catch (Exception e) {
                                                                         Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Error occured in class MapyouAndroidCommonUtils, method setupOnGlobalLayoutListener: ", e);
                                                                     }
                                                                     int currentApiVersion = Build.VERSION.SDK_INT;
                                                                     if (currentApiVersion < Build.VERSION_CODES.JELLY_BEAN)
                                                                         view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                                                     else
                                                                         view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                                                 }
                                                             }
        );
    }

    static public String GetMobileInfo(Context context) {
        try {
            int[] phoneScreenSize = GetPhoneScreenSize(context);
            return Build.MANUFACTURER
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + Build.PRODUCT
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + Build.MODEL
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + Build.BRAND
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD ? Build.SERIAL : "")
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + Build.DEVICE
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + Build.DISPLAY
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + Build.VERSION.SDK_INT
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + Build.VERSION.RELEASE
                    + OcrDictConstants.MOBILE_INFO_DELIMITER + phoneScreenSize[0] + "x" + phoneScreenSize[1];
        } catch (Exception e) {
            Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Error occured in Class MapyouAndroidCommonUtils, method getMobileInfo: ", e);
            return "";
        }
    }

    public static boolean IsSmallScreen(Context context) {
        int[] screenResolution = GetPhoneScreenSize(context);
        int width = screenResolution[0];
        if (width <= OcrDictConstants.SMALL_SCREEN_WIDTH)
            return true;
        return false;
    }

    public static Integer GetTheFirstIntegerByString(String s) {
        if (s == null || s.equals(""))
            return null;
        return Integer.parseInt(s.replaceFirst(".*?(\\d+).*", "$1"));
    }

    public static String GetAddressAfterRemovingCountry(Context context, String address, String comparedAddress) {
        try {
            if (address == null || address.equals(""))
                return context.getString(R.string.unknown_address);
            if (comparedAddress == null || comparedAddress.equals(""))
                return address;
            String[] elementsOfAddress = address.split(",");
            String[] elementsOfComparedAddress = address.split(",");
            String countryOfAddress = elementsOfAddress[elementsOfAddress.length - 1];
            String countryOfComparedAddress = elementsOfComparedAddress[elementsOfComparedAddress.length - 1];
            if (countryOfAddress != null && countryOfComparedAddress != null && countryOfAddress.equals(countryOfComparedAddress)) {
                return address.substring(0, address.length() - (countryOfAddress.length() + 1));
            }
        } catch (Exception e) {
            Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "", e);
        }
        return address;
    }

    public static boolean IsActivityForeground(Context context, String activityName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        if (componentInfo.getClassName().equals(activityName))
            return true;
        return false;
    }

    public static Bitmap CombineImages(Bitmap topImage, Bitmap bottomImage, EnumAlign alignEnum) {
        Bitmap cs = null;

        Bitmap longerBitMap;
        Bitmap shorterBitMap;

        if (topImage.getWidth() > bottomImage.getWidth()) {
            longerBitMap = topImage;
            shorterBitMap = bottomImage;
        } else {
            longerBitMap = bottomImage;
            shorterBitMap = topImage;
        }

        cs = Bitmap.createBitmap(longerBitMap.getWidth(), topImage.getHeight() + bottomImage.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        float topImageLeft = 0f;
        float bottomImageLeft = 0f;
        switch (alignEnum) {
            case LEFT:
                topImageLeft = 0f;
                bottomImageLeft = 0f;
                break;
            case RIGHT:
                if (topImage == shorterBitMap) {
                    topImageLeft = bottomImage.getWidth() - topImage.getWidth();
                    bottomImageLeft = 0f;
                } else {
                    topImageLeft = 0f;
                    bottomImageLeft = topImage.getWidth() - bottomImage.getWidth();
                }
                break;
            case CENTER:
                if (topImage == shorterBitMap) {
                    topImageLeft = bottomImage.getWidth() / 2 - topImage.getWidth() / 2;
                    bottomImageLeft = 0f;
                } else {
                    topImageLeft = 0f;
                    bottomImageLeft = topImage.getWidth() / 2 - bottomImage.getWidth() / 2;
                }
                break;
        }
        comboImage.drawBitmap(topImage, topImageLeft, 0f, null);
        comboImage.drawBitmap(bottomImage, bottomImageLeft, topImage.getHeight(), null);
        return cs;
    }

    public static Bitmap CombineImagesWithRedpoit(Bitmap redpointBitMap, Bitmap timeLableBitMap, Bitmap baseImageBitMap) {
        Bitmap cs = null;

        Float redpointHalf = (float) (redpointBitMap.getWidth()) / 2;
        int width = baseImageBitMap.getWidth() + redpointHalf.intValue();
        int height = baseImageBitMap.getHeight();
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(baseImageBitMap, 0f, 0, null);
        comboImage.drawBitmap(redpointBitMap, width - redpointBitMap.getWidth(), timeLableBitMap.getHeight() - redpointHalf, null);
        return cs;
    }

    public static Bitmap DrawText(String text, int textSize, int textColor, int backgroundColor) {
        // Get text dimensions
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        StaticLayout mTextLayout = new StaticLayout(text, textPaint, (int) Math.ceil(textPaint.measureText(text, 0, text.length())), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap b = Bitmap.createBitmap(mTextLayout.getWidth() + 10, mTextLayout.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);

        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroundColor);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(5, 0);
        mTextLayout.draw(c);
        c.restore();

        return b;
    }

    public static Bitmap AddBorder(Bitmap bmp, int borderSize, int borderColor) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(borderColor);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }


    public static Bitmap DrawTriangle(Point a, Point b, Point c, int fillColor) {
        Bitmap bmpTriangle = Bitmap.createBitmap(Math.max(Math.max(a.x, b.x), c.x), Math.max(Math.max(a.y, b.y), c.y), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTriangle);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setColor(fillColor);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(0, 0);
        path.lineTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.close();

        canvas.drawPath(path, paint);

        return bmpTriangle;
    }

    public static Bitmap DrawTriangleAndOval(Context context, int triangleWidth, int triangleHeight, int ovalWidth, int ovalHeight) {
        int wholeRectangleWidth = ovalWidth;
        int wholeRectangleHeight = ovalHeight / 2 + triangleHeight;
        Bitmap bmpTriangleOval = Bitmap.createBitmap(wholeRectangleWidth, wholeRectangleHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTriangleOval);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);

        RectF oval = new RectF(0, triangleHeight - ovalHeight / 2, wholeRectangleWidth, wholeRectangleHeight);
        paint.setColor(context.getResources().getColor(R.color.home_pane_header));
        canvas.drawOval(oval, paint);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(ovalWidth / 2 - triangleWidth / 2, 0);
        path.lineTo(ovalWidth / 2 + triangleWidth / 2, 0);
        path.lineTo(ovalWidth / 2, triangleHeight);
        path.close();
        paint.setColor(Color.WHITE);
        canvas.drawPath(path, paint);

        return bmpTriangleOval;
    }

    public static Bitmap DrawOval(Context context, int ovalWidth, int ovalHeight) {
        Bitmap bmpOval = Bitmap.createBitmap(ovalWidth, ovalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpOval);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);

        RectF oval = new RectF(0, 0, ovalWidth, ovalHeight);
        paint.setColor(context.getResources().getColor(R.color.home_pane_header));
        canvas.drawOval(oval, paint);
        return bmpOval;
    }

    public static void ReleaseWakeLock(Context context) {
        if (wl != null && wl.isHeld()) {
            SharedPreferences sharedPref = context.getSharedPreferences(OcrDictConstants.WHERESK_SHARED_PREFERRENCES, Context.MODE_PRIVATE);
            if (sharedPref.getBoolean(OcrDictConstants.SHARED_PREFERRENCE_DEBUG_ENABLED, OcrDictConstants.DEFAULT_DEBUG_ENABLED))
                Toast.makeText(context, "Wake lock is released!", Toast.LENGTH_SHORT).show();
            wl.release();
            wl = null;
        }
    }

    public static void AcquireWakeLock(Context context) {
        if (wl == null) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MapyouWakeLock");
            wl.setReferenceCounted(false);
        }
        SharedPreferences sharedPref = context.getSharedPreferences(OcrDictConstants.WHERESK_SHARED_PREFERRENCES, Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(OcrDictConstants.SHARED_PREFERRENCE_DEBUG_ENABLED, OcrDictConstants.DEFAULT_DEBUG_ENABLED))
            Toast.makeText(context, "Wake lock is acquired!", Toast.LENGTH_SHORT).show();
        wl.acquire();
    }

    public static AlertDialog GetAndSetupInfoDialog(Context context, View infoIconView, int titleResourceId, int contentResourceId) {
        return GetAndSetupInfoDialog(context, infoIconView, context.getString(titleResourceId), context.getString(contentResourceId));
    }

    public static AlertDialog GetAndSetupInfoDialog(Context context, View infoIconView, String title, String content) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.got_it), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        if (infoIconView != null)
            infoIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.show();
                }
            });
        return alertDialog;
    }

    public static String GetFullURL(Context context, String relativeUrl) {
        return GetMetaValue(context, "server_url") + relativeUrl;
    }

    public static String FormatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    public static void Vibrate(Context context, int time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    public static void PlayNotificationAlarm(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
        }
    }

    public static boolean IsScreenOn(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return powerManager.isScreenOn();
    }

    public static String GetReplacedStringForSqlite(String originalString, String match, String replacement) {
        return "REPLACE(" + originalString + ", '" + match + "', '" + replacement + "')";
    }

    public static String GetWholeHtmlByContent(String content) {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<html><head>" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
                "<head><body>";

        return header + content + "</body></html>";
    }

    public static void HideSoftKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void GalleryAddPic(Context context, String fileAbsolutePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(fileAbsolutePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    static public boolean ConvertInputStreamToFile(InputStream inputStream, String fileAbsolutePath, AsyncTask asyncTask) throws IOException {
        OutputStream out = null;
        out = new FileOutputStream(fileAbsolutePath);
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = inputStream.read(bytes)) != -1) {
            if (asyncTask == null || !asyncTask.isCancelled())
                out.write(bytes, 0, read);
            else
                break;
        }
        inputStream.close();
        out.flush();
        out.close();
        if (asyncTask != null && asyncTask.isCancelled()) {
            File fileAbsolute = new File(fileAbsolutePath);
            if (fileAbsolute.exists())
                fileAbsolute.delete();
        }
        return true;
    }

    static public String GetRandomLongString() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return timeStamp + "_" + System.currentTimeMillis();
    }

    public static String GetFileNameByFullName(String relativeFileNameWithExtension) {
        if (relativeFileNameWithExtension == null)
            return null;

        return relativeFileNameWithExtension.substring(0, relativeFileNameWithExtension.lastIndexOf("."));
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public static Bitmap GetBitmapByUri(Context context, Uri uri) {
        // just some safety built in
        if (uri == null) {
            return null;
        }

        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (FileNotFoundException e) {
            Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Error in getBitmapByUri", e);
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Error in getBitmapByUri when run is.close()", e);
            }
        }
    }

    public static int CalculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String getAddressText(Context context, String address) {
        String noAddressAvailableText = context.getString(R.string.no_location_info_available);
        if (address == null || address.contains("??"))
            return noAddressAvailableText;
        return address;
    }

    public static void RemoveFiles(Context context, String... fileNames) {
        if (fileNames == null)
            return;
        for (String fileName : fileNames) {
            File file = new File(GetExternalFilePath(context, fileName));
            if (file.exists())
                file.delete();
        }
    }

    public static String GetAddressStringByAddrStr(Context context, String addrStr) {
        if (addrStr == null || addrStr.equals("") || addrStr.contains(",,,")) {
            return context.getString(R.string.no_address_info);
        }
        return addrStr;
    }

    public static String GetAddressByLatLng(Context context, Double lat, Double lng) {
        StringBuilder sb = new StringBuilder();
        if (lat == null || lng == null)
            return sb.toString();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                for (int i = 0; i < 4; i++) {
                    String addressLine = address.getAddressLine(i);
                    if (addressLine != null)
                        sb.append(address.getAddressLine(i) + ", ");
                }
                sb.replace(sb.length() - 2, sb.length(), "");
            }
            Log.d(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Lat: " + lat + "; Lng: " + lng + " Address: " + sb.toString());
        } catch (IOException e) {
            Log.e(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Error in Class MapService, method getAddressByLatLng: ", e);
            return GetAddressByGoogleMapAPI(lat, lng);
        }
        return sb.toString();
    }

    private static String GetAddressByGoogleMapAPI(Double lat, Double lng) {
        String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=false&language=en";
        try {
            URL url = new URL(googleMapUrl);
            JSONObject googleMapResponse = new JSONObject(ConvertInputStreamToString(url.openStream()));
            JSONArray results = (JSONArray) googleMapResponse.get("results");
            if (results != null && results.length() > 0) {
                JSONObject result = results.getJSONObject(0);
                if (result != null) {
                    String formattedAddress = result.getString("formatted_address");
                    Log.d(OcrDictConstants.LOG_WHERESK_ANDROID_TAG, "Lat: " + lat + "; Lng: " + lng + " Address: " + formattedAddress);
                    return formattedAddress;
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return "";
    }

    public static String ConvertInputStreamToString(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = in.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString();
    }

    public static boolean IsPhoneNumberRegisteredOnServer(String phoneNumber, Collection<String> registeredPhoneNumbers) {
        if (phoneNumber == null || registeredPhoneNumbers == null)
            return false;
        String phoneNumberWithoutSpecialCharacter = RemoveSpecialCharactersForPhoneNumber(phoneNumber);
        for (String registeredPhoneNumber : registeredPhoneNumbers) {
            if (registeredPhoneNumber == null)
                continue;
            String registeredPhoneNumberWithoutSpecialCharacter = RemoveSpecialCharactersForPhoneNumber(registeredPhoneNumber);
            if (phoneNumberWithoutSpecialCharacter.length() >= 8 && registeredPhoneNumberWithoutSpecialCharacter.length() >= 8) {
                if (phoneNumberWithoutSpecialCharacter.substring(phoneNumberWithoutSpecialCharacter.length() - 8, phoneNumberWithoutSpecialCharacter.length()).equals(registeredPhoneNumberWithoutSpecialCharacter.substring(registeredPhoneNumberWithoutSpecialCharacter.length() - 8, registeredPhoneNumberWithoutSpecialCharacter.length())))
                    return true;
            } else {
                if (phoneNumberWithoutSpecialCharacter.equals(registeredPhoneNumberWithoutSpecialCharacter))
                    return true;
            }
        }
        return false;
    }

    public static int GetStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String GetColorStringByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.equals(""))
            return MARKER_COLORS[0];
        return MARKER_COLORS[Math.abs(GetIntegerByPhoneNumber(phoneNumber)) % (MARKER_COLORS.length)];
    }

    public static int GetIntegerByPhoneNumber(String phoneNumber) {
        String phoneNumberWithoutSpecialCharacter = StringUtils.replace(RemoveSpecialCharactersForPhoneNumber(phoneNumber), PLUS, "");
        String finalPhoneNumber;
        if (phoneNumberWithoutSpecialCharacter != null && phoneNumberWithoutSpecialCharacter.length() > 8)
            finalPhoneNumber = phoneNumberWithoutSpecialCharacter.substring(phoneNumberWithoutSpecialCharacter.length() - 8, phoneNumberWithoutSpecialCharacter.length());
        else
            finalPhoneNumber = phoneNumberWithoutSpecialCharacter;
        try {
            return Integer.parseInt(finalPhoneNumber);
        } catch (NumberFormatException nfe) {
            return finalPhoneNumber.hashCode();
        }
    }

    public static boolean IsPhoneNumberEqual(String phoneNumber1, String phoneNumber2) {
        if (phoneNumber1 == null || phoneNumber2 == null)
            return false;
        if (phoneNumber1.equals(phoneNumber2))
            return true;
        String phoneNumber1WithoutSpecialCharacter = RemoveSpecialCharactersForPhoneNumber(phoneNumber1);
        String phoneNumber2WithoutSpecialCharacter = RemoveSpecialCharactersForPhoneNumber(phoneNumber2);
        if (phoneNumber1WithoutSpecialCharacter.length() >= 8 && phoneNumber2WithoutSpecialCharacter.length() >= 8) {
            if (phoneNumber1WithoutSpecialCharacter.substring(phoneNumber1WithoutSpecialCharacter.length() - 8).equals(phoneNumber2WithoutSpecialCharacter.substring(phoneNumber2WithoutSpecialCharacter.length() - 8)))
                return true;
        }
        return false;
    }

    public static String GetAppropriateFirstCharacterOfContactName(String contactName) {
        if (contactName == null || contactName.length() == 0)
            return "";
        if (contactName.length() == 1)
            return contactName;
        String firstTwoCharater = contactName.substring(0, 2);
        String firstCharacter = firstTwoCharater.substring(0, 1);
        String secondCharacter = firstTwoCharater.substring(1, 2);
        boolean isFirstTwoLatins = firstTwoCharater.matches("[A-Za-z]+");
        if (isFirstTwoLatins) {
            return firstCharacter.toUpperCase() + secondCharacter.toLowerCase();
        } else {
            return firstCharacter.toUpperCase();
        }
    }

    public static double GetDistLabelLocByDistanceMeters(double distanceMeters) {
        return distanceMeters / 2;
    }

    public static int GetActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static void ShowToast(Context context, String text, int miliSeconds) {
        final Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        new CountDownTimer(miliSeconds, 200) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }

    public static String EncodeBase64(String string) {
        try {
            return Base64.encodeToString(string.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String DecodeBase64(String string) {
        try {
            byte[] data = Base64.decode(string, Base64.DEFAULT);
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static void SavePublishImageToExtStore(Context context, Bitmap finalBitmap, String fname) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/" + context.getString(R.string.app_name));
        myDir.mkdirs();
        File file = new File(myDir, fname + OcrDictConstants.JPG_FILE_SUFFIX);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String paramAnonymousString, Uri paramAnonymousUri) {
            }
        });
    }

    static public String GetSimpleValueByValue(Context context, Integer value) {
        String simpleValueString;
        if (value < 0) {
            simpleValueString = "0";
        } else if (value < 1000) {
            simpleValueString = value.toString();
        } else {
            DecimalFormat df = new DecimalFormat(".0");
            df.setMaximumFractionDigits(1);
            simpleValueString = df.format(value / 1000) + " " + context.getString(R.string.kilo);
        }
        return simpleValueString;
    }

    static public String GetTextWithMoreIndicator(TextView textView, String text) {
        long maxVisibleChars = textView.getPaint().breakText(text, true, textView.getMeasuredWidth(), null);
        if (maxVisibleChars < text.length()) {
            text = text.substring(0, (int) maxVisibleChars - 3) + "...";
        }
        return text;
    }
}
