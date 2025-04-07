<template>
  <q-scroll-area style="height: 300px">
    <q-list separator>
      <q-item v-for="(message, key) in messages" :key="key">
        <q-item-section>
          <q-item-label>
            <q-chip dense> {{ message.from_address }} </q-chip>
            <q-chip dense> {{ fromNow(message.timestamp) }} </q-chip>
            {{ message.body }}
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-list>
  </q-scroll-area>
</template>

<script setup lang="ts">
import { type Message } from '../js/types';
import { fromNow } from '../js/utils';

defineProps<{ messages: Message[] }>();
</script>
