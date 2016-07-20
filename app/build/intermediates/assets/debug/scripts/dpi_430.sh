#!/system/bin/sh

busybox mount -o remount,rw /system

sed -i -e "/ro.sf.lcd_density/d" /system/build.prop

echo "ro.sf.lcd_densit=430" >> /system/build.prop
