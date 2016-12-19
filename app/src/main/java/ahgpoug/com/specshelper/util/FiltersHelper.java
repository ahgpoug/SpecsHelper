package ahgpoug.com.specshelper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class FiltersHelper {
    public static void clearCpuFilter(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cpuFilter.manufacturer", "");
        editor.putString("cpuFilter.codename", "");
        editor.putString("cpuFilter.socket", "Socket");
        editor.putString("cpuFilter.coresCountSpinner", "==");
        editor.putString("cpuFilter.coresCount", "");
        editor.putString("cpuFilter.clockSpinner", "==");
        editor.putString("cpuFilter.clock", "");
        editor.putString("cpuFilter.gpu", "Встроенный GPU");
        editor.putString("cpuFilter.processSpinner", "==");
        editor.putString("cpuFilter.process", "");
        editor.putString("cpuFilter.tdpSpinner", "==");
        editor.putString("cpuFilter.tdp", "");
        editor.putString("cpuFilter.releaseSpinner", "==");
        editor.putString("cpuFilter.release", "");
        editor.putString("cpuFilter.priceSpinner", "==");
        editor.putString("cpuFilter.price", "");
        editor.apply();
    }

    public static void clearMotherboardFilter(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mbFilter.manufacturer", "");
        editor.putString("mbFilter.codename", "");
        editor.putString("mbFilter.socket", "Socket");
        editor.putString("mbFilter.formFactor", "Форм-фактор");
        editor.putString("mbFilter.chipSet", "");
        editor.putString("mbFilter.ramType", "Тип RAM");
        editor.putString("mbFilter.maxRamCount", "");
        editor.putString("mbFilter.maxRamSize", "");
        editor.putString("mbFilter.maxRamClock", "");
        editor.putString("mbFilter.maxRamCountSpinner", "==");
        editor.putString("mbFilter.maxRamSizeSpinner", "==");
        editor.putString("mbFilter.maxRamClockSpinner", "==");
        editor.putString("mbFilter.sli", "Поддержка SLI");
        editor.putString("mbFilter.cfire", "Поддержка CrossFire");
        editor.putString("mbFilter.price", "");
        editor.putString("mbFilter.priceSpinner", "==");
        editor.apply();
    }

    public static void clearGpuFilter(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gpuFilter.manufacturer", "");
        editor.putString("gpuFilter.codename", "");
        editor.putString("gpuFilter.cClock", "");
        editor.putString("gpuFilter.mClock", "");
        editor.putString("gpuFilter.memorySize", "");
        editor.putString("gpuFilter.bus", "");
        editor.putString("gpuFilter.process", "");
        editor.putString("gpuFilter.slots", "");
        editor.putString("gpuFilter.price", "");
        editor.putString("gpuFilter.cClockSpinner", "==");
        editor.putString("gpuFilter.mClockSpinner", "==");
        editor.putString("gpuFilter.memorySizeSpinner", "==");
        editor.putString("gpuFilter.memoryType", "Тип памяти");
        editor.putString("gpuFilter.busSpinner", "==");
        editor.putString("gpuFilter.processSpinner", "==");
        editor.putString("gpuFilter.slotsSpinner", "==");
        editor.putString("gpuFilter.sli", "Поддержка SLI/CrossFire");
        editor.putString("gpuFilter.priceSpinner", "==");
        editor.apply();
    }

    public static void clearRamFilter(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ramFilter.manufacturer", "");
        editor.putString("ramFilter.codename", "");
        editor.putString("ramFilter.clock", "");
        editor.putString("ramFilter.memorySize", "");
        editor.putString("ramFilter.price", "");
        editor.putString("ramFilter.clockSpinner", "==");
        editor.putString("ramFilter.memorySizeSpinner", "==");
        editor.putString("ramFilter.type", "Тип памяти");
        editor.putString("ramFilter.priceSpinner", "==");
        editor.apply();
    }
}
