package allanksr.com.firebase

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Prefs {
    fun mCleanAllPrefs(ctx: Context){
        val editor = ctx.getSharedPreferences("ID", MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }

    /////////////////// Recuperar dados no SharedPreferences *******************************************
    fun getStringSP(ctx: Context, key: String): String {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        return preferences.getString(key, "")!!
    }
    fun getIntSP(ctx: Context, key: String): Int {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        return preferences.getInt(key, 0)
    }
    fun getFloatSP(ctx: Context, key: String): Float {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        return preferences.getFloat(key, .0f)
    }
    fun getBooleanSP(ctx: Context, key: String): Boolean {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        return preferences.getBoolean(key, false)
    }
    fun getLongSP(ctx: Context, key: String): Long {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        return preferences.getLong(key, 0L)
    }
    /////////////////// Gravar dados no SharedPreferences *******************************************
    fun setStringSP(ctx: Context, key: String, word: String?) {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, word)
        editor.apply()
    }
    fun setFloatSP(ctx: Context, key: String, word: Float) {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putFloat(key, word)
        editor.apply()
    }
    fun setIntSP(ctx: Context, key: String, number: Int) {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(key, number)
        editor.apply()
    }
    fun setLongSP(ctx: Context, key: String, number: Long?) {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putLong(key, number!!)
        editor.apply()
    }
    fun setBooleanSP(ctx: Context, key: String, mboolean: Boolean) {
        val preferences = ctx.getSharedPreferences("ID", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(key, mboolean)
        editor.apply()
    }


}
