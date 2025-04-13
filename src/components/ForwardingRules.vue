<template>
  <!-- Show existing rules -->
  <div class="text-h6">Forwarding rules</div>
  <q-list separator bordered class="q-my-sm" style="border-radius: 10px">
    <q-item v-for="(rule, key) in rules" :key="key">
      <q-item-section avatar>
        <q-avatar icon="phone" size="28px"> </q-avatar>
      </q-item-section>
      <q-item-section class="col-8">
        {{ rule.glob }}
        <q-chip>from: {{ rule.sender }} </q-chip>
        <q-chip>forward: {{ rule.forward }} </q-chip>
      </q-item-section>
      <q-item-section>
        <div>
          <q-btn @click="deleteRule(rule)" round color="secondary" size="12px" icon="delete" />
          <q-btn round color="primary" size="12px" icon="edit" />
        </div>
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
      <q-step
        :name="1"
        title="Add a matching rule that select SMS for forwarding"
        icon="settings"
        :done="step > 1"
      >
        <q-input
          v-model="thisRule.glob"
          placeholder="*"
          label="glob that text must contain (* means anything)"
        />
        <q-input
          v-model="thisRule.sender"
          placeholder="987 654 3210"
          label="Restrict match to this sender (optional)"
        />

        <q-stepper-navigation>
          <q-btn @click="step = 2" color="primary" label="Continue" />
        </q-stepper-navigation>
      </q-step>
      <q-step :name="2" title="Forward to Phone/Email" icon="phone" :done="step > 2">
        <q-stepper-navigation>
          <q-input v-model="thisRule.forward" placeholder="type phone or email" />
          <q-btn @click="step = 3" color="primary" label="Continue" class="q-ml-sm" />
          <q-btn flat @click="step = 1" color="primary" label="Back" class="q-ml-sm" />
        </q-stepper-navigation>
      </q-step>
      <q-step :name="3" title="Check rule" icon="check">
        <q-btn @click="checkThisRule" color="primary" label="Match" /> existing SMS with this rule.
        <q-stepper-navigation>
          <q-list>
            <div v-if="matchedMessage && matchedMessage.length > 0">
              Following existing SMS were matched.
            </div>
            <format-sms :messages="matchedMessage"></format-sms>
            <q-btn @click="saveRules" color="primary" label="Save" />
            <q-btn flat @click="step = 2" color="primary" label="Back" class="q-ml-sm" />
          </q-list>
        </q-stepper-navigation>
      </q-step>
    </q-stepper>
  </q-expansion-item>
</template>

<script setup lang="ts">
import { ref, onMounted, type Ref } from 'vue';

import * as T from '../js/types';
import * as S from '../js/storage';
import FormatSms from './FormatSms.vue';
import Sms from '../plugins/sms';

// Emit on change
const emit = defineEmits(['change']);

const step = ref(1);
const rules: Ref<T.Rule[]> = ref([]);

const thisRule: Ref<T.Rule> = ref(T.createRule());
const matchedMessage: Ref<T.Message[]> = ref([]);

onMounted(async () => {
  rules.value = (await S.loadRules()) || [];
  emit('change', rules.value);
});

/* Save rules */
const saveRules = async () => {
  rules.value.push(thisRule.value);
  await S.storeRules(rules.value);
  step.value = 1;
  emit('change', rules.value);
};

const checkThisRule = async () => {
  const rulePattern = thisRule.value.glob;
  console.debug('Checking this rule pattern: ' + rulePattern);
  const { result } = await Sms.querySms({ query: rulePattern });
  console.debug('Got matching sms ' + JSON.stringify(result));
  matchedMessage.value = result;
};

const deleteRule = async (rule: T.Rule) => {
  console.info('Deleting rule', rule);
  rules.value = await S.deleteRule(rule);
};
</script>
