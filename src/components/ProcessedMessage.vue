<template>
  <q-scroll-area style="height: 300px">
    <q-list separator>
      <q-item v-for="(message, key) in messages" :key="key">
        <q-item-section>
          <q-item-label>
            <div>
              From: <span class="text-caption"> {{ message.from_address }} </span>, Date:
              <span class="text-caption"> {{ fromNow(message.timestamp) }} </span>
              <q-chip v-if="(message?.matched_rules || []).length > 0">
                {{ message?.matched_rules }}
              </q-chip>
            </div>
            {{ message.body }}
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-list>
  </q-scroll-area>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { type Message } from '../js/types';
import { fromNow } from '../js/utils';

defineProps<{ messages: Message[] }>();

onMounted(() => {
  console.debug('[processed-message] component was mounted');
});
</script>
