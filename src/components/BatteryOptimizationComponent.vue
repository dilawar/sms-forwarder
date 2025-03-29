<template>
  <q-page class="q-px-sm">
    <div class="text-h6">{{ title }}</div>
    <div class="q-pa-sd row">
      <q-btn
        class="col"
        v-if="!batteryOptmization"
        @click="(_) => openBatteryOptimizationSettings()"
      >
        Open Bettery Settings
      </q-btn>
      <q-btn class="col" @click="(_) => requestIgnoreBatteryOptimization()">
        Ignore Battery Optimization
      </q-btn>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { Capacitor } from '@capacitor/core';
import { BatteryOptimization } from '@capawesome-team/capacitor-android-battery-optimization';

defineProps({
  title: String,
});

const batteryOptmization = ref(false);

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

const requestIgnoreBatteryOptimization = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    return;
  }
  await BatteryOptimization.requestIgnoreBatteryOptimization();
};
</script>
