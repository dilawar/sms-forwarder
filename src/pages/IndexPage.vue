<template>
  <q-page padding>
    <battery-optimization-component title="Battery optimization" />
    <forwarding-rules />

    <!-- Result section -->
    <div class="text-h6 q-py-sm">Live Result</div>

    <div class="justify">
      Total SMS processed <strong> {{ numMsgReceived }} </strong>
    </div>

    <!-- These are some buttons for testing. Remove them in release  -->
    <div v-if="true">
      <div class="text-h6 q-py-sm">Plugin Debugging</div>
      <div margin class="q-gutter-sm items-start">
        <q-btn @click="echoFromPlugin">Echo from plugin</q-btn>
        <q-btn @click="readLiveSms">Read new SMS</q-btn>
        <q-input v-model="query" label="Query"></q-input>
        <q-btn @click="sendQuery">Submit</q-btn>
        <div>
          {{ queryResult }}
        </div>
      </div>
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
import { type Message } from '../js/types';

// Query for searching SMS
const query = ref('');
const queryResult: Ref<Message[]> = ref([]);

/* Total number of sms read.  */
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
  if (!result) {
    console.info('Result is empty.');
    return;
  }

  try {
    numMsgReceived.value += result.length;
  } catch (e) {
    /* handle error */
    console.error(
      'Failed to parse messages: Error is ' +
        JSON.stringify(e) +
        '. Result is ' +
        JSON.stringify(result)
    );
  }
  console.debug('Got sms from plugin:', JSON.stringify(result));
};

const sendQuery = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    console.debug('This plugin is only supported on android.');
    return false;
  }
  const q = query.value;
  console.info(`Quering '${q}'.`);
  const { result } = await Sms.querySms({ query: q });
  queryResult.value = result;
  console.info('Got sms from plugin for query: ' + JSON.stringify(result));
};

onUnmounted(async () => {
  if (readSmsLoop) {
    console.info('removing timer that reads sms');
    await clearIntervalAsync(readSmsLoop);
  }
});
</script>
