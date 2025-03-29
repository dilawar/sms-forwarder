export ANDROID_HOME:=$(HOME)/Android/Sdk
export ANDROID_SDK_ROOT:=$(HOME)/Android/Sdk

PATH:=$(PATH):$(ANDROID_SDK_ROOT)/tools; 
export PATH:=$(PATH):$(ANDROID_SDK_ROOT)/platform-tools

run:
	echo ANDROID_HOME=$(ANDROID_HOME)
	yarn quasar build -m capacitor -T android
	# yarn quasar dev -m capacitor -T android

bootstrap:
	yarn quasar mode add capacitor
