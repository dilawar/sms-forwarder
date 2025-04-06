<template>
  <q-list bordered separator>
    <q-item v-for="(message, key) in messages" :key="key">
      {{ message }} 
      <q-item-section>
        <q-item-label>{{ message.address }}</q-item-label>
        <q-item-label>{{ message.body }}</q-item-label>
      </q-item-section>
    </q-item>
  </q-list>
</template>

<script setup lang="ts">
import { type Message } from '../plugins/sms'
defineProps<{ messages: Message[]}>()
</script>
