<template>
  <h5>Forwaring rules</h5>

  <q-list clickable v-ripple> </q-list>

  <q-select label="Where to forward?" v-model="typeOfForward" :options="typesOfForward" />

  <q-btn round color="red">Add</q-btn>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const typeOfForward = ref(null);
const typesOfForward = ['email', 'phone'];
</script>
