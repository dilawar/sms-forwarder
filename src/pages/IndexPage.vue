<template>
  <q-page-container>
    <q-page padding class="row q-pa-sm justify-around" >
      <forwarding-rules />
      <!-- result section -->
      <div>
        Total SMS processed: <strong>{{ numMsgReceived }}</strong>
      </div>

      <!-- These are some buttons for testing. Remove them in release  -->
      <div class="row">
        <q-btn @click="echoFromPlugin">Echo From Plugin</q-btn>

        <q-btn @click="readLiveSms">Read live message</q-btn>

        <q-input v-model="query" /> <q-btn @click="sendQuery">Query SMS</q-btn>
      </div>
      <battery-optimization-component title="Battery optimization" />

      <!-- place QPageScroller at end of page -->
      <q-page-scroller position="bottom-left" :scroll-offset="150" :offset="[18, 18]">
        <q-btn fab icon="keyboard_arrow_up" color="accent" />
      </q-page-scroller>

    </q-page>
  </q-page-container>
</template>

<script setup lang="ts">
import { ref, type Ref, onUnmounted } from 'vue';
import { setIntervalAsync, clearIntervalAsync } from 'set-interval-async';
import { Capacitor } from '@capacitor/core';
import BatteryOptimizationComponent from 'components/BatteryOptimizationComponent.vue';
import ForwardingRules from 'components/ForwardingRules.vue';
import Sms from '../plugins/sms';

const query = ref('');

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

const sendQuery = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    console.debug('This plugin is only supported on android.');
    return false;
  }
  console.log('Quering ' + query.value);
  const { result } = await Sms.querySms({ query: query.value });
  console.debug('Got sms from plugin for query: ', JSON.stringify(result));
};

onUnmounted(async () => {
  if (readSmsLoop) {
    console.info('removing timer that reads sms');
    await clearIntervalAsync(readSmsLoop);
  }
});
</script>
