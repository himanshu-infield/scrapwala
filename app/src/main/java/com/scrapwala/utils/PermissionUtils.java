package com.scrapwala.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.util.Arrays;

public class PermissionUtils {

    public static final int PERMISSION_REQUEST_CODE = 1;

    public static String[] MEDIA_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };


    public static String[] CALL_PERMISSION = {
            Manifest.permission.CALL_PHONE,

    };

    public static String[] MEDIA_PERMISSIONS_13_PLUS = {
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA,
    };

    public static String[] IMAGE_CHOOSER_PERMISSIONS_14_PLUS = {
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
            Manifest.permission.CAMERA,
    };

    public static String[] getReadWritePermissionAccordingToSdk() {
        int version = Build.VERSION.SDK_INT;

        if(version >= 34) {
            return IMAGE_CHOOSER_PERMISSIONS_14_PLUS;
        } else if(version >= 33) {
            return MEDIA_PERMISSIONS_13_PLUS;
        } else {
            return MEDIA_PERMISSIONS;
        }
    }


    // ========== PHONE_CALL/RECORDING PERMISSIONS ===========
    /*public static boolean hasPermissionGranted(Activity activity) {
        if (!PermissionUtils.hasPhoneCallPermissions(activity)) {
            PermissionUtils.shouldShowRequestPermission(activity, PHONE_CALL_PERMISSIONS);
            return false;
        }

        *//*if (!PermissionUtils.hasRecordingPermissions(activity)) {
            PermissionUtils.shouldShowRequestPermission(activity, RECORDING_PERMISSION);
            return false;
        }*//*

        return true;
    }*/

    /*private static boolean hasRecordingPermissions(Context context*//*, String... permissions*//*) {

        // If android sdk version is less than marshmallow, return true;
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        int currentCountryCode = PhoneUtils.getCurrentCountryCode(context);
        //System.out.println("----current country code ---->" + currentCountryCode);
        // In case of '1' and '61', we can not force user to accept 'recording' permission.
        if (currentCountryCode == 1 || currentCountryCode == 61) {
            return true;
        }

        if (context != null && RECORDING_PERMISSION != null) {
            for (String permission : RECORDING_PERMISSION) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }*/

    /*private static boolean hasPhoneCallPermissions(Context context*//*, String... permissions*//*) {

        // If android sdk version is less than marshmallow, return true;
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        if (context != null && PHONE_CALL_PERMISSIONS != null) {
            for (String permission : PHONE_CALL_PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }*/

    public static void hasLocationPermissions(Activity activity) {
        try {
            final int finePermissionCheck = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            final int coarsePermissionCheck = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (finePermissionCheck != PackageManager.PERMISSION_GRANTED
                    && coarsePermissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            10002);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======= OTHER PERMISSIONS =====================
    public static boolean hasPermissionGranted(Activity activity, String... permissions) {
        if (!PermissionUtils.hasPermissions(activity, permissions)) {
            PermissionUtils.shouldShowRequestPermission(activity, permissions);
            return false;
        }

        return true;
    }

    private static boolean hasPermissions(Context context, String... permissions) {

        // If android sdk version is less than marshmallow, return true;
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void shouldShowRequestPermission(Activity mActivity, String... permissions) {
        // Do something, when permissions not granted
        if (shouldShowRequestDialog(mActivity, permissions)) {
            ActivityCompat.requestPermissions(
                    mActivity,
                    permissions,
                    PERMISSION_REQUEST_CODE
            );
            // Show an alert dialog here with request explanation
            /* AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage(formattedPermissions(permissions));
            builder.setTitle("Please grant these permissions");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(
                            mActivity,
                            permissions,
                            PERMISSION_REQUEST_CODE
                    );
                }
            });
            builder.setNeutralButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                    intent.setData(uri);
                    mActivity.startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();*/
        } else {
            // Directly request for required permissions, without explanation
            ActivityCompat.requestPermissions(
                    mActivity,
                    permissions,
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    private static boolean shouldShowRequestDialog(Activity mActivity, String... permissions) {
        // ActivityCompat.shouldShowRequestPermissionRationale ->
        // This method returns true if the user has previously denied the request.
        // But if the user has selected “Don't’ ask again” this method would always false .
        // It also returns false if we are prompting for permission for the first time.
        if (mActivity != null && permissions != null) {
            for (String permission : permissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String formattedPermissions(String... permissions) {
        String result = "";
        try {
            StringBuilder message = new StringBuilder();
            for (String per : permissions) {
                String[] arr = per.split("\\.");
                message.append(arr[arr.length - 1]);
                message.append("\n");
            }
            result = message.toString();
        } catch (Exception e) {
            result = Arrays.toString(permissions);
        }

        return result;
    }
}