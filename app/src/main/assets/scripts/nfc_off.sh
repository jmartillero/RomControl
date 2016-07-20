#!/system/bin/sh

busybox mount -o remount,rw /system

sed -i 's/<CscFeature_NFC_StatusBarIconType>ATT/<CscFeature_NFC_StatusBarIconType>FALSE/g' /system/csc/feature.xml
