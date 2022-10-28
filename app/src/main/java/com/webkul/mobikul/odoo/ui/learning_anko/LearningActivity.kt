package com.webkul.mobikul.odoo.ui.learning_anko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.ui.learning_anko.domain.commands.RequestForecastCommand
import com.webkul.mobikul.odoo.ui.learning_anko.kotlin_learnings.ManualLazy
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import timber.log.Timber
import kotlin.properties.Delegates

class LearningActivity : AppCompatActivity() {

	val END_POINT =
		"http://api.openweathermap.org/data/2.5/forecast/daily?APPID=15646a06818f61f7b8d7823ca833e1ce&q=94043&mode=json&units=metric&cnt=7"
	companion object{
		const val TAG = "LearningActivity"
	}
	private val recyclerView: RecyclerView by lazy { find(R.id.learning_anko) }
	private var counterByObservable by Delegates.observable(0){
		kd, old, new ->
		Timber.tag(TAG).d("Old = $old, New = $new, KD = $kd")
	}

	private var counterByVetoable by Delegates.observable(0){
			kd, old, new ->
		if(new >= 5)
			Timber.tag(TAG).d("Old = $old, New = $new, KD = $kd")
		else
			Timber.tag(TAG).d("Throw Error")
	}


	private val counterButton : FloatingActionButton by ManualLazy {
		find(R.id.btn_simple)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_learning)

		val recyclerView: RecyclerView = find(R.id.learning_anko)

		doAsync {
			val result = RequestForecastCommand("78097").execute()
			uiThread {

			}
		}

		counterButton.setOnClickListener {
/*
			counterByObservable++
*/
			counterByVetoable++
		}
	}


}

