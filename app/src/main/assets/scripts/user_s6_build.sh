#!/system/bin/sh

cp -p /system/romcontrol/files/buildprop/user_s6_build.prop /system/build.prop

pkill zygote
