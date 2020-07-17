package com.example.akash.myhighway1.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.akash.myhighway1.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static int DEVICE_WIDTH = 0;
    public static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
    public static int ORIGINAL_IMAGE_WIDTH = -99;

    public static int getGridColumnsAsPerdeviceWidth(Activity activity, int gridItemWidth) {
        float scaleFactor = activity.getResources().getDisplayMetrics().density * gridItemWidth;
        int number = activity.getWindowManager().getDefaultDisplay().getWidth();
        return (int) ((float) number / scaleFactor);
    }

    public static int getDeviceWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public int getDeviceHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int getDeviceWidth1(Context context) {
        if (DEVICE_WIDTH < 0) {
            String name;
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            DEVICE_WIDTH = size.x;
        }
        return DEVICE_WIDTH;
    }

    public void hideSoftKeyboardIfOpen(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getAndroidId(Context context) {
        String aid = "";
        try {
            aid = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (aid == null) {
                aid = "No DeviceId";
            } else if (aid.length() <= 0) {
                aid = "No DeviceId";
            }
        } catch (Exception e) {
            e.printStackTrace();
            aid = "No DeviceId";
        }
        return aid;
    }


    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "-"; //not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    public static boolean isConnectingToInternet(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo != null && networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
            return false;
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

                for (NetworkInfo tempNetworkInfo : networkInfos) {
                    if (tempNetworkInfo.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getPhoneNumber(Context context) {
        String phoneNumber = null;

        try {
            TelephonyManager e = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //phoneNumber = e.getLine1Number(); remove comment if all permission checked
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return phoneNumber;
    }


    public static void makeCall(Context context, String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return;
        }

        boolean hasCallFeature = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        if (!hasCallFeature) {
            return;
        }

        try {
            if (!mobile.startsWith("+91") && !mobile.startsWith("0")) mobile = "+91" + mobile;
            Uri number = Uri.parse("tel:" + mobile);
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            context.startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();
            // Ignore if user has some call problems
        }
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void openAppPageInPlayStore(Context context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void openRequestPermissionSettings(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getUniqueId() {
        UUID idOne = UUID.randomUUID();
        return MD5(idOne.toString());
    }

    public static String MD5(String text) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            byte[] array = e.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 255 | 256).substring(1, 3));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException var5) {
            return null;
        }
    }


    public static String getFormatedFromLong(long value) {
        double dbValue = Double.parseDouble(String.valueOf(value));
        return getAbbreviateNumberInt(dbValue, 0);
    }

    public static String getAbbreviateNumberInt(double value, int iteration) {

        if (value >= 1000) {
            char[] c = new char[]{'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y'};
            double d = ((long) value / 100) / 10.0;
            boolean isRound = (d * 10) % 10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
            return (d < 1000 ? //this determines the class, i.e. 'k', 'm' etc
                    ((d > 99.9 || isRound || (!isRound && d > 9.99) ? //this decides whether to trim the decimals
                            (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                    ) + "" + c[iteration])
                    : getAbbreviateNumberInt(d, iteration + 1));
        } else {
            return String.format("%d", (int) value);
        }
    }

/*        public static Device getDeviceInfo(Context context) {
        Device device = new Device();
        device.setDeviceId(getAndroidId(context));
        device.setIMEI(com.anantapps.anantframework.device.Device.getDeviceId(context));
        device.setManufacturer(com.anantapps.anantframework.device.Device.getBrand());
        device.setPhoneModel(com.anantapps.anantframework.device.Device.getPhoneModel());
        device.setOS(com.anantapps.anantframework.device.Device.getOSInString());
        device.setCarrier(com.anantapps.anantframework.device.Device.getCarrier(context));
        device.setAvailableMemoryInMB(com.anantapps.anantframework.device.Device.availableMemoryInMB(context));
        device.setTotalMemoryInMB(com.anantapps.anantframework.device.Device.totalMemoryInMB(context));
        device.setPhoneNumber(getPhoneNumber(context));
        device.setConnection(getNetworkClass(context));
        device.setAppVersion(BuildConfig.VERSION_NAME);
        device.setAppVersionNumber(BuildConfig.VERSION_CODE);
        return device;

    }*/

    public static void openMarketLink(Activity context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Pattern PATTERN =
            Pattern.compile("__w-((?:-?\\d+)+)__");

    public static String getUrl(Context context, String model, int width) {
        if (!TextUtils.isEmpty(model)) {
            Matcher m = PATTERN.matcher(model);
            int bestBucket = 0;
            if (m.find()) {
                if (width != ORIGINAL_IMAGE_WIDTH) {
                    String[] found = m.group(1).split("-");
                    if (width <= 0) width = getDeviceWidth(context);
                    for (String bucketStr : found) {
                        bestBucket = Integer.parseInt(bucketStr);
                        if (bestBucket >= width) {
                            // the best bucket is the first immediately
                            // bigger than the requested width
                            break;
                        }
                    }
                }
                if (bestBucket > 0) {
                    model = m.replaceFirst("w" + bestBucket);
                } else model = model.replaceFirst("__w-((?:-?\\d+)+)__", "original");
            }
        }
        return model;
    }

    public static String getLocationImageUrl(Number latitude, Number longitude, int size) {
        return getLocationImageUrl(latitude, longitude, size, size);
    }

    public static String getLocationImageUrl(Number latitude, Number longitude, int width, int height) {
        return "https://maps.googleapis.com/maps/api/staticmap?"
                + "markers=color:red%7C"
                + latitude + "," + longitude
                + "&zoom=18&size=" + width + "x" + height;
    }


    public static Intent getLocationMapIntent(Context context, String mapLink) {
//        String label = null;
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocationName(mapLink, 1);
//            if (!addresses.isEmpty()) {
//                label = addresses.get(0).getFeatureName();
//                if (label == null) addresses.get(0).getAddressLine(0);
//            }
//        } catch (IOException | IndexOutOfBoundsException e) {
//            e.printStackTrace();
//        }
//        String uriString = "geo:<" + mapLink + ">?q=<" + mapLink + ">";
//        if (!TextUtils.isEmpty(label)) uriString += "(" + label + ")";

        Uri gmmIntentUri = Uri.parse(mapLink);
        return new Intent(Intent.ACTION_VIEW, gmmIntentUri);
    }
}
