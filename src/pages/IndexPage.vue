<template>
  <q-page class="row q-pa-sm">
    <div>
      <battery-optimization-component title="Battery optimization" />
      <forwarding-rules />
    </div>
    <div>
      <q-btn @click="echoFromPlugin">Echo From Plugin</q-btn>
    </div>
    <div>
      <q-btn @click="readLiveSms">Read live message</q-btn>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { Capacitor } from '@capacitor/core';
import BatteryOptimizationComponent from 'components/BatteryOptimizationComponent.vue';
import ForwardingRules from 'components/ForwardingRules.vue';
import Sms from '../plugins/sms';

const echoFromPlugin = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    console.debug('Not android');
    return false;
  }
  const { value } = await Sms.echo({ value: 'Hello from JS' });
  console.debug('response from native:', value);
};

const readLiveSms = async () => {
  if (Capacitor.getPlatform() !== 'android') {
    console.debug('Not android');
    return false;
  }
  const { result } = await Sms.getLiveSms();
  console.debug('live sms:', result);
};
</script>
