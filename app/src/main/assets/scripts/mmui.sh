#!/system/bin/sh

mount -o rw,remount /system

cp -p /system/romcontrol/files/SystemUI/SystemUI_mm.apk /system/priv-app/SystemUI/SystemUI.apk

pkill com.android.systemui
