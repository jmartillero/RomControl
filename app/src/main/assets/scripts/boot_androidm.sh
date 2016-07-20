#!/system/bin/sh

mount -o rw,remount /system

cp -p /system/romcontrol/files/BootAnimations/AndroidM/bootanimation /system/bin/bootanimation
cp -p /system/romcontrol/files/BootAnimations/AndroidM/bootanimation.zip /system/media/bootanimation.zip
