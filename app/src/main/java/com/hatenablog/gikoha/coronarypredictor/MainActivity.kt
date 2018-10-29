/*
 * Copyright (c) gikoha 2018.
 * このソフトウェアは、 Apache 2.0ライセンスで配布されている製作物が含まれています。
 * This software includes the work that is distributed in the Apache License 2.0
 *
 */
package com.hatenablog.gikoha.coronarypredictor

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
    val titles = arrayOf("腎不全", "心房細動", "末梢血管障害", "貧血Hb<11", "年齢>=75", "心不全", "糖尿病", "CTO",
            "Plt低下<10万", "MIの既往", "悪性腫瘍")
    val risk_thrombus = arrayOf(2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0)
    val risk_bleed = arrayOf(2, 1, 2, 0, 0, 2, 0, 0, 2, 1, 1)
    val severities = arrayOf("低リスク", "中等度リスク", "高リスク")
    val percentage_3years_thrombus = arrayOf(2.4, 3.7, 7.6)
    val percentage_3years_bleed = arrayOf(2.3, 4.1, 8.8)
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

            val lp = RelativeLayout.LayoutParams(convertDpToPx(context, 100), convertDpToPx(context, 20))
            lp.leftMargin = convertDpToPx(context, 10 + (i % 2) * 140)
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
            var s_thrombus = 0
            var s_bleed = 0
            var r_thrombus = 0
            var r_bleed = 0

            for (cb in cbarray)
            {
                if (cb.isChecked)
                {
                    s_thrombus += risk_thrombus[i]
                    s_bleed += risk_bleed[i]
                }

                i++
            }
            when (s_thrombus)
            {
                in 0..1 -> r_thrombus = 0
                in 2..3 -> r_thrombus = 1
                else -> r_thrombus = 2
            }
            when (s_bleed)
            {
                0 -> r_bleed = 0
                in 1..2 -> r_bleed = 1
                else -> r_bleed = 2
            }


            thrombusRiskText.text = s_thrombus.toString() + "pt " + severities[r_thrombus]
            bleedRiskText.text = s_bleed.toString() + "pt " + severities[r_bleed]

            cum3ythrombusField.text = percentage_3years_thrombus[r_thrombus].toString() + "%"
            cum3ybleedField.text = percentage_3years_bleed[r_bleed].toString() + "%"
        }
    }
}
