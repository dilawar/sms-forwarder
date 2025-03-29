<template>
  <div>
    <p>{{ title }}</p>
    <ul>
      <li v-for="todo in todos" :key="todo.id" @click="increment">
        {{ todo.id }} - {{ todo.content }}
      </li>
    </ul>
    <p>Count: {{ todoCount }} / {{ meta.totalCount }}</p>
    <p>Active: {{ active ? 'yes' : 'no' }}</p>
    <p>Clicks on todos: {{ clickCount }}</p>

    <p>Is battery optimization {{ batteryOptmization }} </p>

    <q-btn @click="_ => openBatteryOptimizationSettings()">
      Open Better Settings
    </q-btn>
    <q-btn @click="_ => requestIgnoreBatteryOptimization()">
      Requst Better Optimization
    </q-btn>

  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue';
import type { Todo, Meta } from './models';

import { Capacitor } from '@capacitor/core';
import { BatteryOptimization } from '@capawesome-team/capacitor-android-battery-optimization';

const batteryOptmization = ref(false)

onMounted(async () => {
  batteryOptmization.value = await isBatteryOptimizationEnabled()
})

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


interface Props {
  title: string;
  todos?: Todo[];
  meta: Meta;
  active: boolean;
};

const props = withDefaults(defineProps<Props>(), {
  todos: () => []
});

const clickCount = ref(0);
function increment() {
  clickCount.value += 1;
  return clickCount.value;
}

const todoCount = computed(() => props.todos.length);
</script>
