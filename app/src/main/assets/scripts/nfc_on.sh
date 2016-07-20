#!/system/bin/sh

busybox mount -o remount,rw /system

sed -i 's/<CscFeature_NFC_StatusBarIconType>FALSE/<CscFeature_NFC_StatusBarIconType>ATT/g' /system/csc/feature.xml
