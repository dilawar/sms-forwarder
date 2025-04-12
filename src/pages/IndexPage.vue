<template>
  <q-page padding>
    <battery-optimization-component title="Battery optimization" />
    <forwarding-rules @change="onUpdateRules" />

    <!-- Result section -->
    <div class="text-h6 q-py-sm">Live Result</div>

    <div class="justify">
      Total SMS processed <strong> {{ processedMessages.length }} </strong>
      <processed-message :messages="processedMessages" />
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
import { ref, type Ref, onMounted, onUnmounted } from 'vue';
import { setIntervalAsync, clearIntervalAsync } from 'set-interval-async';
import { Capacitor } from '@capacitor/core';

import BatteryOptimizationComponent from 'components/BatteryOptimizationComponent.vue';
import ForwardingRules from 'components/ForwardingRules.vue';
import ProcessedMessage from 'components/ProcessedMessage.vue';

import Sms from '../plugins/sms';
import { type Message, type Rule, RuleMatchType } from '../js/types';
import { loadMessages, storeMessage } from '../js/storage';

// Query for searching SMS
const query = ref('');
const matchingRules: Ref<Rule[]> = ref([]);
const processedMessages: Ref<Message[]> = ref([]);
const queryResult: Ref<Message[]> = ref([]);

onMounted(async () => {
  processedMessages.value = await loadMessages();
  console.group('index');
  console.info(`Loaded ${processedMessages.value.length} Messages...`);
});

const onUpdateRules = (rules: Rule[]) => {
  console.info('[index] Got rules from component ', JSON.stringify(rules));
  matchingRules.value = rules;
};

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
    await handleIncomingMessages(result);
  } catch (e) {
    /* handle error */
    console.error('Failed to handle incoming messages: Error is ' + JSON.stringify(e));
  }
};

const handleIncomingMessages = async (messages: Message[]) => {
  console.debug('Handling sms received:' + JSON.stringify(messages));
  for (const message of messages) {
    await handleIncomingMessage(message);
  }
};

const handleIncomingMessage = async (message: Message) => {
  console.debug('Handle incoming message: ' + JSON.stringify(message));

  // TODO: match with given pattern.
  matchingRules.value.forEach( rule => {
    console.debug(111, JSON.stringify(rule));
    if(messageMatchesRule(message, rule) === RuleMatchType.Both ) {
      console.info("Rule %o matches messages %o", rule, message);
    }
  })

  // save in message history.
  processedMessages.value.push(message);
  await storeMessage(message);
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
  console.groupEnd();
});

const messageMatchesRule = (message: Message, rule: Rule): RuleMatchType => {
  let matchType = RuleMatchType.None;
  if(message.from_address === rule.address) {
    matchType = RuleMatchType.Sender;
    console.debug("matchType %s", matchType)
  }

  return matchType
}

</script>
