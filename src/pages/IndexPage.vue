<template>
  <q-page class="row q-pa-sm justify-around">
    <div>
      <battery-optimization-component title="Battery optimization" />
      <forwarding-rules />
    </div>

    <!-- result section -->
    <div>
      Total SMS processed: <strong>{{ numMsgReceived }}</strong>
    </div>

    <!-- These are some buttons for testing. Remove them in release  -->
    <div class="row justify-around fit">
      <q-btn @click="echoFromPlugin">Echo From Plugin</q-btn>
      <q-btn @click="readLiveSms">Read live message</q-btn>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, type Ref, onUnmounted } from 'vue';
import { setIntervalAsync, clearIntervalAsync } from 'set-interval-async';
import { Capacitor } from '@capacitor/core';
import BatteryOptimizationComponent from 'components/BatteryOptimizationComponent.vue';
import ForwardingRules from 'components/ForwardingRules.vue';
import Sms from '../plugins/sms';

/**
 * Total number of sms read.
 */
const numMsgReceived: Ref<number> = ref(0);

const readSmsLoop = setIntervalAsync(async () => {
  await readLiveSms();
}, 2000);

const echoFromPlugin = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    console.debug('This plugin is only supported on android.');
    return false;
  }
  const { value } = await Sms.echo({ value: 'Hello from JS' });
  console.debug('response from native:', value);
};

const readLiveSms = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    console.debug('This plugin is only supported on android.');
    return false;
  }
  const { result } = await Sms.getLiveSms();
  try {
    numMsgReceived.value += result.length;
  } catch (e) {
    /* handle error */
    console.error('Failed to parse messages: ' + JSON.stringify(e));
  }
  console.debug('Got sms from plugin:', JSON.stringify(result));
};

onUnmounted(async () => {
  if (readSmsLoop) {
    console.info('removing timer that reads sms');
    await clearIntervalAsync(readSmsLoop);
  }
});
</script>
