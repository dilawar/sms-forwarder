<template>
  <h5>Forwaring rules</h5>

  <q-stepper v-model="step" vertical color="primary" animated>
    <q-step :name="1" title="Add rules to select SMS for forwarding" icon="settings" :done="step > 1">

      Write some patterns/words that SMS _must_ contain. Enter '*' if you want
      to select every SMS.

      <q-input placeholder="*" label="pattern or word that SMS must contain">
      </q-input>

      <q-stepper-navigation>
        <q-btn @click="step = 2" color="primary" label="Continue" />
      </q-stepper-navigation>
    </q-step>

    <q-step
      :name="2"
      title="Create an ad group"
      caption="Optional"
      icon="create_new_folder"
      :done="step > 2"
    >
      An ad group contains one or more ads which target a shared set of keywords.

      <q-stepper-navigation>
        <q-btn @click="step = 4" color="primary" label="Continue" />
        <q-btn flat @click="step = 1" color="primary" label="Back" class="q-ml-sm" />
      </q-stepper-navigation>
    </q-step>

    <q-step :name="3" title="Ad template" icon="assignment" disable>
      This step won't show up because it is disabled.
    </q-step>

    <q-step :name="4" title="Create an ad" icon="add_comment">
      Try out different ad text to see what brings in the most customers, and learn how to enhance
      your ads using features like ad extensions. If you run into any problems with your ads, find
      out how to tell if they're running and how to resolve approval issues.

      <q-stepper-navigation>
        <q-btn color="primary" label="Finish" />
        <q-btn flat @click="step = 2" color="primary" label="Back" class="q-ml-sm" />
      </q-stepper-navigation>
    </q-step>
  </q-stepper>
</template>

<script setup lang="ts">
import { ref } from 'vue';
const step = ref(1);
</script>
