<template>
  <q-scroll-area style="height: 300px">
    <div class="text-h6 q-py-sm">
      Forwarded SMS: <strong> {{ sortedMessages.length }} </strong>
    </div>
    <q-list separator>
      <q-item v-for="(message, key) in sortedMessages" :key="key">
        <q-item-section>
          <q-item-label>
            <q-chip> From: {{ message.sender }} </q-chip>
            <q-chip> {{ fromNow(message.timestamp) }} </q-chip>
            <q-chip v-if="(message?.matched_rules || []).length > 0">
              {{ message?.matched_rules }}
            </q-chip>
            {{ message.body }}
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-list>
  </q-scroll-area>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { type Message } from '../js/types';
import { fromNow } from '../js/utils';

const { messages } = defineProps<{ messages: Message[] }>();

const sortedMessages = computed(() => {
  return messages.toSorted((a, b) => -a.timestamp + b.timestamp);
});
</script>
