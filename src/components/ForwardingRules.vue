<template>
  <div class="text-h6">Forwaring rules</div>
  <q-list> </q-list>

  <q-expansion-item
    class="overflow-hidden"
    style="border-radius: 15px"
    icon="add"
    header-class="bg-info text-white"
    label="Click to add forwarding rule"
  >
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
          <q-btn @click="saveRule" color="primary" label="Check And Save" />
          <q-btn flat @click="step = 2" color="primary" label="Back" class="q-ml-sm" />
        </q-stepper-navigation>
      </q-step>
    </q-stepper>
  </q-expansion-item>
</template>

<script setup lang="ts">
import { ref, onMounted, type Ref } from 'vue';

import * as T from '../js/types';
import * as S from '../js/storage';

const step = ref(1);
const ruleId: string = 'rule0';
const rule: Ref<T.Rule> = ref(T.createRule());

onMounted(async () => {
  rule.value = (await S.loadRule(ruleId)) || T.createRule();
});

/**
 * Save this rule.
 */
const saveRule = async () => {
  await S.storeRule(ruleId, rule.value);
};
</script>
