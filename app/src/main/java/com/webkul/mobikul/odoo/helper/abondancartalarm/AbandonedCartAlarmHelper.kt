/*
 * Webkul Software.
 *
 * Kotlin
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

package com.webkul.mobikul.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.webkul.mobikul.broadcast_receivers.AbandonedCartAlarmReceiver
import com.webkul.mobikul.odoo.constant.ApplicationConstant.DEFAULT_TIME_FOR_ABANDONED_CART_NOTIFICATION

class AbandonedCartAlarmHelper {

  public  companion object {

      @JvmStatic
       public fun scheduleAlarm(context: Context) {
            try {
                Log.d("neeraj", "onAppBackgrounded ")

                val pIntent = PendingIntent.getBroadcast(context, AbandonedCartAlarmReceiver.REQUEST_CODE,
                        Intent(context.applicationContext, AbandonedCartAlarmReceiver::class.java), PendingIntent.FLAG_CANCEL_CURRENT)
                val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DEFAULT_TIME_FOR_ABANDONED_CART_NOTIFICATION, pIntent)
                Log.d("DEBUG", "scheduleAlarm: started ")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

      @JvmStatic
        fun cancelAlarm(context: Context) {
            try {
                val pIntent = PendingIntent.getBroadcast(context, AbandonedCartAlarmReceiver.REQUEST_CODE,
                        Intent(context.applicationContext, AbandonedCartAlarmReceiver::class.java), PendingIntent.FLAG_CANCEL_CURRENT)
                val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarm.cancel(pIntent)
                Log.d("DEBUG", "cancelAlarm: canceled ")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}