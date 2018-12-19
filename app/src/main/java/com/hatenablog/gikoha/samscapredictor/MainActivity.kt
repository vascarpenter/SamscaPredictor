/*
 * Copyright (c) gikoha 2018.
 * このソフトウェアは、 Apache 2.0ライセンスで配布されている製作物が含まれています。
 * This software includes the work that is distributed in the Apache License 2.0
 */
package com.hatenablog.gikoha.samscapredictor

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()
{
    val titles = arrayOf("Hb<10", "UA>7.2", "lasix>=20/日", "Hct<31.6%", "LAVI>44.7", "eGFR<50",
            "糖尿病", "年齢>=80", "LVEF<40", "退院<6ヶ月")
    val risk_rehosp = arrayOf(1, 1, 1, 1, 1, 1, 2, 2, 2, 3)
    val percentage_rehosp = arrayOf(6.5, 8.5, 19.3, 42.1, 56.2)
    val cbarray: MutableList<CheckBox> = mutableListOf()

    fun convertDpToPx(context: Context, dp: Int): Int
    {
        val d = context.resources.displayMetrics.density
        return (dp * d + 0.5).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // Do any additional setup after loading the view, typically from a nib.

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        val context = applicationContext
        var i = 0
        for (title in titles)
        {
            val cbox = CheckBox(context)
            cbox.isChecked = false
            cbox.text = title

            val lp = RelativeLayout.LayoutParams(convertDpToPx(context, 120), convertDpToPx(context, 20))
            lp.leftMargin = convertDpToPx(context, 10 + (i % 2) * 150)
            lp.topMargin = convertDpToPx(context, 70 + (i / 2) * 30)
            mainLayout.addView(cbox, lp)
            cbarray.add(cbox)
            i++
        }
        calculateButton.setOnClickListener {
            // hide input method window
            var im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            var i = 0
            var s_rehosp = 0

            for (cb in cbarray)
            {
                if (cb.isChecked)
                {
                    s_rehosp += risk_rehosp[i]
                }

                i++
            }

            rehospRiskText.text = s_rehosp.toString() + "pt "
            var r_group = 0
            when (s_rehosp)
            {
                0 -> r_group = 0
                in 1..3 -> r_group = 1
                in 4..6 -> r_group = 2
                in 7..9 -> r_group = 3
                else -> r_group = 4
            }

            rehospPercentField.text = percentage_rehosp[r_group].toString() + "%"
        }
    }
}
