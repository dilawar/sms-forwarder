<template>
  <div v-if="batteryOptmization">
    <p>
      This application under battery optimization. For this app to work properly in the background,
      please disable the battery optimization.
    </p>

    <div class="row q-px-sm">
      <q-btn class="col" @click="(_) => openBatteryOptimizationSettings()">
        Open Bettery Settings
      </q-btn>
      <q-btn class="col" @click="(_) => requestIgnoreBatteryOptimization()">
        Ignore Battery Optimization
      </q-btn>
    </div>
  </div>
  <div v-else>
    <p>This app is not under battery optimization. Yay!</p>
  </div>

  <div v-if="!smsPermission">
    <p>This application needs SMS read/send permissions.</p>

    <div class="row q-px-sm">
      <q-btn class="col" @click="requestSmsPermissions"> Request SMS Permissions </q-btn>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Capacitor } from '@capacitor/core';
import { BatteryOptimization } from '@capawesome-team/capacitor-android-battery-optimization';
import Sms from '../plugins/sms';

defineProps({
  title: String,
});

const batteryOptmization = ref(false);
const smsPermission = ref(false);

onMounted(async () => {
  batteryOptmization.value = await isBatteryOptimizationEnabled();
});

const isBatteryOptimizationEnabled = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    return false;
  }
  const { enabled } = await BatteryOptimization.isBatteryOptimizationEnabled();
  return enabled;
};

const openBatteryOptimizationSettings = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    return;
  }
  await BatteryOptimization.openBatteryOptimizationSettings();
};

const requestSmsPermissions = async () => {
  console.info('Requesting SMS permissions');
  await Sms.requestPermissions();
};

const requestIgnoreBatteryOptimization = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    return;
  }
  await BatteryOptimization.requestIgnoreBatteryOptimization();
  // refresh the component.
  const router = useRouter();
  router.go(0);
};
</script>
