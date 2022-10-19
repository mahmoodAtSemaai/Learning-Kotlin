package com.webkul.mobikul.odoo.ui.learning_anko

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import org.jetbrains.anko.*
import timber.log.Timber

class LearningActivity : AppCompatActivity() {

	val END_POINT =
		"http://api.openweathermap.org/data/2.5/forecast/daily?APPID=15646a06818f61f7b8d7823ca833e1ce&q=94043&mode=json&units=metric&cnt=7"
	companion object{
		const val TAG = "LearningActivity"
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_learning)

		val recyclerView: RecyclerView = find(R.id.learning_anko)

		doAsync {
			val result = Request(END_POINT).run()
			uiThread {
				Timber.tag(TAG).d(result)
			}
		}
	}


}

