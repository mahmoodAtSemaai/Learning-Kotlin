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

package com.webkul.mobikul.broadcast_receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.ApplicationConstant.NOTIFICATION_CHANNEL_ABANDONED_CART
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.ui.cart.NewCartActivity


class AbandonedCartAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val REQUEST_CODE = 12345
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
//            BaseActivity.setLocale(context,false)
            val cartId = AppSharedPref.getCartId(context);
            //TODO: @Mahmood look into it. Unsure about cartIntent redirecting to NewHomeActivity
            val cartIntent: Intent = Intent(context, NewHomeActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


            val pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, cartIntent, PendingIntent.FLAG_ONE_SHOT)

            val icon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ABANDONED_CART)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.your_cart_misses_you))
                    .setContentText(context.getString(R.string.products_waiting_in_cart_msg))
                    .setLargeIcon(icon)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

            notificationBuilder.setStyle(NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.products_waiting_in_cart_msg))
                    .setBigContentTitle(context.getString(R.string.your_cart_misses_you)))

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL_ABANDONED_CART,NOTIFICATION_CHANNEL_ABANDONED_CART,NotificationManager.IMPORTANCE_DEFAULT))

            }
            notificationManager.notify(34155, notificationBuilder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}