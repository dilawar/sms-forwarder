<template>
  <q-list separator>
    <q-item v-for="(message, key) in messages" :key="key">
      <q-item-label>
        <q-chip> {{ message.address }} </q-chip>
        <q-chip>{{ dayjs(message.date).fromNow() }}</q-chip>
        {{ message.body }}
      </q-item-label>
    </q-item>
  </q-list>
</template>

<script setup lang="ts">
import { type Message } from '../js/types';

import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
dayjs.extend(relativeTime);

defineProps<{ messages: Message[] }>();
</script>
