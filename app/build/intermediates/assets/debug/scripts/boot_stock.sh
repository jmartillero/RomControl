#!/system/bin/sh

mount -o rw,remount /system

cp -p /system/romcontrol/files/BootAnimations/Stock/bootanimation /system/bin/bootanimation
cp -p /system/romcontrol/files/BootAnimations/Stock/bootsamsung.qmg /system/media/bootsamsung.qmg
cp -p /system/romcontrol/files/BootAnimations/Stock/bootsamsungloop.qmg /system/media/bootsamsungloop.qmg
cp -p /system/romcontrol/files/BootAnimations/Stock/shutdown.qmg /system/media/shutdown.qmg
