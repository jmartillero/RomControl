#!/system/bin/sh

busybox mount -o remount,rw /system

sed -i -e "/ro.sf.lcd_density/d" /system/build.prop

echo "ro.sf.lcd_densit=480" >> /system/build.prop
