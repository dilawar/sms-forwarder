<template>
  <q-list>
    <q-item v-for="(message, key) in messages" :key="key">
      {{ message }}
    </q-item>
  </q-list>
</template>

<script setup lang="ts">
import { type Message } from '../js/types';
defineProps<{ messages: Message[] }>();
</script>
