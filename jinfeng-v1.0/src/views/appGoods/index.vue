<template>
  <div class="main">
    <app-link :name="title"></app-link>
  </div>
</template>

<script>
import appLink from "@/components/link";
export default {
  data() {
    return {
      title: "商品管理"
    };
  },

  components: { appLink },

  methods: {}
};
</script>

<style scoped>
</style>