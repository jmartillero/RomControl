#!/system/bin/sh

cp -p /system/romcontrol/files/buildprop/user_s4_build.prop /system/build.prop

pkill zygote
