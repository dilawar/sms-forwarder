export ANDROID_HOME:=$(HOME)/Android/Sdk
export ANDROID_SDK_ROOT:=$(HOME)/Android/Sdk

export PATH:=$(PATH):$(ANDROID_SDK_ROOT)/tools
export PATH:=$(PATH):$(ANDROID_SDK_ROOT)/platform-tools

dev:
	yarn quasar dev

android run_android: build_android
	yarn quasar dev -m capacitor -T android

build_android:
	echo ANDROID_HOME=$(ANDROID_HOME)
	yarn quasar build -m capacitor -T android && cd ./src-capacitor && npx cap run android

bootstrap:
	yarn quasar mode add capacitor
