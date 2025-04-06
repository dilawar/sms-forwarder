<template>
  <div class="text-h6">Forwaring rules</div>

  <q-list separator bordered class="q-my-sm" style="border-radius:10px">
    <q-item v-for="(rule, key) in rules" :key="key">
      <q-item-section> {{ rule.glob }} </q-item-section>
      <q-item-section> {{ rule.address }} </q-item-section>
      <q-item-section avatar>
        <q-avatar color="primary" icon="phone" size="28px"> </q-avatar>
      </q-item-section>
    </q-item>
  </q-list>

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
        <q-input v-model="thisRule.glob" placeholder="*" label="glob that SMS must contain">
        </q-input>

        <q-stepper-navigation>
          <q-btn @click="step = 2" color="primary" label="Continue" />
        </q-stepper-navigation>
      </q-step>
      <q-step :name="2" title="Phone/Email" icon="phone" :done="step > 2">
        <q-stepper-navigation>
          <q-input v-model="thisRule.address" placeholder="type phone or email" />
          <q-btn @click="step = 3" color="primary" label="Continue" class="q-ml-sm" />
          <q-btn flat @click="step = 1" color="primary" label="Back" class="q-ml-sm" />
        </q-stepper-navigation>
      </q-step>
      <q-step :name="3" title="Check rule" icon="check">
        We'll also try to match existing SMS with this rule.
        <q-stepper-navigation>
          <q-btn @click="saveRules" color="primary" label="Check And Save" />
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
const rules: Ref<T.Rule[]> = ref([]);
const thisRule: Ref<T.Rule> = ref(T.createRule());

onMounted(async () => {
  rules.value = (await S.loadRules()) || [];
});

/* Save rules */
const saveRules = async () => {
  rules.value.push(thisRule.value);
  await S.storeRules(rules.value);
};
</script>
