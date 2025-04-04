<template>
  <h5>Forwaring rules</h5>

  <q-stepper v-model="step" vertical color="primary" animated>
    <q-step :name="1" title="Add rules for SMS forwarding" icon="settings" :done="step > 1">
      Write a glob pattern that SMS must contain.
      <q-input v-model="rule.glob" placeholder="*" label="glob that SMS must contain"> </q-input>

      <q-stepper-navigation>
        <q-btn @click="step = 2" color="primary" label="Continue" />
      </q-stepper-navigation>
    </q-step>

    <q-step :name="2" title="Phone/Email" icon="phone" :done="step > 2">
      <q-stepper-navigation>
        <q-input v-model="rule.address" placeholder="type phone or email" />
        <q-btn @click="step = 3" color="primary" label="Continue" class="q-ml-sm" />
        <q-btn flat @click="step = 1" color="primary" label="Back" class="q-ml-sm" />
      </q-stepper-navigation>
    </q-step>
    <q-step :name="3" title="Check rule" icon="check">
      We'll also try to match existing SMS with this rule.
      <q-stepper-navigation>
        <q-btn color="primary" label="Check" />
        <q-btn flat @click="step = 2" color="primary" label="Back" class="q-ml-sm" />
      </q-stepper-navigation>
    </q-step>
  </q-stepper>
</template>

<script setup lang="ts">

import { ref, type Ref } from 'vue';
import * as T from '../js/types'

const step = ref(1);
const rule: Ref<T.Rule> = ref(T.createRule())

</script>
