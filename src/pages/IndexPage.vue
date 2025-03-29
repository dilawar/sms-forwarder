<template>
  <q-page class="row items-center justify-evenly">
    <example-component
      title="Example component is here"
      active
      :meta="meta"
    ></example-component>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import type { Meta } from 'components/models';

import ExampleComponent from 'components/ExampleComponent.vue';

const meta = ref<Meta>({
  totalCount: 1200
});

</script>
