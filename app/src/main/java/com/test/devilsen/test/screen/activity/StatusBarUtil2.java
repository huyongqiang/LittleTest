package com.test.devilsen.test.screen.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * desc : 状态栏颜色设置工具
 * date : 2019-11-08 15:40
 *
 * @author : dongSen
 */
public class StatusBarUtil2 {

    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";    //OPPO
    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";     //小米
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";       //华为
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";   //锤子
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";          //vivo

    /**
     * 设置状态栏透明度、背景颜色、文字颜色
     *
     * @param isTranslate 是否透明，若为true，则bgColor设置无效，且Activity会占满整个屏幕
     * @param isDarkText  字体颜色，只有黑白两色，无论什么色值，都只会转为黑白两色
     * @param bgColor     背景色，即状态栏颜色，isTranslate若为true则此值无效
     */
    public static void setStatusColor(Activity activity, boolean isTranslate, boolean isDarkText, @ColorRes int bgColor) {
        //如果系统为6.0及以上，就可以使用Google自带的方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStandardColor(activity, isTranslate, isDarkText, bgColor);
        } else {
            if (isColorOS_3()) { //如果是OPPO Color3.0 & Android 5.1
                setColorOSColor(activity, isTranslate, isDarkText, bgColor);
            } else if (isMIUI6Later()) { //如果是Android 6.0以下，MIUI6及以上
                setMIUI6Translate(activity, isTranslate);
                setMIUI6StatusBarDarkMode(activity, isDarkText);
            } else if (isFlyme4Later()) {
                darModeForFlyme4(activity, isDarkText);
                if (isTranslate) {
                    setStatusTranslate(activity);
                }
            } else {
                //既不属于MIUI6,也不属于OPPO，还不属于魅族，极有可能是华为、三星、索尼、诺基亚、VIVO、锤子、360等
                if (isTranslate) {
                    setStatusTranslate(activity);
                }
//                setStatusBarColor(activity, isTranslate ? Color.TRANSPARENT : ContextCompat.getColor(activity, bgColor));
            }
        }
    }

    /**
     * 使用Android标准API设置系统状态栏颜色
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void setStandardColor(Activity activity, boolean isTranslate, boolean isDarkText, @ColorRes int bgColor) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.setSystemUiVisibility((isTranslate ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN : 0) | (isDarkText ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0));
        window.setStatusBarColor(isTranslate ? Color.TRANSPARENT : ContextCompat.getColor(activity, bgColor));
    }


    /**
     * 设置Color OS系统状态栏颜色
     */
    private static void setColorOSColor(Activity activity, boolean isTranslate, boolean isDarkText, int bgColor) {
        //控制字体颜色，只有黑白两色
        final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = isDarkText ? 0x00000010 : 0x00190000;
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility((isTranslate ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN : 0) | SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT);
        //可有可无
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(isTranslate ? Color.TRANSPARENT : ContextCompat.getColor(activity, bgColor));
        }
    }

    /**
     * 是否是MIUI6及以后版本
     *
     * @return 是否是MIUI6
     */
    private static boolean isMIUI6Later() {
        try {
            Class<?> cla = Class.forName("android.os.SystemProperties");
            Method mtd = cla.getMethod("get", String.class);
            String val = (String) mtd.invoke(null, KEY_VERSION_MIUI);
            val = val.replaceAll("[vV]", "");
            int version = Integer.parseInt(val);
            return version >= 6;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置MIUI6及以上状态栏透明,字体为默认白色，Android6.0以上也可以
     *
     * @param on 是否为透明
     */
    private static void setMIUI6Translate(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置MIUI6及以上，Android6.0以下版本状态栏黑色字符
     * Android 6.0以上此方法无效
     * 此方法是官方给的，应该没有错，我没有MIUI6的手机，无法测试
     */
    private static void setMIUI6StatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是ColorOS_3.0系统 Android 5.1
     *
     * @return 是否是colorOS_3.0
     */
    private static boolean isColorOS_3() {
        try {
            Class<?> cla = Class.forName("android.os.SystemProperties");
            Method mtd = cla.getMethod("get", String.class);
            String val = (String) mtd.invoke(null, KEY_VERSION_OPPO);
            val = val.replaceAll("[vV]", "");
            val = val.substring(0, 1);
            int version = Integer.parseInt(val);
            return version >= 3;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否是魅族Flyme4
     *
     * @return 是否是魅族Flyme4
     */
    private static boolean isFlyme4Later() {
        if ("MEIZU".equals(Build.BRAND.trim().toUpperCase())) {
            return Build.FINGERPRINT.contains("Flyme_OS_4") || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4") || Pattern.compile("Flyme_OS_[4|5]", Pattern.CASE_INSENSITIVE)
                    .matcher(Build.DISPLAY).find();
        }
        return false;
    }


    /**
     * 判断是否是HTC
     *
     * @return 是否是魅族Flyme4
     */
    private static boolean isHtc() {
        if ("htc".equals(Build.BRAND.trim().toUpperCase())) {
            return Build.FINGERPRINT.contains("Flyme_OS_4") || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4") || Pattern.compile("Flyme_OS_[4|5]", Pattern.CASE_INSENSITIVE)
                    .matcher(Build.DISPLAY).find();
        }
        return false;
    }

    /**
     * 设置魅族Flyme4以后 状态栏黑色字体
     * 没有手机，从网上找的方法，应该没问题
     *
     * @param dark 是否黑色
     */
    private static void darModeForFlyme4(Activity activity, boolean dark) {
        try {
            WindowManager.LayoutParams e = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(e);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(e, value);
            activity.getWindow().setAttributes(e);
        } catch (Exception var8) {
            Log.e("StatusBar", "darkIcon: failed");
        }
    }

    /**
     * 设置状态栏透明，Android4.4、Android5.0以上方法不一
     * 我感觉5.0以下的系统已经很少了吧，应该不需要适配了吧
     * 如果只是设置状态栏透明，就使用这个就好了
     */
    public static void setStatusTranslate(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置状态栏背景颜色，不适配5.0以下系统（这里的效果并不好，不建议用白色底）
     *
     * @param color 颜色值
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 设置全屏
     */
    public static void setFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //TRANSLUCENT 半透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置为透明
            window.setStatusBarColor(Color.TRANSPARENT);

            int visibility = window.getDecorView().getVisibility();
            //全屏展示
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //隐藏导航栏
//            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            //防止内容区域大小发生变化
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            window.getDecorView().setSystemUiVisibility(visibility);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏的高度
     */
    private int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    /**
     * 为ToolBar设置间距
     *
     * @param view toolbar或其他位于顶端的view
     */
    private void setHeightAndPadding(Context context, View view) {
        int statusBarHeight = getStatusBarHeight(context);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height += statusBarHeight;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + statusBarHeight, view.getPaddingRight(), view.getPaddingBottom());
    }
}
