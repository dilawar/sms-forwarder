<template>
  <q-page class="q-px-sm">
    <div v-if="batteryOptmization">
      <p>
        This application under battery optimization. For this app to work properly in the
        background, please disable the battery optimization.
      </p>
      <div class="row">
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

    <q-btn @click="echoFromPlugin">Echo From Plugin</q-btn>
  </q-page>
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

onMounted(async () => {
  batteryOptmization.value = await isBatteryOptimizationEnabled();
});

const echoFromPlugin = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    console.debug('Not android');
    return false;
  }
  const { value } = await Sms.echo({ value: 'Hello from JS' });
  console.debug('response from native:', value);
};

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
  // refresh the component.
  const router = useRouter();
  router.go(0);
};
</script>
