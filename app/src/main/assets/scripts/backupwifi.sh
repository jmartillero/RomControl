#!/system/bin/sh

mkdir /sdcard/backups_romcontrol
mkdir /sdcard/backups_romcontrol/wifi
cp -rf /data/misc/wifi/wpa_supplicant.conf /sdcard/backups_romcontrol/wifi/wpa_supplicant.conf
